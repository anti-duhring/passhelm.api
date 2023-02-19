package com.passhelm.passhelm.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
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
class AdminControllerTest {

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

    private String createUser() throws Exception{
        URI uri = URI.create("http://localhost:8080/api/v1/user");

        String json = "{\n" +
                "    \"name\": \"Test User Role\",\n" +
                "    \"email\": \"testuserrole@gmail.com\",\n" +
                "    \"username\": \"test_user_role\",\n" +
                "    \"password\": \"123456\"\n" +
                "}";

        final String[] userId = {""};

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(uri)
                        .contentType("application/json")
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated()
                )
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.name").value("Test User Role")
                )
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.email").value("testuserrole@gmail.com")
                )
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.username").value("test_user_role")
                ).andDo(result -> userId[0] =
                        result.getResponse().getContentAsString().split("id\":")[1].split(",")[0]);

        return userId[0];
    }

    @Test
    @Order(1)
    @DisplayName("Should get 403 when try to change user roles without been admin")
    void shouldGet403WhenTryToChangeUserRolesWithoutBeenAdmin() throws Exception{
        String userId = createUser();
        String token = login("test_user_role", "123456");
        URI uri = URI.create("http://localhost:8080/api/v1/admin/update-user-role/" + userId);
        String json = "[\n" +
                "        \"ROLE_USER\",\n" +
                "        \"ROLE_ADMIN\"\n" +
                "]\n";

        mockMvc.perform(
                MockMvcRequestBuilders.put(uri)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isForbidden()
        );
    }

    @Test
    @Order(2)
    @DisplayName("Should get 200 when change user roles")
    void shouldGet200WhenChangeUserRoles() throws Exception{
        String token = login("mateusvnlima", "123456");
        URI uri = URI.create("http://localhost:8080/api/v1/admin/update-user-role/1");
        String json = "[\n" +
                "        \"ROLE_USER\",\n" +
                "        \"ROLE_ADMIN\"\n" +
                "]\n";

        mockMvc.perform(
                MockMvcRequestBuilders.put(uri)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(json)
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.username").value("mateusvnlima")
        );
    }

}