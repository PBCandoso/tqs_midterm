package tqs.midterm.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import tqs.midterm.cache.CacheManager;
import tqs.midterm.entity.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OpenaqService {

    private String baseUrl = "https://api.openaq.org/v1/";

    private WebClient webClient;
    private CacheManager cache;

    public OpenaqService(){
        this.webClient = WebClient.create(this.baseUrl);
        this.cache = new CacheManager();
    }

    // Get available countries
    public Flux<List<Country>> getCountries(){
        @SuppressWarnings("unchecked")
        Flux<List<Country>> cached = (Flux<List<Country>>) this.cache.get(this.baseUrl+"/countries");
        if(cached == null){
            Flux<List<Country>> res= webClient.get()
                    .uri("/countries")
                    .retrieve()
                    .bodyToFlux(CountryList.class)
                    .flatMap( response -> Flux.just(response.getResults()));

            this.cache.add(this.baseUrl+"/countries", LocalDateTime.now().plusMinutes(60),res);
            return res;
        }
        else {
            return cached;
        }
    }

    // Get available cities (within a country)
    public Flux<List<City>> getCities(String country){
        String searchUri="/cities" + (country != null ? "?&country="+country : "");
        @SuppressWarnings("unchecked")
        Flux<List<City>> cached = (Flux<List<City>>) this.cache.get(this.baseUrl+searchUri);
        if(cached == null) {
            Flux<List<City>> res = webClient.get()
                    .uri(searchUri)
                    .retrieve()
                    .bodyToFlux(CityList.class)
                    .flatMap(response -> Flux.just(response.getResults()));
            this.cache.add(this.baseUrl+searchUri, LocalDateTime.now().plusMinutes(60), res);
            return res;
        }
        else {
            return cached;
        }
    }

    // Get city air measurements
    public Flux<List<Location>> getLatestCityAir(String city){
        String searchUri="/latest?&city="+city;
        @SuppressWarnings("unchecked")
        Flux<List<Location>> cached = (Flux<List<Location>>) this.cache.get(this.baseUrl+searchUri);
        if(cached == null) {
            Flux<List<Location>> res = webClient.get()
                    .uri(searchUri)
                    .retrieve()
                    .bodyToFlux(LatestList.class)
                    .flatMap(response -> Flux.just(response.getResults()));
            this.cache.add(this.baseUrl+searchUri, LocalDateTime.now().plusMinutes(60), res);
            return res;
        }
        else {
            return cached;
        }
    }
}
