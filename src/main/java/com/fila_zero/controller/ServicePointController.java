package com.fila_zero.controller;

import com.fila_zero.dto.ServicePointDto;
import com.fila_zero.dto.ServicePointRequest;
import com.fila_zero.service.ServicePointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-points")
@Tag(name = "Pontos de Atendimento", description = "Gerenciamento de pontos de atendimento")
@PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
public class ServicePointController {

    private final ServicePointService servicePointService;

    public ServicePointController(ServicePointService servicePointService) {
        this.servicePointService = servicePointService;
    }

    @PostMapping
    @Operation(summary = "Cria um novo ponto de atendimento")
    public ResponseEntity<ServicePointDto> create(@Valid @RequestBody ServicePointRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicePointService.create(request));
    }

    @GetMapping
    @Operation(summary = "Lista todos os pontos de atendimento")
    public ResponseEntity<List<ServicePointDto>> getAll() {
        return ResponseEntity.ok(servicePointService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um ponto de atendimento por ID")
    public ResponseEntity<ServicePointDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(servicePointService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um ponto de atendimento")
    public ResponseEntity<ServicePointDto> update(@PathVariable Long id, @Valid @RequestBody ServicePointRequest request) {
        return ResponseEntity.ok(servicePointService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um ponto de atendimento")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicePointService.delete(id);
        return ResponseEntity.noContent().build();
    }
}