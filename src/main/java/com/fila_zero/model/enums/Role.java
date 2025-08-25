package com.fila_zero.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ATENDENTE,
    ROLE_GESTOR,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}