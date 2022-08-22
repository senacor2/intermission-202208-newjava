package com.senacor.intermission.newjava.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void createCustomer() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{ " +
                    "\"prename\": \"Foo\", " +
                    "\"lastname\": \"Bar\", " +
                    "\"dateOfBirth\": \"1970-01-01\"" +
                    "}"))
            .andExpect(status().isCreated());
    }

    @Test
    public void deleteCustomer() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/customer/" + UUID.randomUUID())
                .characterEncoding("UTF-8"))
            .andExpect(status().isOk());
    }
}
