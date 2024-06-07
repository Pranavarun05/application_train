package com.trainticket.demo.service;

import com.trainticket.demo.entities.Ticket;
import com.trainticket.demo.entities.User;
import com.trainticket.demo.exceptions.TicketNotFoundException;
import com.trainticket.demo.exceptions.TrainFullException;
import com.trainticket.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket saveTicket(Ticket ticket) {
        String section = allocateSection();
        int seatNumber = allocateSeatNumber(section);

        if (seatNumber == 0) {
            throw new TrainFullException("Train is full");
        }

        ticket.setSection(section);
        ticket.setSeatNumber(seatNumber);

        return ticketRepository.save(ticket);
    }

    public Ticket getTicketByUser(User user) {
        try {
            return ticketRepository.findByUser(user);
        }catch (Exception e){
            throw new TicketNotFoundException("Ticket not found for the user :" + user.getEmail());
        }
    }

    public List<Ticket> getTicketsBySection(String section) {
        return ticketRepository.findBySection(section);
    }

    public Ticket getTicketBySectionAndSeatNumber(String section, int seatNumber) {
        return ticketRepository.findBySectionAndSeatNumber(section, seatNumber);
    }

    public void deleteTicket(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    private String allocateSection() {
        long countA = getTicketsBySection("A").size();

        if (countA < 30) {
            return "A";
        } else {
            return "B";
        }
    }

    private int allocateSeatNumber(String section) {
        List<Ticket> tickets = getTicketsBySection(section);

        for (int i = 1; i <= 30; i++) {
            int finalI = i;
            if (tickets.stream().noneMatch(ticket -> ticket.getSeatNumber() == finalI)) {
                return i;
            }
        }

        return 0;
    }
}
