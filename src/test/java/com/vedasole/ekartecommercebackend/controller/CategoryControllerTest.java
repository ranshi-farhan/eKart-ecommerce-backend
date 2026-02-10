package com.vedasole.ekartecommercebackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedasole.ekartecommercebackend.config.TestMailConfig;
import com.vedasole.ekartecommercebackend.entity.Category;
import com.vedasole.ekartecommercebackend.repository.CategoryRepository;
import com.vedasole.ekartecommercebackend.util.TestApplicationInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestMailConfig.class)
class CategoryControllerTest {

    private static final String BASE_URL = "/api/v1/categories";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private TestApplicationInitializer testApplicationInitializer;

    private Category expected;

    @BeforeEach
    void setUp() {
        expected = new Category(
                1L,
                "Mobiles & Tablets",
                "/images/categories/mobile-and-tablets.webp",
                "All mobile phones and tablets",
                null,
                true,
                LocalDateTime.of(2022, 1, 1, 1, 0),
                LocalDateTime.of(2022, 1, 1, 1, 0)
        );
    }

    @Test
    void shouldCreateCategory() throws Exception {
        when(categoryRepository.save(any(Category.class))).thenReturn(expected);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expected))
                        .header("Authorization",
                                "Bearer ".concat(testApplicationInitializer.getAdminToken())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mobiles & Tablets"));
    }

    @Test
    void shouldGetAllCategories() throws Exception {
        when(categoryRepository.findAll()).thenReturn(List.of(expected));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Mobiles & Tablets"));
    }

    @Test
    void shouldGetCategoryById() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(expected));

        mockMvc.perform(get(BASE_URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mobiles & Tablets"));
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(expected));
        when(categoryRepository.save(any(Category.class))).thenReturn(expected);

        mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expected))
                        .header("Authorization",
                                "Bearer ".concat(testApplicationInitializer.getAdminToken())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mobiles & Tablets"));
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(expected));

        mockMvc.perform(delete(BASE_URL + "/{id}", 1L)
                        .header("Authorization",
                                "Bearer ".concat(testApplicationInitializer.getAdminToken())))
                .andExpect(status().isNoContent());
    }
}
