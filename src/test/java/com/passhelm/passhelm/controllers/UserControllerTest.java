package com.passhelm.passhelm.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.net.URI;
import java.nio.file.AccessDeniedException;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @BeforeAll
    void setUp() {
        URI uri = URI.create("http://localhost:8080/api/v1/user");

        String json = "{\n" +
                "    \"name\": \"User Test\",\n" +
                "    \"email\": \"user_test@test.com\",\n" +
                "    \"username\": \"user_test\",\n" +
                "    \"password\": \"123456\"\n" +
                "}";

        try {
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .post(uri)
                            .contentType("application/json")
                            .content(json)
                    )
                    .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

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
    @DisplayName("Should return 200 when get all users")
    void shouldGet200StatusCodeWhenGetAllUsers() throws Exception {
        String token = this.login("mateusvnlima", "123456");
        URI uri = URI.create("http://localhost:8080/api/v1/user");

        mockMvc
        .perform(MockMvcRequestBuilders
                .get(uri)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
        )
        .andExpect(MockMvcResultMatchers
                .status()
                .isOk()
        )
        .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    @Test
    @Order(2)
    @DisplayName("Should return 200 and user data when create a new user")
    void shouldGet200StatusCodeAndUserDataWhenCreateUser() throws Exception {
        URI uri = URI.create("http://localhost:8080/api/v1/user");

        String json = "{\n" +
                "    \"name\": \"Tom Brady\",\n" +
                "    \"email\": \"tom_brady@gmail.com\",\n" +
                "    \"username\": \"tom_brady\",\n" +
                "    \"password\": \"!A34567a9123\"\n" +
                "}";

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
                        .jsonPath("$.name").value("Tom Brady")
                )
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.email").value("tom_brady@gmail.com")
                )
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.username").value("tom_brady")
                );
    }

    @Test
    @Order(3)
    @DisplayName("Should return 200 and user data when update a user")
    void shouldGet200AndUpdateUser() throws Exception {
        String token = login("user_test", "123456");
        URI uri = URI.create("http://localhost:8080/api/v1/user/3");

        String json = "{\n" +
                "    \"name\": \"Mac Jones\",\n" +
                "    \"email\": \"mac_jones@gmail.com\",\n" +
                "    \"username\": \"mac_jones\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders
                .put(uri)
                .contentType("application/json")
                .content(json)
                .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.name").value("Mac Jones"),
                MockMvcResultMatchers.jsonPath("$.email").value("mac_jones@gmail.com"),
                MockMvcResultMatchers.jsonPath("$.username").value("mac_jones")
        );
    }

    @Test
    @Order(4)
    @DisplayName("Should return 204 when delete a user")
    void shouldGet204AndDeleteUser() throws Exception {
        String token = login("mateusvnlima", "123456");
        URI uri = URI.create("http://localhost:8080/api/v1/user/3");

        mockMvc.perform(MockMvcRequestBuilders
                .delete(uri)
                .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test()
    @Order(5)
    void shouldThrowsIllegalStateExceptionWhenCreateUserWithUsernameAlreadyInUse() throws Exception {
        URI uri = URI.create("http://localhost:8080/api/v1/user");

        String json = "{\n" +
                "    \"name\": \"Mateus Vinicius\",\n" +
                "    \"email\": \"mateusvnlima@gmail.com\",\n" +
                "    \"username\": \"tom_brady\",\n" +
                "    \"password\": \"!A34567a9123\"\n" +
                "}";

        try {

            MvcResult result = mockMvc
                    .perform(MockMvcRequestBuilders
                            .post(uri)
                            .contentType("application/json")
                            .content(json)
                    )
                    .andReturn();

        } catch(Exception e) {
            Assertions.assertEquals( "Request processing failed: java.lang.IllegalStateException: Username taken", e.getMessage());
        }
    }

    @Test()
    @Order(6)
    void shouldThrowsIllegalStateExceptionWhenCreateUserWithEmailAlreadyInUse() throws Exception {
        URI uri = URI.create("http://localhost:8080/api/v1/user");

        String json = "{\n" +
                "    \"name\": \"Mateus Vinicius\",\n" +
                "    \"email\": \"mateusvnlima@gmail.com\",\n" +
                "    \"username\": \"mateus_vinicius\",\n" +
                "    \"password\": \"!A34567a9123\"\n" +
                "}";

        try {

            MvcResult result = mockMvc
                    .perform(MockMvcRequestBuilders
                            .post(uri)
                            .contentType("application/json")
                            .content(json)
                    )
                    .andReturn();

        } catch(Exception e) {
            Assertions.assertEquals( "Request processing failed: java.lang.IllegalStateException: Email taken",
                    e.getMessage());
        }


    }

    @Test
    @Order(7)
    void shouldThrowsIllegalStateExceptionWhenTryToDeleteAnInexistentUser() throws Exception {
        URI uri = URI.create("http://localhost:8080/api/v1/100");

        try {

            MvcResult result = mockMvc
                    .perform(MockMvcRequestBuilders
                            .delete(uri)
                    )
                    .andReturn();

        } catch(Exception e) {
            Assertions.assertEquals( "Request processing failed: java.lang.IllegalStateException: Email taken",
                    e.getMessage());
        }
    }

    @Test
    @Order(8)
    @DisplayName("Should return 200 when get an user")
    void shouldGet200StatusCodeWhenGetUser() throws Exception {
        String token = login("mateusvnlima", "123456");
        URI uri = URI.create("http://localhost:8080/api/v1/user/1");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.name").value("Mateus Vinicius")
                );

    }

}