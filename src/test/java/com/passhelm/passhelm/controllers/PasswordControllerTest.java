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
class PasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void shouldGet200StatusCodeWhenGetAllPasswordsFromUser() throws Exception {

        URI uri = URI.create("http://localhost:8080/api/v1/password?userId=1");

        mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
        )
                .andExpectAll(
                        MockMvcResultMatchers.status().is(200)
                );
    }

    @Test
    @Order(2)
    void shouldGet200StatusCodeAndPasswordDataWhenCreateAPassword() throws Exception {

        URI uri = URI.create("http://localhost:8080/api/v1/password");
        String json = "{\n" +
                "        \"userId\": 1,\n" +
                "        \"categoryId\": 1,\n" +
                "        \"title\": \"Teste\",\n" +
                "        \"login\": \"teste\",\n" +
                "        \"password\": \"123456\"\n" +
                " }";

        mockMvc.perform(
                MockMvcRequestBuilders.post(uri)
                      .contentType("application/json")
                      .content(json)
        )
                .andExpectAll(
                        MockMvcResultMatchers.status().is(200),
                        MockMvcResultMatchers.jsonPath("$.userId").value(1),
                        MockMvcResultMatchers.jsonPath("$.categoryId").value(1),
                        MockMvcResultMatchers.jsonPath("$.title").value("Teste"),
                        MockMvcResultMatchers.jsonPath("$.login").value("teste"),
                        MockMvcResultMatchers.jsonPath("$.password").value("123456")
                );

    }

    @Test
    @Order(3)
    void shouldGet200StatusCodeAndPasswordDataWhenUpdateAPassword() throws Exception {

        URI uri = URI.create("http://localhost:8080/api/v1/password/2");
        String json = "{\n" +
                "    \"categoryId\": 1,\n" +
                "    \"title\": \"Test2\",\n" +
                "    \"login\": \"test2\",\n" +
                "    \"password\": \"123456\"\n" +
                " }";

        mockMvc.perform(
                MockMvcRequestBuilders.put(uri)
                     .contentType("application/json")
                     .content(json)
        )
                .andExpectAll(
                        MockMvcResultMatchers.status().is(200),
                        MockMvcResultMatchers.jsonPath("$.userId").value(1),
                        MockMvcResultMatchers.jsonPath("$.categoryId").value(1),
                        MockMvcResultMatchers.jsonPath("$.title").value("Test2"),
                        MockMvcResultMatchers.jsonPath("$.login").value("test2"),
                        MockMvcResultMatchers.jsonPath("$.password").value("123456")
                );
    }

    @Test
    @Order(4)
    void shouldGet200StatusCodeWhenDeleteAPassword() throws Exception {
        URI uri = URI.create("http://localhost:8080/api/v1/password/2");

        mockMvc.perform(
                MockMvcRequestBuilders.delete(uri)
        )
               .andExpectAll(
                        MockMvcResultMatchers.status().is(200)
                );
    }
}