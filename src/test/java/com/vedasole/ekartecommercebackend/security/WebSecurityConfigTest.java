package com.vedasole.ekartecommercebackend.security;

import com.vedasole.ekartecommercebackend.utility.TestApplicationInitializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Slf4j
class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestApplicationInitializer testApplicationInitializer;

    @Test
    void whenAccessPublicUrl_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/categories"))
                .andDo(result ->
                        log.debug("Public endpoint response: {}",
                                result.getResponse().getContentAsString()))
                .andExpect(status().isOk());
    }

    @Test
    void whenAccessProtectedUrlWithoutAuth_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/auth/check-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenAccessGeneralProtectedUrlWithAuth_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/auth/check-token")
                        .header("Authorization",
                                "Bearer " + testApplicationInitializer.getUserToken()))
                .andExpect(status().isOk());
    }

    @Test
    void whenAccessAdminProtectedUrlWithAdminAuth_thenAllowed() throws Exception {
        mockMvc.perform(delete("/api/v1/customers/3")
                        .header("Authorization",
                                "Bearer " + testApplicationInitializer.getAdminToken()))
                // We only care that ADMIN is allowed past security
                .andExpect(status().isNotUnauthorized())
                .andExpect(status().isNotForbidden());
    }
}
