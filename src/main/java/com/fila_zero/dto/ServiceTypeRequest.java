package com.fila_zero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ServiceTypeRequest {
    @NotBlank
    @Size(max = 150)
    private String name;

    @NotBlank
    @Size(min = 1, max = 3)
    private String ticketPrefix;

    @NotNull
    private Long servicePointId;

    private boolean active = true;
}