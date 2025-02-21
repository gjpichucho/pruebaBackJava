package com.nttdata.clientservice.infrastructure.input.adapter.rest.mapper;

import com.nttdata.clientservice.domain.Client;
import com.nttdata.clientservice.infrastructure.input.adapter.rest.model.ClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClientRestMapper {

    @Mapping(target = "id", source = "identification")
    ClientResponse toClientResponse(Client client);
}
