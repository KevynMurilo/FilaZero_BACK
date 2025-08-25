package com.fila_zero.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipos_servico")
public class ServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String name;

    @Column(name = "sigla_senha", nullable = false, length = 3)
    private String ticketPrefix;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_atendimento_id", nullable = false)
    private ServicePoint servicePoint;

    @Column(name = "ativo", nullable = false)
    private boolean active = true;
}