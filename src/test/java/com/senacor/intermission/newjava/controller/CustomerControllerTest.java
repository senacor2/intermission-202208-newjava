package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CustomerRepository customerRepository;

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
        Customer customer = Customer.builder().build();
        customerRepository.save(customer);

        mvc.perform(MockMvcRequestBuilders.delete("/customer/" + customer.getUuid())
                .characterEncoding("UTF-8"))
            .andExpect(status().isOk());
    }
}
