package com.fila_zero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServiceDeskRequest {
    @NotBlank
    private String identifier;
    @NotNull
    private Long servicePointId;
}