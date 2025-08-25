package com.fila_zero.dto;

import com.fila_zero.model.enums.PriorityType;
import com.fila_zero.model.enums.TicketStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TicketDto {
    private Long id;
    private String formattedTicket;
    private PriorityType priorityType;
    private TicketStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime calledAt;
    private LocalDateTime finishedAt;
    private String serviceTypeName;
    private String serviceDeskIdentifier;
    private String attendantName;
}