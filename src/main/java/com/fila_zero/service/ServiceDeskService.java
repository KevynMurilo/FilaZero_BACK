package com.fila_zero.service;

import com.fila_zero.dto.ServiceDeskDto;
import com.fila_zero.dto.ServiceDeskRequest;
import com.fila_zero.exception.ResourceNotFoundException;
import com.fila_zero.model.ServiceDesk;
import com.fila_zero.model.ServicePoint;
import com.fila_zero.repository.ServiceDeskRepository;
import com.fila_zero.repository.ServicePointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceDeskService {

    private final ServiceDeskRepository serviceDeskRepository;
    private final ServicePointRepository servicePointRepository;

    public ServiceDeskService(ServiceDeskRepository s, ServicePointRepository p) {
        this.serviceDeskRepository = s;
        this.servicePointRepository = p;
    }

    @Transactional
    public ServiceDeskDto create(ServiceDeskRequest request) {
        ServicePoint sp = servicePointRepository.findById(request.getServicePointId())
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de Atendimento não encontrado"));
        ServiceDesk desk = new ServiceDesk();
        desk.setIdentifier(request.getIdentifier());
        desk.setServicePoint(sp);
        return mapToDto(serviceDeskRepository.save(desk));
    }

    @Transactional(readOnly = true)
    public List<ServiceDeskDto> getAll() {
        return serviceDeskRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private ServiceDeskDto mapToDto(ServiceDesk desk) {
        ServiceDeskDto dto = new ServiceDeskDto();
        dto.setId(desk.getId());
        dto.setIdentifier(desk.getIdentifier());
        dto.setServicePointId(desk.getServicePoint().getId());
        return dto;
    }
}