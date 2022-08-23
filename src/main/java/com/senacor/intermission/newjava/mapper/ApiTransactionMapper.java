package com.senacor.intermission.newjava.mapper;

import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.model.api.ApiCreateTransaction;
import com.senacor.intermission.newjava.model.api.ApiTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ApiTransactionMapper {

    @Mappings({
        @Mapping(target = "senderIban", source = "sender.iban"),
        @Mapping(target = "receiverIban", source = "receiver.iban"),
        @Mapping(target = "amountInCents", source = "valueInCents")
    })
    ApiTransaction toApiTransaction(Transaction transaction);

    @Mappings({
        @Mapping(target = "valueInCents", source = "request.amountInCents"),
        @Mapping(target = "status", constant = "PENDING"),
        @Mapping(target = "transactionTime", source = "request.transactionDate"),
        @Mapping(target = "description", source = "request.description"),
        @Mapping(target = "sender", source = "sender"),
        @Mapping(target = "receiver", source = "receiver")
    })
    Transaction createTransaction(ApiCreateTransaction request, Account sender, Account receiver);

}
