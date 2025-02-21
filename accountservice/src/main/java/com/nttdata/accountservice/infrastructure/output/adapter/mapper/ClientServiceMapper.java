package com.nttdata.accountservice.infrastructure.output.adapter.mapper;

import com.nttdata.accountservice.domain.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ClientServiceMapper {

    @Mapping(target = "clientId", source = "id")
    @Mapping(target = "clientName", source = "name")
    Client toClient(com.nttdata.accountservice.infrastructure.output.client.model.Client client);

}
