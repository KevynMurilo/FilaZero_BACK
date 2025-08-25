package com.fila_zero.model;

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
@Table(name = "sessoes_guiche")
public class DeskSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guiche_id", nullable = false)
    private ServiceDesk serviceDesk;

    @Column(name = "data_hora_login", nullable = false)
    private LocalDateTime loginTime;

    @Column(name = "data_hora_logout")
    private LocalDateTime logoutTime;

    @Column(name = "ativo", nullable = false)
    private boolean active;

    @PrePersist
    protected void onCreate() {
        this.loginTime = LocalDateTime.now();
        this.active = true;
    }
}