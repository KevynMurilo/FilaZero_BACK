package com.fila_zero.dto;

import lombok.Data;

@Data
public class ServicePointDto {
    private Long id;
    private String name;
    private String location;
    private boolean active;
}