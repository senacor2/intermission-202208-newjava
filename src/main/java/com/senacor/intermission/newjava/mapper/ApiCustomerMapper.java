package com.senacor.intermission.newjava.mapper;

import com.senacor.intermission.newjava.model.BaseCustomer;
import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.model.PremiumCustomer;
import com.senacor.intermission.newjava.model.api.ApiCreateCustomer;
import com.senacor.intermission.newjava.model.api.ApiCustomer;
import com.senacor.intermission.newjava.model.api.ApiCustomerType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApiCustomerMapper {

    @Mapping(target = "type", source = "type")
    ApiCustomer toApiCustomer(Customer customer, ApiCustomerType type);

    BaseCustomer toOwnBaseCustomer(ApiCreateCustomer apiCreateCustomer);

    PremiumCustomer toOwnPremiumCustomer(ApiCreateCustomer apiCreateCustomer);
}
