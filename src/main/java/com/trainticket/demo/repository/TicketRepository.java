package com.trainticket.demo.repository;

import com.trainticket.demo.entities.Ticket;
import com.trainticket.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findBySection(String section);
    Ticket findBySectionAndSeatNumber(String section, int seatNumber);

    Ticket findByUser(User user);
}
