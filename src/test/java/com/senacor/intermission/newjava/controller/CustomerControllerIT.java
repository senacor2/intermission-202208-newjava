package com.senacor.intermission.newjava.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerControllerIT {

    @Autowired
    private MockMvc mvc;

    private String customerUuid;
    private String accountUuid;

    @Test
    @Order(1)
    public void createCustomer() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("""
                    {
                        "prename": "Foo",
                        "lastname": "Bar",
                        "dateOfBirth": "1970-01-01"
                    }"""))
            .andExpect(status().isCreated())
            .andExpect(content().json("""
                    {
                        "prename": "Foo",
                        "lastname": "Bar",
                        "dateOfBirth": "1970-01-01"
                    }"""))
            .andExpect(jsonPath("$.uuid").exists())
            .andReturn();
        customerUuid = JsonPath.read(result.getResponse().getContentAsString(), "$.uuid");
    }

    @Test
    @Order(2)
    public void createCustomer_invalidData() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("""
                    {
                        "prename": "t",
                        "lastname": "s"
                    }"""))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    public void createAccount() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/customers/" + customerUuid
                    + "/accounts")
                .characterEncoding("UTF-8"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.accountNumber").exists())
            .andExpect(jsonPath("$.iban").exists())
            .andExpect(jsonPath("$.uuid").exists())
            .andExpect(jsonPath("$.balanceInCents").value("0"))
            .andReturn();
        accountUuid = JsonPath.read(result.getResponse().getContentAsString(), "$.uuid");
    }

    @Test
    @Order(4)
    public void getAccounts() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/customers/" + customerUuid
                    + "/accounts")
                .characterEncoding("UTF-8"))
            .andExpect(status().isOk())
            .andExpect(content().json("[" + accountUuid + "]"));
    }

    @Test
    @Order(5)
    public void deleteCustomer() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/customers/" + customerUuid)
                .characterEncoding("UTF-8"))
            .andExpect(status().isNoContent());
    }
}
