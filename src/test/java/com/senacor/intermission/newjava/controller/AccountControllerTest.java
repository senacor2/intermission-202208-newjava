package com.senacor.intermission.newjava.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void createAccount() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/customer/" + UUID.randomUUID() + "/account")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
            .andExpect(status().isCreated())
            .andExpect(content().json("{ \"iban\": null }"));
    }

    @Test
    public void getAccount() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/customer/" + UUID.randomUUID() + "/account/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void getAllAccounts() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/customer/" + UUID.randomUUID() + "/account")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }
}
