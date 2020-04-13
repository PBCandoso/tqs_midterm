package tqs.midterm.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import tqs.midterm.entity.*;
import tqs.midterm.service.OpenaqService;

import java.util.List;

@RestController
public class AirQualityController {

    private final OpenaqService airService;
    private HttpHeaders headers;

    public AirQualityController(OpenaqService service) {
        this.airService = service;
        this.headers=new HttpHeaders();
        headers.add("Access-Control-Allow-Origin","*");
    }

    @GetMapping("/countries")
    public ResponseEntity<Mono<List<Country>>> getCountries() {
        return new ResponseEntity<>(airService.getCountries(),this.headers, HttpStatus.OK);
    }

    @GetMapping("/cities")
    public ResponseEntity<Mono<List<City>>> getCities(@RequestParam(required=false) String country){
        return new ResponseEntity<>(airService.getCities(country),this.headers, HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<Mono<List<Location>>> getAirData(@RequestParam String city){
        return new ResponseEntity<>(airService.getLatestCityAir(city),this.headers, HttpStatus.OK);
    }


}
