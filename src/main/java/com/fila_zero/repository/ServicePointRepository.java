package com.fila_zero.repository;

import com.fila_zero.model.ServicePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicePointRepository extends JpaRepository<ServicePoint, Long> {

    List<ServicePoint> findAllByActiveTrue();
}