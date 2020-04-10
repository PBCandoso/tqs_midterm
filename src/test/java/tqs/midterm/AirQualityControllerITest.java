package tqs.midterm;


import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Mono;
import tqs.midterm.controller.AirQualityController;
import tqs.midterm.entity.City;
import tqs.midterm.entity.Country;
import tqs.midterm.entity.Location;
import tqs.midterm.entity.Measurement;
import tqs.midterm.service.OpenaqService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AirQualityController.class)
public class AirQualityControllerITest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OpenaqService service;

    @Test
    public void getCountries_returnsJsonArray() throws Exception {
        Country portugal = new Country("PT","Portugal");
        Country andorra = new Country("AD","Andorra");
        Mono<List<Country>> expected = Mono.just(Arrays.asList(portugal,andorra));

        given(service.getCountries()).willReturn(expected);

        // Use MvcResult in order for MockMvc to support the async request
        MvcResult result = mvc.perform(get("/countries").contentType(MediaType.APPLICATION_JSON)).andReturn();

        // 2 results are returned and check the fields in some of them
        mvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].name",is(portugal.getName())))
                .andExpect(jsonPath("$[1].code",is(andorra.getCode())));
        // Assert that the service is called once
        verify(service, VerificationModeFactory.times(1)).getCountries();
        reset(service);
    }

    @Test
    public void getCity_returnsJsonArray() throws Exception {
        City aveiro = new City("PT","Aveiro");
        City porto = new City("PT","Porto");
        Mono<List<City>> expected = Mono.just(Arrays.asList(aveiro,porto));

        given(service.getCities("PT")).willReturn(expected);

        MvcResult result = mvc.perform(get("/cities?&country=PT").contentType(MediaType.APPLICATION_JSON)).andReturn();

        // 2 results are returned and we check the fields in some of them
        mvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[1].name",is(porto.getName())))
                .andExpect(jsonPath("$[0].country",is(aveiro.getCountry())));

        verify(service, VerificationModeFactory.times(1)).getCities("PT");
        reset(service);
    }

    @Test
    public void getLatest_returnsJsonArray() throws Exception {
        Measurement m1 = new Measurement("no2",57.7,"µg/m³","2020-03-30T09:00:00.000Z");
        Measurement m2 = new Measurement("pm10",0,"µg/m³",	"2020-04-10T15:00:00.000Z");
        Location local1 = new Location("PT1054",Arrays.asList(m1,m2));

        Mono<List<Location>> expected = Mono.just(Arrays.asList(local1));

        given(service.getLatestCityAir("Aveiro")).willReturn(expected);

        MvcResult result = mvc.perform(get("/latest?&city=Aveiro").contentType(MediaType.APPLICATION_JSON)).andReturn();

        mvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].location",is(local1.getLocation())))
                .andExpect(jsonPath("$[0].measurements[0].parameter",is(m1.getParameter())))
                .andExpect(jsonPath("$[0].measurements[1].value",is(m2.getValue())));

        verify(service, VerificationModeFactory.times(1)).getLatestCityAir("Aveiro");
        reset(service);
    }
}
