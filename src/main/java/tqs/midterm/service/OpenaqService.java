package tqs.midterm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import tqs.midterm.entity.*;

import java.util.List;
import java.util.Map;

@Service
public class OpenaqService {

    private String baseUrl = "https://api.openaq.org/v1/";

    private WebClient webClient;

    public OpenaqService(){
        webClient = WebClient.create(this.baseUrl);
    }

    // Get available countries
    public Flux<List<Country>> getCountries(){
        return webClient.get()
                .uri("/countries")
                .retrieve()
                .bodyToFlux(CountryList.class)
                .flatMap( response -> Flux.just(response.getResults()));
    }

    // Get available cities (within a country)
    public Flux<List<City>> getCities(String country){
        return webClient.get()
                .uri("/cities" + (country != null ? "?&country="+country : ""))
                .retrieve()
                .bodyToFlux(CityList.class)
                .flatMap( response -> Flux.just(response.getResults()));
    }

    // Get city air measurements
    public Flux<List<Location>> getLatestCityAir(String city){
        return webClient.get()
                .uri("/latest?&city="+city)
                .retrieve()
                .bodyToFlux(LatestList.class)
                .flatMap( response -> Flux.just(response.getResults()));
    }
}
