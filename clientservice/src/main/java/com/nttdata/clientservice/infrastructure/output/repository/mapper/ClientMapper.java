package com.nttdata.clientservice.infrastructure.output.repository.mapper;

import com.nttdata.clientservice.domain.Client;
import com.nttdata.clientservice.infrastructure.output.repository.entity.ClientEntity;
import com.nttdata.clientservice.infrastructure.output.repository.entity.PersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClientMapper {

    @Mapping(target = "clientId", source = "clientEntity.idClient")
    @Mapping(target = "personId", source = "person.idPerson")
    @Mapping(target = "identification", source = "person.identification")
    @Mapping(target = "name", source = "person.name")
    @Mapping(target = "gender", source = "person.gender")
    @Mapping(target = "age", source = "person.age")
    @Mapping(target = "address", source = "person.address")
    @Mapping(target = "phone", source = "person.phone")
    @Mapping(target = "status", source = "clientEntity.status", qualifiedByName = "mapStatus")
    Client toClient(ClientEntity clientEntity, PersonEntity person);

    @Named("mapStatus")
    default String mapStatus(Boolean value) {
        return value ? "Activo" : "Inactivo";
    }

}
