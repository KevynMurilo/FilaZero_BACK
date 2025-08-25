package com.fila_zero.repository;

import com.fila_zero.model.ServiceDesk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceDeskRepository extends JpaRepository<ServiceDesk, Long> {
}