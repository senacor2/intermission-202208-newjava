package com.senacor.intermission.newjava.mapper;

import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.model.api.ApiCreateTransaction;
import com.senacor.intermission.newjava.model.api.ApiTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApiTransactionMapper {

    @Mapping(target = "senderIban", source = "sender.iban")
    @Mapping(target = "receiverIban", source = "receiver.iban")
    @Mapping(target = "amountInCents", source = "transaction.valueInCents")
    @Mapping(target = "transactionDate", source = "transaction.transactionTime")
    ApiTransaction toApiTransaction(Transaction transaction, Account sender, Account receiver);

    @Mapping(target = "valueInCents", source = "request.amountInCents")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "transactionTime", source = "request.transactionDate")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "receiverId", source = "receiver.id")
    Transaction createTransaction(ApiCreateTransaction request, Account sender, Account receiver);

}
