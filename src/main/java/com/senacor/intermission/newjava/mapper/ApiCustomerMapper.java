package com.senacor.intermission.newjava.mapper;

import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.model.api.ApiCustomer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApiCustomerMapper {

    ApiCustomer toApiCustomer(Customer customer);

    Customer toOwnCustomer(ApiCustomer apiCustomer);

}
