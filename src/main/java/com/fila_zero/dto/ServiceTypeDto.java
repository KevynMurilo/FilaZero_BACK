package com.fila_zero.dto;

import lombok.Data;

@Data
public class ServiceTypeDto {
    private Long id;
    private String name;
    private String ticketPrefix;
    private Long servicePointId;
    private boolean active;
}