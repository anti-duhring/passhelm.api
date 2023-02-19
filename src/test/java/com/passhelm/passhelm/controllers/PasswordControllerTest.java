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
import java.nio.file.AccessDeniedException;

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

    private String login(String login, String password) throws Exception{
        URI uriLogin = URI.create("http://localhost:8080/api/v1/login");
        String dataLogin = "{\n" +
                "    \"login\": \"" + login + "\",\n" +
                "    \"password\": \"" + password + "\"\n" +
                "}";
        final String[] token = {""};
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .post(uriLogin)
                            .contentType("application/json")
                            .content(dataLogin)
                    )
                    .andDo(result -> token[0] = result.getResponse().getContentAsString().split("\"token" +
                            "\":\"")[1].split("\"")[0]);
        } catch (Exception e) {
            throw new AccessDeniedException("Error when trying to login");
        }

        return token[0];
    }

    @Test
    @Order(1)
    @DisplayName("Should get 200 when look for all passwords from itself")
    void shouldGet200StatusCodeWhenGetAllPasswordsItself() throws Exception {
        String token = login("tombrady", "123456");
        URI uri = URI.create("http://localhost:8080/api/v1/password?userId=2");

        mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .header("Authorization", "Bearer " + token)
        )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @Order(1)
    @DisplayName("Should get 403 when look for all passwords from another user")
    void shouldGet403StatusCodeWhenGetAllPasswordsFromAnotherUser() throws Exception {
        String token = login("tombrady", "123456");
        URI uri = URI.create("http://localhost:8080/api/v1/password?userId=1");

        mockMvc.perform(
                        MockMvcRequestBuilders.get(uri)
                                .header("Authorization", "Bearer " + token)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isForbidden()
                );
    }

    @Test
    @Order(2)
    @DisplayName("Should get 200 and password data when create a password")
    void shouldGet200StatusCodeAndPasswordDataWhenCreateAPassword() throws Exception {
        String token = login("mateusvnlima", "123456");
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
                        .header("Authorization", "Bearer " + token)
        )
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.jsonPath("$.userId").value(1),
                        MockMvcResultMatchers.jsonPath("$.categoryId").value(1),
                        MockMvcResultMatchers.jsonPath("$.title").value("Teste"),
                        MockMvcResultMatchers.jsonPath("$.login").value("teste"),
                        MockMvcResultMatchers.jsonPath("$.password").value("123456")
                );

    }

    @Test
    @Order(3)
    @DisplayName("Should get 200 and password data when update a password")
    void shouldGet200StatusCodeAndPasswordDataWhenUpdateAPassword() throws Exception {
        String token = login("mateusvnlima", "123456");
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
                        .header("Authorization", "Bearer " + token)
        )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.userId").value(1),
                        MockMvcResultMatchers.jsonPath("$.categoryId").value(1),
                        MockMvcResultMatchers.jsonPath("$.title").value("Test2"),
                        MockMvcResultMatchers.jsonPath("$.login").value("test2"),
                        MockMvcResultMatchers.jsonPath("$.password").value("123456")
                );
    }

    @Test
    @Order(4)
    @DisplayName("Should get 204 when delete a password")
    void shouldGet204StatusCodeWhenDeleteAPassword() throws Exception {
        String token = login("mateusvnlima", "123456");
        URI uri = URI.create("http://localhost:8080/api/v1/password/2");

        mockMvc.perform(
                MockMvcRequestBuilders.delete(uri)
                        .header("Authorization", "Bearer " + token)
        )
               .andExpectAll(
                        MockMvcResultMatchers.status().isNoContent()
                );
    }
}