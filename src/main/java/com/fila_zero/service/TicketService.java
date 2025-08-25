package com.fila_zero.service;

import com.fila_zero.dto.TicketCreateRequest;
import com.fila_zero.dto.TicketDto;
import com.fila_zero.exception.ResourceNotFoundException;
import com.fila_zero.model.ServiceDesk;
import com.fila_zero.model.ServiceType;
import com.fila_zero.model.Ticket;
import com.fila_zero.model.User;
import com.fila_zero.model.enums.TicketStatus;
import com.fila_zero.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final UserRepository userRepository;
    private final ServiceDeskRepository serviceDeskRepository;

    public TicketService(TicketRepository ticketRepository, ServiceTypeRepository serviceTypeRepository, UserRepository userRepository, ServiceDeskRepository serviceDeskRepository) {
        this.ticketRepository = ticketRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.userRepository = userRepository;
        this.serviceDeskRepository = serviceDeskRepository;
    }

    @Transactional
    public TicketDto generateTicket(TicketCreateRequest request) {
        ServiceType serviceType = serviceTypeRepository.findById(request.getServiceTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Serviço não encontrado: " + request.getServiceTypeId()));

        int todayCount = ticketRepository.countByServiceTypeAndDate(serviceType.getId(), LocalDate.now());
        int sequenceNumber = todayCount + 1;

        String formattedTicket = String.format("%s%03d", serviceType.getTicketPrefix(), sequenceNumber);

        Ticket ticket = new Ticket();
        ticket.setServiceType(serviceType);
        ticket.setPriorityType(request.getPriorityType());
        ticket.setSequenceNumber(sequenceNumber);
        ticket.setFormattedTicket(formattedTicket);

        return mapToDto(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketDto callNextTicket(Long attendantId, Long serviceDeskId) {
        User attendant = userRepository.findById(attendantId)
                .orElseThrow(() -> new ResourceNotFoundException("Atendente não encontrado: " + attendantId));

        ServiceDesk serviceDesk = serviceDeskRepository.findById(serviceDeskId)
                .orElseThrow(() -> new ResourceNotFoundException("Guichê não encontrado: " + serviceDeskId));

        Ticket ticketToCall = ticketRepository.findNextTicketToCall()
                .orElseThrow(() -> new ResourceNotFoundException("Nenhuma senha aguardando para ser chamada."));

        ticketToCall.setStatus(TicketStatus.EM_ATENDIMENTO);
        ticketToCall.setCalledAt(LocalDateTime.now());
        ticketToCall.setAttendant(attendant);
        ticketToCall.setServiceDesk(serviceDesk);

        return mapToDto(ticketRepository.save(ticketToCall));
    }

    @Transactional
    public TicketDto finishTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Senha não encontrada: " + ticketId));

        if (ticket.getStatus() != TicketStatus.EM_ATENDIMENTO) {
            throw new IllegalStateException("A senha não está em atendimento e não pode ser finalizada.");
        }

        ticket.setStatus(TicketStatus.FINALIZADO);
        ticket.setFinishedAt(LocalDateTime.now());

        return mapToDto(ticketRepository.save(ticket));
    }

    @Transactional(readOnly = true)
    public List<TicketDto> getWaitingQueue() {
        return ticketRepository.findByStatusOrderByPriorityTypeAscCreatedAtAsc(TicketStatus.AGUARDANDO)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketDto cancelTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Senha não encontrada: " + ticketId));

        if (ticket.getStatus() == TicketStatus.FINALIZADO) {
            throw new IllegalStateException("Não é possível cancelar uma senha já concluída.");
        }

        ticket.setStatus(TicketStatus.CANCELADO);

        return mapToDto(ticketRepository.save(ticket));
    }

    private TicketDto mapToDto(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setFormattedTicket(ticket.getFormattedTicket());
        dto.setPriorityType(ticket.getPriorityType());
        dto.setStatus(ticket.getStatus());
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setCalledAt(ticket.getCalledAt());
        dto.setFinishedAt(ticket.getFinishedAt());

        if (ticket.getServiceType() != null) {
            dto.setServiceTypeName(ticket.getServiceType().getName());
        }
        if (ticket.getServiceDesk() != null) {
            dto.setServiceDeskIdentifier(ticket.getServiceDesk().getIdentifier());
        }
        if (ticket.getAttendant() != null) {
            dto.setAttendantName(ticket.getAttendant().getFullName());
        }
        return dto;
    }
}