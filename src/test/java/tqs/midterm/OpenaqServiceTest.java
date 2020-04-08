package tqs.midterm;

import org.junit.jupiter.api.BeforeEach;
import tqs.midterm.service.OpenaqService;

public class OpenaqServiceTest
{

    OpenaqService service;

    @BeforeEach
    void setup(){service = new OpenaqService();}
}
