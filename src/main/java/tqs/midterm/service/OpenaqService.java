package tqs.midterm.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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

    public OpenaqService(String url){
        this.baseUrl=url;
        this.webClient = WebClient.create(this.baseUrl);
        this.cache = new CacheManager();
    }

    // Get available countries
    public Mono<List<Country>> getCountries(){
        @SuppressWarnings("unchecked")
        Mono<List<Country>> cached = (Mono<List<Country>>) this.cache.get(this.baseUrl+"/countries");
        if(cached == null){
            Mono<List<Country>> res= webClient.get()
                    .uri("/countries")
                    .retrieve()
                    .bodyToMono(CountryList.class)
                    .flatMap( response -> Mono.just(response.getResults()));
            this.cache.add(this.baseUrl+"/countries", LocalDateTime.now().plusMinutes(60),res);
            return res;
        }
        else {
            return cached;
        }
    }

    // Get available cities (within a country)
    public Mono<List<City>> getCities(String country){
        String searchUri="/cities" + (country != null ? "?&country="+country : "");
        @SuppressWarnings("unchecked")
        Mono<List<City>> cached = (Mono<List<City>>) this.cache.get(this.baseUrl+searchUri);
        if(cached == null) {
            Mono<List<City>> res = webClient.get()
                    .uri(searchUri)
                    .retrieve()
                    .bodyToMono(CityList.class)
                    .flatMap(response -> Mono.just(response.getResults()));
            this.cache.add(this.baseUrl+searchUri, LocalDateTime.now().plusMinutes(60), res);
            return res;
        }
        else {
            return cached;
        }
    }

    // Get city air measurements
    public Mono<List<Location>> getLatestCityAir(String city){
        String searchUri="/latest?&city="+city;
        @SuppressWarnings("unchecked")
        Mono<List<Location>> cached = (Mono<List<Location>>) this.cache.get(this.baseUrl+searchUri);
        if(cached == null) {
            Mono<List<Location>> res = webClient.get()
                    .uri(searchUri)
                    .retrieve()
                    .bodyToMono(LatestList.class)
                    .flatMap(response -> Mono.just(response.getResults()));
            this.cache.add(this.baseUrl+searchUri, LocalDateTime.now().plusMinutes(60), res);
            return res;
        }
        else {
            return cached;
        }
    }
}
