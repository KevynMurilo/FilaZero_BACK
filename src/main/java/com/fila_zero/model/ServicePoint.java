package com.fila_zero.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pontos_atendimento")
public class ServicePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String name;

    @Column(name = "localizacao", length = 200)
    private String location;

    @Column(name = "ativo", nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "servicePoint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceType> serviceTypes;
}