package com.senacor.intermission.newjava.controller;

import com.jayway.jsonpath.JsonPath;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerControllerIT {

    @Autowired
    private WebTestClient client;

    private String customerUuid;
    private String accountUuid;

    @Test
    @Order(1)
    public void createCustomer() throws Exception {
        byte[] body = client
            .post()
                .uri("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("""
                    {
                      "prename": "Foo",
                      "lastname": "Bar",
                      "dateOfBirth": "1970-01-01"
                    }""")
            .exchange()
                .expectStatus().isCreated()
                .expectBody()
                    .json("""
                        {
                            "prename": "Foo",
                            "lastname": "Bar",
                            "dateOfBirth": "1970-01-01"
                        }""")
                    .jsonPath("$.uuid").exists()
            .returnResult().getResponseBody();
        customerUuid = JsonPath.read(new String(body, StandardCharsets.UTF_8), "$.uuid");
    }

    @Test
    @Order(2)
    public void createCustomer_invalidData() throws Exception {
        client
            .post()
                .uri("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("""
                    {
                        "prename": "t",
                        "lastname": "s"
                    }""")
            .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    public void createAccount() throws Exception {
        byte[] body = client
            .post()
                .uri("/customers/{uuid}/accounts", customerUuid)
            .exchange()
                .expectStatus().isCreated()
                .expectBody()
                    .jsonPath("$.accountNumber").exists()
                    .jsonPath("$.iban").exists()
                    .jsonPath("$.uuid").exists()
                    .jsonPath("$.balanceInCents").isEqualTo("0")
            .returnResult().getResponseBody();
        accountUuid = JsonPath.read(new String(body, StandardCharsets.UTF_8), "$.uuid");
    }

    @Test
    @Order(4)
    public void getAccounts() throws Exception {
        client
            .get()
                .uri("/customers/{uuid}/accounts", customerUuid)
            .exchange()
                .expectStatus().isOk()
                .expectBody().json("[" + accountUuid + "]");
    }

    @Test
    @Order(5)
    public void deleteCustomer() throws Exception {
        client
            .delete()
                .uri("/customers/{uuid}", customerUuid)
            .exchange()
                .expectStatus().isNoContent();
    }
}
