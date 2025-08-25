package com.fila_zero.service;

import com.fila_zero.dto.ServiceTypeDto;
import com.fila_zero.dto.ServiceTypeRequest;
import com.fila_zero.exception.ResourceNotFoundException;
import com.fila_zero.model.ServicePoint;
import com.fila_zero.model.ServiceType;
import com.fila_zero.repository.ServicePointRepository;
import com.fila_zero.repository.ServiceTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;
    private final ServicePointRepository servicePointRepository;

    public ServiceTypeService(ServiceTypeRepository serviceTypeRepository, ServicePointRepository servicePointRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.servicePointRepository = servicePointRepository;
    }

    @Transactional
    public ServiceTypeDto create(ServiceTypeRequest request) {
        ServicePoint servicePoint = servicePointRepository.findById(request.getServicePointId())
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de Atendimento não encontrado com id: " + request.getServicePointId()));

        ServiceType serviceType = new ServiceType();
        serviceType.setName(request.getName());
        serviceType.setTicketPrefix(request.getTicketPrefix().toUpperCase());
        serviceType.setServicePoint(servicePoint);
        serviceType.setActive(request.isActive());

        return mapToDto(serviceTypeRepository.save(serviceType));
    }

    @Transactional(readOnly = true)
    public List<ServiceTypeDto> getAllByServicePoint(Long servicePointId) {
        return serviceTypeRepository.findByServicePointId(servicePointId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ServiceTypeDto mapToDto(ServiceType serviceType) {
        ServiceTypeDto dto = new ServiceTypeDto();
        dto.setId(serviceType.getId());
        dto.setName(serviceType.getName());
        dto.setTicketPrefix(serviceType.getTicketPrefix());
        dto.setServicePointId(serviceType.getServicePoint().getId());
        dto.setActive(serviceType.isActive());
        return dto;
    }
}