package com.passhelm.passhelm.controllers;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
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

    @Test
    @Order(1)
    void shouldGet200StatusCodeWhenGetAllUsers() {
        URI uri = URI.create("http://localhost:8080/api/v1/user");

        try {
            mockMvc
            .perform(MockMvcRequestBuilders
                    .get(uri)
                    .contentType("application/json")
            )
            .andExpect(MockMvcResultMatchers
                    .status()
                    .is(200)
            )
            .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    void shouldGet200StatusCodeAndUserDataWhenCreateUser() {
        URI uri = URI.create("http://localhost:8080/api/v1/user");

        String json = "{\n" +
                "    \"name\": \"Tom Brady\",\n" +
                "    \"email\": \"tom_brady@gmail.com\",\n" +
                "    \"username\": \"tom_brady\",\n" +
                "    \"password\": \"!A34567a9123\"\n" +
                "}";

        try {
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .post(uri)
                            .contentType("application/json")
                            .content(json)
                    )
                    .andExpect(MockMvcResultMatchers
                            .status()
                            .is(200)
                    )
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.name").value("Tom Brady")
                    )
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.email").value("tom_brady@gmail.com")
                    )
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.username").value("tom_brady")
                    )
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.password").value("!A34567a9123")
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(3)
    void shouldGet200AndUpdateUser() throws Exception {
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
        ).andExpectAll(
                MockMvcResultMatchers.status().is(200),
                MockMvcResultMatchers.jsonPath("$.name").value("Mac Jones"),
                MockMvcResultMatchers.jsonPath("$.email").value("mac_jones@gmail.com"),
                MockMvcResultMatchers.jsonPath("$.username").value("mac_jones")
        );
    }

    @Test
    @Order(4)
    void shouldGet200AndDeleteUser() throws Exception {
        URI uri = URI.create("http://localhost:8080/api/v1/user/3");

        mockMvc.perform(MockMvcRequestBuilders
                .delete(uri)
        ).andExpectAll(
                MockMvcResultMatchers.status().is(200)
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

}