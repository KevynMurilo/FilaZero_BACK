package com.fila_zero.controller;

import com.fila_zero.dto.ServiceTypeDto;
import com.fila_zero.dto.ServiceTypeRequest;
import com.fila_zero.service.ServiceTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-types")
@Tag(name = "Tipos de Serviço", description = "Gerenciamento de tipos de serviço")
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    public ServiceTypeController(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @PostMapping
    @Operation(summary = "Cria um novo tipo de serviço")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<ServiceTypeDto> create(@Valid @RequestBody ServiceTypeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceTypeService.create(request));
    }

    @GetMapping("/by-service-point/{servicePointId}")
    @Operation(summary = "Lista todos os tipos de serviço de um ponto de atendimento")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ServiceTypeDto>> getAllByServicePoint(@PathVariable Long servicePointId) {
        return ResponseEntity.ok(serviceTypeService.getAllByServicePoint(servicePointId));
    }
}