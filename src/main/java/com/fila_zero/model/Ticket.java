package com.fila_zero.model;

import com.fila_zero.model.enums.PriorityType;
import com.fila_zero.model.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "senhas")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_sequencial", nullable = false)
    private int sequenceNumber;

    @Column(name = "senha_formatada", nullable = false, length = 20)
    private String formattedTicket;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_prioridade", nullable = false, length = 30)
    private PriorityType priorityType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private TicketStatus status;

    @Column(name = "data_hora_emissao", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "data_hora_chamada")
    private LocalDateTime calledAt;

    @Column(name = "data_hora_conclusao")
    private LocalDateTime finishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_servico_id", nullable = false)
    private ServiceType serviceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guiche_id")
    private ServiceDesk serviceDesk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atendente_id")
    private User attendant;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = TicketStatus.AGUARDANDO;
    }
}