package com.fila_zero.dto;

import com.fila_zero.model.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String fullName;
    private String username;
    private Set<Role> roles;
    private boolean enabled;
}