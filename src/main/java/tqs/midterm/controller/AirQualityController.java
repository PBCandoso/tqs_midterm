package tqs.midterm.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tqs.midterm.entity.*;
import tqs.midterm.service.OpenaqService;

import java.util.List;

@RestController
public class AirQualityController {

    private OpenaqService airService;

    public AirQualityController(OpenaqService service) {
        this.airService = service;
    }

    @CrossOrigin
    @GetMapping("/countries")
    private Mono<List<Country>> getContries() {
        return airService.getCountries();
    }

    @CrossOrigin
    @GetMapping("/cities")
    private Mono<List<City>> getCities(@RequestParam(required=false) String country){
        return airService.getCities(country);
    }

    @CrossOrigin
    @GetMapping("/latest")
    private Mono<List<Location>> getAirData(@RequestParam String city){
        return airService.getLatestCityAir(city);
    }


}
