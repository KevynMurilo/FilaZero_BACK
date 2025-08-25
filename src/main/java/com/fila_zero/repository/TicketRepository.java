package com.fila_zero.repository;

import com.fila_zero.model.ServiceType;
import com.fila_zero.model.Ticket;
import com.fila_zero.model.enums.PriorityType;
import com.fila_zero.model.enums.TicketStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatusOrderByPriorityTypeAscCreatedAtAsc(TicketStatus status);

    @Query("SELECT t FROM Ticket t WHERE t.status = 'AGUARDANDO' ORDER BY t.priorityType DESC, t.createdAt ASC LIMIT 1")
    Optional<Ticket> findNextTicketToCall();

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.serviceType.id = :serviceTypeId AND t.createdAt >= :startOfDay")
    int countByServiceTypeAndDate(Long serviceTypeId, LocalDate startOfDay);

    List<Ticket> findByStatusOrderByPriorityTypeDescCreatedAtAsc(TicketStatus status);

    List<Ticket> findAllByStatusInAndCalledAtIsNotNull(List<TicketStatus> statuses, Pageable pageable);
}