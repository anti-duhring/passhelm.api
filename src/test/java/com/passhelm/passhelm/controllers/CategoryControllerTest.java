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
class CategoryControllerTest {

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
    @DisplayName("Should get 200 when get all categories from itself")
    void shouldGet200StatusCodeWhenGetAllCategories() throws Exception {
        String token = login("mateusvnlima", "123456");
        URI uri = URI.create("http://localhost:8080/api/v1/category?userId=1");

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(uri)
                        .header("Authorization", "Bearer " + token)
        )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @Order(1)
    @DisplayName("Should get 403 when get all categories from another user")
    void shouldGet403StatusCodeWhenGetAllCategoriesFromAnotherUser() throws Exception {
        String token = login("tombrady", "123456");
        URI uri = URI.create("http://localhost:8080/api/v1/category?userId=1");

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(uri)
                                .header("Authorization", "Bearer " + token)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isForbidden()
                );
    }

    @Test
    @Order(2)
    void shouldGet200StatusCodeAndCategoryDataWhenCreateCategory() throws Exception{
        String token = login("mateusvnlima", "123456");
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
                                .header("Authorization", "Bearer " + token)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
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

    @Test
    @Order(6)
    void shouldGet200StatusAndCategoryDataWhenUpdateCategory() throws Exception{
        String token = login("mateusvnlima", "123456");
        URI uri = URI.create("http://localhost:8080/api/v1/category/2");

        String json = "{\n" +
                "    \"label\": \"Rede Social\",\n" +
                "    \"color\": \"#FFFFFF\"\n" +
                "}";

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                             .put(uri)
                             .contentType("application/json")
                            .content(json)
                                .header("Authorization", "Bearer " + token)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().is(200),
                        MockMvcResultMatchers.jsonPath("$.label").value("Rede Social"),
                        MockMvcResultMatchers.jsonPath("$.color").value("#FFFFFF")

                );
    }

    @Test
    @Order(7)
    void shouldGet200StatusWhenDeleteCategory() throws Exception{
        String token = login("mateusvnlima", "123456");
        URI uri = URI.create("http://localhost:8080/api/v1/category/2");

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                              .delete(uri)
                                .header("Authorization", "Bearer " + token)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isNoContent()
                );
    }
}