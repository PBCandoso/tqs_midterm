package tqs.midterm;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tqs.midterm.entity.Country;
import tqs.midterm.entity.CountryList;
import tqs.midterm.service.AirQualityService;

@SpringBootApplication
public class MidtermApplication {

    public static void main(String[] args) {
        SpringApplication.run(MidtermApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() throws Exception {
        return args -> {
            System.out.println("Running");
        };
    }

}
