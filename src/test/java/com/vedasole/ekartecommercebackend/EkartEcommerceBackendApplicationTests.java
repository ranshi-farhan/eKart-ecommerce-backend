package com.vedasole.ekartecommercebackend;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
class EkartEcommerceBackendApplicationTests {

    @Test
    void contextLoads() {
        // If application context fails, test will fail automatically
    }

    @Test
    void whenCreatingModelMapper_thenNoExceptions() {
        assertDoesNotThrow(ModelMapper::new);
    }
}
