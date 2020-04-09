package tqs.midterm;

import com.gargoylesoftware.htmlunit.HttpHeader;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import tqs.midterm.cache.CacheManager;
import tqs.midterm.entity.Country;
import tqs.midterm.service.OpenaqService;

import java.io.IOException;
import java.util.List;



public class OpenaqServiceTest {

    // Service to test
    @InjectMocks
    private OpenaqService service;
    // Mock the web client and cache
    @Mock
    private CacheManager cache;

    public static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }


    private final String baseUrl = "https://api.openaq.org/v1/";


    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockWebServer.getPort());
        service = new OpenaqService(baseUrl);
    }

    @Test
    void getCountriesTest() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("{\"meta\":{\"name\":\"openaq-api\",\"license\":\"CC BY 4.0\",\"website\":\"https://docs.openaq.org/\",\"page\":1,\"limit\":100,\"found\":98},\"results\":[{\"code\":\"AD\",\"count\":118514,\"locations\":3,\"cities\":2,\"name\":\"Andorra\"},{\"code\":\"AE\",\"count\":77555,\"locations\":4,\"cities\":3,\"name\":\"United Arab Emirates\"}]}")
        );

        Mono<List<Country>> res = service.getCountries();
        List<Country> resCountries = res.block();

    }




}
