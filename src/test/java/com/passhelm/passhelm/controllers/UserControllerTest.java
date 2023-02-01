package com.passhelm.passhelm.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldGet200StatusCode() {
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
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldGet201StatusCode() {
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
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}