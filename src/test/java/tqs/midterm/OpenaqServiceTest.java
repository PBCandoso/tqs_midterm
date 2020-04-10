package tqs.midterm;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.mockito.Mockito.*;

import com.gargoylesoftware.htmlunit.HttpHeader;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import tqs.midterm.cache.CacheManager;
import tqs.midterm.entity.City;
import tqs.midterm.entity.Country;
import tqs.midterm.entity.Location;
import tqs.midterm.entity.Measurement;
import tqs.midterm.service.OpenaqService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


public class OpenaqServiceTest {


    // Cache Mock
    @Mock
    CacheManager cache;
    // Service to test
    @InjectMocks
    OpenaqService service;
    // Mock for WebClient
    private static MockWebServer mockWebServer;

    private String baseUrl = "https://api.openaq.org/v1/";

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void eachSetup(){
        MockitoAnnotations.initMocks(this);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        this.baseUrl = String.format("http://localhost:%s",
                mockWebServer.getPort());
        service = new OpenaqService(baseUrl,cache);
    }

    // Utility methods

    // Creates (queues) mock responses from MockWebServer
    void mockGetCountries(){
        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"meta\":{\"name\":\"openaq-api\",\"license\":\"CC BY 4.0\",\"website\":\"https://docs.openaq.org/\",\"page\":1,\"limit\":100,\"found\":98},\"results\":[{\"code\":\"AD\",\"count\":118514,\"locations\":3,\"cities\":2,\"name\":\"Andorra\"},{\"code\":\"AE\",\"count\":77555,\"locations\":4,\"cities\":3,\"name\":\"United Arab Emirates\"}]}")
        );
    }

    void mockGetCities(){
        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"meta\":{\"name\":\"openaq-api\",\"license\":\"CC BY 4.0\",\"website\":\"https://docs.openaq.org/\",\"page\":1,\"limit\":100,\"found\":16},\"results\":[{\"country\":\"PT\",\"name\":\"Aveiro\",\"city\":\"Aveiro\",\"count\":231279,\"locations\":4},{\"country\":\"PT\",\"name\":\"Braga\",\"city\":\"Braga\",\"count\":102910,\"locations\":3}]}")
        );
    }

    void mockGetLatest(){
        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"meta\":{\"name\":\"openaq-api\",\"license\":\"CC BY 4.0\",\"website\":\"https://docs.openaq.org/\",\"page\":1,\"limit\":100,\"found\":4},\"" +
                        "results\":[{\"location\":\"PT01054\",\"city\":\"Aveiro\",\"country\":\"PT\",\"distance\":8783328.209930472,\"" + "measurements\":[" +
                        "{\"parameter\":\"no2\",\"value\":57.7,\"lastUpdated\":\"2020-03-30T09:00:00.000Z\",\"unit\":\"µg/m³\",\"sourceName\":\"EEA Portugal\",\"averagingPeriod\":{\"value\":1,\"unit\":\"hours\"}}," +
                        "{\"parameter\":\"o3\",\"value\":42,\"lastUpdated\":\"2020-04-10T10:00:00.000Z\",\"unit\":\"µg/m³\",\"sourceName\":\"EEA Portugal\",\"averagingPeriod\":{\"value\":1,\"unit\":\"hours\"}}," +
                        "{\"parameter\":\"pm10\",\"value\":0,\"lastUpdated\":\"2020-04-10T10:00:00.000Z\",\"unit\":\"µg/m³\",\"sourceName\":\"EEA Portugal\",\"averagingPeriod\":{\"value\":1,\"unit\":\"hours\"}}],\"" +
                        "coordinates\":{\"latitude\":40.999443,\"longitude\":-8.623333}}]}")
        );
    }

    // Tests

    // Test both the request sent to the third party API, and the response that should contain a list with 2 countries
    @Test
    void getCountriesTest() throws InterruptedException {

        mockGetCountries();

        Mono<List<Country>> res = service.getCountries();
        List<Country> expectedCountries = Arrays.asList(new Country("AD","Andorra"),new Country("AE","United Arab Emirates"));

        // Test the response

        StepVerifier.create(res).expectNext(expectedCountries).verifyComplete();

        // Test the request

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertThat("GET", is(recordedRequest.getMethod()));
        assertThat("/countries", is(recordedRequest.getPath()));
    }

    // Test both the request sent to the third party API, and the response that should contain a list with 2 cities
    @Test
    void getCitiesTest() throws InterruptedException {

        mockGetCities();

        Mono<List<City>> res = service.getCities("PT");
        List<City> expectedCities = Arrays.asList(new City("PT","Aveiro"),new City("PT","Braga"));

        // Test the response

        StepVerifier.create(res).expectNext(expectedCities).verifyComplete();

        // Test the request

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertThat("GET", is(recordedRequest.getMethod()));
        assertThat("/cities?country=PT", is(recordedRequest.getPath()));
    }

    // Test both the request sent to the third party API, and the response that should contain a list with 1 location
    // which in itself contains 3 different measurements
    @Test
    void getLatestTest() throws InterruptedException{

        mockGetLatest();

        Mono<List<Location>> res = service.getLatestCityAir("Aveiro");
        List<Location> expectedLocations = Arrays.asList(
                new Location("PT01054", Arrays.asList(
                                new Measurement("no2",57.7,"µg/m³","2020-03-30T09:00:00.000Z"),
                                new Measurement("o3",42,"µg/m³","2020-04-10T10:00:00.000Z"),
                                new Measurement("pm10",0,"µg/m³","2020-04-10T10:00:00.000Z")
                        ))
        );

        // Test the response

        StepVerifier.create(res).expectNext(expectedLocations).verifyComplete();

        // Test the request

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertThat("GET", is(recordedRequest.getMethod()));
        assertThat("/latest?city=Aveiro", is(recordedRequest.getPath()));

    }

    // Test that the cache is being called and returns the correct object
    @Test
    void getCountriesCacheTest(){

        mockGetCountries();

        // Value will be cached
        Mono<List<Country>> res1 = service.getCountries();
        when(cache.get(anyString())).thenReturn(res1);

        // The object is added once to the cache
        verify(cache,times(1)).add(eq(this.baseUrl+"/countries"), any(),eq(res1));

        Mono<List<Country>> res2 = service.getCountries();

        // We check if the value is in the cache once per request, so we used it twice
        verify(cache,times(2)).get(this.baseUrl+"/countries");

        // Assert that we return the correct cached value
        assertThat(res1,is(res2));

    }

    @Test
    void getCitiesCacheTest(){

        mockGetCities();

        // Value will be cached
        Mono<List<City>> res1 = service.getCities("PT");
        when(cache.get(anyString())).thenReturn(res1);

        // The object is added once to the cache
        verify(cache,times(1)).add(eq(this.baseUrl+"/cities?&country=PT"), any(),eq(res1));

        Mono<List<City>> res2 = service.getCities("PT");

        // We check if the value is in the cache once per request, so we used it twice
        verify(cache,times(2)).get(this.baseUrl+"/cities?&country=PT");

        // Assert that we return the correct cached value
        assertThat(res1,is(res2));
    }

    @Test
    void getLatestCacheTest(){

        mockGetLatest();

        // Value will be cached
        Mono<List<Location>> res1 = service.getLatestCityAir("Aveiro");
        when(cache.get(anyString())).thenReturn(res1);

        // The object is added once to the cache
        verify(cache,times(1)).add(eq(this.baseUrl+"/latest?&city=Aveiro"), any(),eq(res1));

        Mono<List<Location>> res2 = service.getLatestCityAir("Aveiro");

        // We check if the value is in the cache once per request, so we used it twice
        verify(cache,times(2)).get(this.baseUrl+"/latest?&city=Aveiro");

        // Assert that we return the correct cached value
        assertThat(res1,is(res2));
    }


}
