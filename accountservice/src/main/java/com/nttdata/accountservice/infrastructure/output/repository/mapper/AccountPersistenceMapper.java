package com.nttdata.accountservice.infrastructure.output.repository.mapper;

import com.nttdata.accountservice.domain.Account;
import com.nttdata.accountservice.infrastructure.output.repository.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AccountPersistenceMapper {

    @Mapping(target = "accountId", source = "id")
    Account toAccount(AccountEntity accountEntity);

    @Mapping(target = "id", source = "accountId")
    AccountEntity toAccountEntity(Account account);
}
