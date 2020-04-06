package tqs.midterm.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import tqs.midterm.entity.CityList;
import tqs.midterm.entity.CountryList;
import tqs.midterm.entity.StateList;

@Service
public class AirQualityService {

    private String api_key = "9a19be54-3bce-4410-ae0c-1220f981fc03";
    private String baseUrl = "https://api.airvisual.com/v2";

    private WebClient webClient;

    public AirQualityService(){
        webClient = WebClient.create(this.baseUrl);
    }

    // Get available countries for user to select
    public Flux<CountryList> getCountries(){
        return webClient.get()
                .uri("/countries?key="+this.api_key)
                .retrieve()
                .bodyToFlux(CountryList.class);
    }

    // Get available states for user to select (within a country)
    public Flux<StateList> getStates(String country){
        return webClient.get()
                .uri("/states?country="+country+"&key="+this.api_key)
                .retrieve()
                .bodyToFlux(StateList.class);
    }


    // Get available cities for user to select (within a country and a state)
    public Flux<CityList> getCities(String country, String state){
        return webClient.get()
                .uri("/cities?state="+state+"&country="+country+"&key="+this.api_key)
                .retrieve()
                .bodyToFlux(CityList.class);
    }

}
