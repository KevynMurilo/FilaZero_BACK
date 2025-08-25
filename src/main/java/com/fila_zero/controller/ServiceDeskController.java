package com.fila_zero.controller;

import com.fila_zero.dto.ServiceDeskDto;
import com.fila_zero.dto.ServiceDeskRequest;
import com.fila_zero.service.ServiceDeskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-desks")
@Tag(name = "Guichês de Atendimento", description = "Gerenciamento de guichês")
@PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
public class ServiceDeskController {

    private final ServiceDeskService serviceDeskService;

    public ServiceDeskController(ServiceDeskService serviceDeskService) {
        this.serviceDeskService = serviceDeskService;
    }

    @PostMapping
    public ResponseEntity<ServiceDeskDto> create(@Valid @RequestBody ServiceDeskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceDeskService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ServiceDeskDto>> getAll() {
        return ResponseEntity.ok(serviceDeskService.getAll());
    }
}