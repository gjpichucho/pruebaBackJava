package com.nttdata.accountservice.infrastructure.input.adapter.rest.mapper;

import com.nttdata.accountservice.domain.*;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.MovementRequest;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.MovementResponse;
import com.nttdata.accountservice.infrastructure.input.adapter.rest.model.MovementUpdate;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MovementRestMapper {

    void updateMovement(@MappingTarget Movement movement, Movement movementUpdate);

    @Mapping(target = "accountNumber", source = "accountId")
    @Mapping(target = "movementDate", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "movementId", ignore = true)
    Movement toMovement(MovementUpdate movementUpdate);

    @Mapping(target = "accountNumber", source = "accountId")
    @Mapping(target = "movementDate", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "movementId", ignore = true)
    Movement toMovement(MovementRequest movementUpdate);

    MovementResponse toPostMovementResponse(Movement movement);

    MovementResponse toMovementResponse(Movement movement);

    MovementResponse toPutMovementResponse(Movement movement);

    @Mapping(target = "movements", source = "movementList")
    MovementReport toMovementReport(Account account, List<Movement> movementList);

    @Mapping(target = "client", source = "client")
    @Mapping(target = "accountMovements", source = "movementReportList")
    MovementReportResponse toMovementReportResponse(Client client, List<MovementReport> movementReportList);


}
