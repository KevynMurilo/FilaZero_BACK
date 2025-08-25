package com.fila_zero.repository;

import com.fila_zero.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    List<ServiceType> findByServicePointId(Long servicePointId);
    List<ServiceType> findByServicePointIdAndActiveTrue(Long servicePointId);
}