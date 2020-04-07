package tqs.midterm;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tqs.midterm.service.OpenaqService;

@SpringBootApplication
public class MidtermApplication {

    public static void main(String[] args) {
        SpringApplication.run(MidtermApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() throws Exception {
        return args -> {
            OpenaqService service = new OpenaqService();
        };
    }

}
