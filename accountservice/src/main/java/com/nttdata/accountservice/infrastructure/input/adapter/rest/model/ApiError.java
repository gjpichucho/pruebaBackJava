package com.nttdata.accountservice.infrastructure.input.adapter.rest.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private String title;

    private String detail;

    private List<ErrorList> errors = new ArrayList<>();

    private String instance;

    private String type;

}
