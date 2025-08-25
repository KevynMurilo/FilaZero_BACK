package com.fila_zero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ServicePointRequest {
    @NotBlank
    @Size(max = 150)
    private String name;

    @Size(max = 200)
    private String location;

    private boolean active = true;
}