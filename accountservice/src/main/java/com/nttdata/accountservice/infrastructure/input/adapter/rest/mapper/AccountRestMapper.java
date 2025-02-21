package com.nttdata.accountservice.infrastructure.input.adapter.rest.mapper;

import com.nttdata.accountservice.domain.Account;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.AccountRequest;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.AccountResponse;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.AccountUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AccountRestMapper {

    void updateAccount(@MappingTarget Account account, Account accountUpdate);

    AccountResponse toAccountresponse(Account account);

    @Mapping(target = "accountId", ignore = true)
    Account toAccount(AccountRequest accountRequest);

    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "accountType", source = "accountType")
    @Mapping(target = "initialBalance", source = "initialBalance")
    @Mapping(target = "status", source = "status")
    Account toAccount(AccountUpdate accountUpdate);

    AccountResponse toPostAccountResponse(Account account);

    AccountResponse toPutAccountResponse(Account account);


}
