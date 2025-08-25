package com.fila_zero.controller;

import com.fila_zero.dto.TicketCreateRequest;
import com.fila_zero.dto.TicketDto;
import com.fila_zero.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Senhas", description = "Operações de geração e atendimento de senhas")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/generate")
    @Operation(summary = "Gera uma nova senha de atendimento (Público)")
    public ResponseEntity<TicketDto> generateTicket(@Valid @RequestBody TicketCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.generateTicket(request));
    }

    @PostMapping("/call-next")
    @Operation(summary = "Chama a próxima senha da fila", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ATENDENTE', 'GESTOR', 'ADMIN')")
    public ResponseEntity<TicketDto> callNextTicket(@RequestParam Long attendantId, @RequestParam Long serviceDeskId) {
        return ResponseEntity.ok(ticketService.callNextTicket(attendantId, serviceDeskId));
    }

    @PostMapping("/{ticketId}/finish")
    @Operation(summary = "Finaliza o atendimento de uma senha", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ATENDENTE', 'GESTOR', 'ADMIN')")
    public ResponseEntity<TicketDto> finishTicket(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.finishTicket(ticketId));
    }

    @GetMapping("/queue/waiting")
    @Operation(summary = "Retorna a fila de senhas aguardando (Para painéis)")
    public ResponseEntity<List<TicketDto>> getWaitingQueue() {
        return ResponseEntity.ok(ticketService.getWaitingQueue());
    }

    @PostMapping("/{ticketId}/cancel")
    @Operation(summary = "Cancela uma senha (não comparecimento)", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ATENDENTE', 'GESTOR', 'ADMIN')")
    public ResponseEntity<TicketDto> cancelTicket(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.cancelTicket(ticketId));
    }
}