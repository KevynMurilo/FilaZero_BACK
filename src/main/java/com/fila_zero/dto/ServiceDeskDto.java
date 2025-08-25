package com.fila_zero.dto;

import lombok.Data;

@Data
public class ServiceDeskDto {
    private Long id;
    private String identifier;
    private Long servicePointId;
}