package com.fila_zero.repository;

import com.fila_zero.model.DeskSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeskSessionRepository extends JpaRepository<DeskSession, Long> {

    Optional<DeskSession> findByUserIdAndActiveTrue(Long userId);
}