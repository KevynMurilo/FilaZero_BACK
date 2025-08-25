package com.fila_zero.dto;

import com.fila_zero.model.enums.Role;
import lombok.Data;
import java.util.Set;

@Data
public class UserUpdateRequest {
    private String fullName;
    private Set<Role> roles;
    private Boolean enabled;
}