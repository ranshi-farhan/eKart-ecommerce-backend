package com.vedasole.ekartecommercebackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedasole.ekartecommercebackend.config.TestMailConfig;
import com.vedasole.ekartecommercebackend.entity.Category;
import com.vedasole.ekartecommercebackend.payload.CategoryDto;
import com.vedasole.ekartecommercebackend.service.service_interface.CategoryService;
import com.vedasole.ekartecommercebackend.utility.TestApplicationInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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
    private CategoryService categoryService;

    @Autowired
    private TestApplicationInitializer testApplicationInitializer;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        category = new Category(
                1L,
                "Mobiles & Tablets",
                "/images/categories/mobile-and-tablets.webp",
                "All mobile phones and tablets",
                null,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        categoryDto = new ModelMapper().map(category, CategoryDto.class);
    }

    @Test
    void shouldCreateCategory() throws Exception {
        given(categoryService.createCategory(any(CategoryDto.class)))
                .willReturn(categoryDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .header("Authorization",
                                "Bearer " + testApplicationInitializer.getAdminToken()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mobiles & Tablets"));
    }

    @Test
    void shouldGetAllCategories() throws Exception {
        given(categoryService.getAllCategories())
                .willReturn(List.of(categoryDto));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.categories").isArray())
                .andExpect(jsonPath("$._embedded.categories[0].name")
                        .value("Mobiles & Tablets"));
    }

    @Test
    void shouldGetCategoryById() throws Exception {
        given(categoryService.getCategoryById(1L))
                .willReturn(categoryDto);

        mockMvc.perform(get(BASE_URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mobiles & Tablets"));
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        given(categoryService.updateCategory(any(CategoryDto.class), eq(1L)))
                .willReturn(categoryDto);

        mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .header("Authorization",
                                "Bearer " + testApplicationInitializer.getAdminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mobiles & Tablets"));
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", 1L)
                        .header("Authorization",
                                "Bearer " + testApplicationInitializer.getAdminToken()))
                .andExpect(status().isOk());
    }
}
