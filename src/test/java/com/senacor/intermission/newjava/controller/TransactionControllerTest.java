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
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void createTransaction() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/customer/" + UUID.randomUUID()
                    + "/account/" + UUID.randomUUID()
                    + "/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{ " +
                    "\"valueInCents\": 4999, " +
                    "\"description\": \"Almost 50 Euros\"" +
                    // TODO Address sender and receiver by IBAN
//                    "\"sender\": \"DE0120000000100000000001\"," +
//                    "\"receiver\": \"DE0120000000100000000001\"" +
                    "}"))
            .andExpect(status().isCreated());
    }

    @Test
    public void getAllTransactions() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/customer/" + UUID.randomUUID()
                    + "/account/" + UUID.randomUUID()
                    + "/transaction")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }
}
