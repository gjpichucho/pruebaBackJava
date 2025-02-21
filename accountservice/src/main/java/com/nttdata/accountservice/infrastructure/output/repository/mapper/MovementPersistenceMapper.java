package com.nttdata.accountservice.infrastructure.output.repository.mapper;

import com.nttdata.accountservice.domain.Movement;
import com.nttdata.accountservice.infrastructure.output.repository.entity.MovementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MovementPersistenceMapper {

    @Mapping(target = "movementId", source = "id")
    @Mapping(target = "movementDate", source = "movementDate")
    Movement toMovement(MovementEntity movementEntity);

    @Mapping(target = "id", source = "movementId")
    @Mapping(target = "movementDate", expression = "java(java.time.LocalDateTime.now())")
    MovementEntity toMovementEntity(Movement movement);
}
