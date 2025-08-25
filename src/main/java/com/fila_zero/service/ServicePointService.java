package com.fila_zero.service;

import com.fila_zero.dto.ServicePointDto;
import com.fila_zero.dto.ServicePointRequest;
import com.fila_zero.exception.ResourceNotFoundException;
import com.fila_zero.model.ServicePoint;
import com.fila_zero.repository.ServicePointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicePointService {

    private final ServicePointRepository servicePointRepository;

    public ServicePointService(ServicePointRepository servicePointRepository) {
        this.servicePointRepository = servicePointRepository;
    }

    @Transactional
    public ServicePointDto create(ServicePointRequest request) {
        ServicePoint servicePoint = new ServicePoint();
        servicePoint.setName(request.getName());
        servicePoint.setLocation(request.getLocation());
        servicePoint.setActive(request.isActive());
        return mapToDto(servicePointRepository.save(servicePoint));
    }

    @Transactional(readOnly = true)
    public List<ServicePointDto> getAll() {
        return servicePointRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServicePointDto getById(Long id) {
        return servicePointRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de Atendimento não encontrado com id: " + id));
    }

    @Transactional
    public ServicePointDto update(Long id, ServicePointRequest request) {
        ServicePoint servicePoint = servicePointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de Atendimento não encontrado com id: " + id));

        servicePoint.setName(request.getName());
        servicePoint.setLocation(request.getLocation());
        servicePoint.setActive(request.isActive());

        return mapToDto(servicePointRepository.save(servicePoint));
    }

    @Transactional
    public void delete(Long id) {
        if (!servicePointRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ponto de Atendimento não encontrado com id: " + id);
        }
        servicePointRepository.deleteById(id);
    }

    private ServicePointDto mapToDto(ServicePoint servicePoint) {
        ServicePointDto dto = new ServicePointDto();
        dto.setId(servicePoint.getId());
        dto.setName(servicePoint.getName());
        dto.setLocation(servicePoint.getLocation());
        dto.setActive(servicePoint.isActive());
        return dto;
    }
}