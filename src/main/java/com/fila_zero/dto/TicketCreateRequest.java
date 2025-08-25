package com.fila_zero.dto;

import com.fila_zero.model.enums.PriorityType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketCreateRequest {
    @NotNull
    private Long serviceTypeId;

    @NotNull
    private PriorityType priorityType;
}