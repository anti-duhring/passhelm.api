package com.passhelm.passhelm.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void shouldGet200StatusCodeWhenGetAllCategories() throws Exception {

        URI uri = URI.create("http://localhost:8080/api/v1/category");

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(uri)
        )
                .andExpectAll(
                        MockMvcResultMatchers.status().is(200)
                );
    }

    @Test
    @Order(2)
    void shouldGet200StatusCodeAndCategoryDataWhenCreateCategory() throws Exception{

        URI uri = URI.create("http://localhost:8080/api/v1/category");

        String json = "{\n" +
                "    \"userId\": 1,\n" +
                "    \"label\": \"Trabalho\",\n" +
                "    \"color\": \"#000000\"\n" +
                "}";

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post(uri)
                                .contentType("application/json")
                                .content(json)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().is(200),
                        MockMvcResultMatchers.jsonPath("$.userId").value(1),
                        MockMvcResultMatchers.jsonPath("$.label").value("Trabalho"),
                        MockMvcResultMatchers.jsonPath("$.color").value("#000000")
                );
    }

    @Test
    @Order(3)
    void shouldThrowsIllegalStateExceptionWhenUserDoesNotExist() throws Exception{

        URI uri = URI.create("http://localhost:8080/api/v1/category");

        String json = "{\n" +
                "    \"userId\": 999,\n" +
                "    \"label\": \"Trabalho\",\n" +
                "    \"color\": \"#000000\"\n" +
                "}";

        try {
            mockMvc
                    .perform(
                            MockMvcRequestBuilders
                                    .post(uri)
                                    .contentType("application/json")
                                    .content(json)
                    )
                    .andReturn();

        } catch (Exception e) {

            Assertions.assertEquals( "Request processing failed: java.lang.IllegalStateException: User does not exist",
                    e.getMessage());
        }

    }

    @Test
    @Order(4)
    void shouldThrowsIllegalStateExceptionWhenLabelIsEmpty() throws Exception{

        URI uri = URI.create("http://localhost:8080/api/v1/category");

        String json = "{\n" +
                "    \"userId\": 1,\n" +
                "    \"label\": \"\",\n" +
                "    \"color\": \"#000000\"\n" +
                "}";

        try {
            mockMvc
                    .perform(
                            MockMvcRequestBuilders
                                    .post(uri)
                                    .contentType("application/json")
                                    .content(json)
                    )
                    .andReturn();

        } catch (Exception e) {

            Assertions.assertEquals( "Request processing failed: java.lang.IllegalStateExcept" +
                            "ion: Label cannot be empty",
                    e.getMessage());
        }

    }

    @Test
    @Order(5)
    void shouldThrowsIllegalStateExceptionWhenColorIsEmpty() throws Exception{

        URI uri = URI.create("http://localhost:8080/api/v1/category");

        String json = "{\n" +
                "    \"userId\": 1,\n" +
                "    \"label\": \"Trabalho\",\n" +
                "    \"color\": \"\"\n" +
                "}";

        try {
            mockMvc
                    .perform(
                            MockMvcRequestBuilders
                                    .post(uri)
                                    .contentType("application/json")
                                    .content(json)
                    )
                    .andReturn();

        } catch (Exception e) {

            Assertions.assertEquals( "Request processing failed: java.lang.IllegalStateExcept" +
                            "ion: Color cannot be empty",
                    e.getMessage());
        }

    }
}