package tqs.midterm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import tqs.midterm.entity.CityList;
import tqs.midterm.entity.CountryList;
import tqs.midterm.entity.StateList;
import tqs.midterm.service.AirQualityService;

@RestController
public class AirQualityController {

    private AirQualityService airService;

    public AirQualityController(AirQualityService service) {
        this.airService = service;
    }

    @GetMapping("/countries")
    private Flux<CountryList> getContries() {
        return airService.getCountries();
    }

    @GetMapping("/states")
    private Flux<StateList> getStates(@RequestParam String country) {
        return airService.getStates(country);
    }

    @GetMapping("/cities")
    private Flux<CityList> getCities(@RequestParam String state, @RequestParam String country){
        return airService.getCities(country,state);
    }

}
