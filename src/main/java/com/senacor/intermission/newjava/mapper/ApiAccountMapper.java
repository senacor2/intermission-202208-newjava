package com.senacor.intermission.newjava.mapper;

import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Balance;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApiAccountMapper {

    @Mapping(target = "uuid", source = "account.uuid")
    @Mapping(target = "balanceInCents", source = "balance.valueInCents")
    ApiAccount toApiAccount(Account account, Balance balance);

}
