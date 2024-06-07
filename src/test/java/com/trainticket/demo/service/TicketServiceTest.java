package com.trainticket.demo.service;

import com.trainticket.demo.entities.Ticket;
import com.trainticket.demo.entities.User;
import com.trainticket.demo.exceptions.TicketNotFoundException;
import com.trainticket.demo.exceptions.TrainFullException;
import com.trainticket.demo.repository.TicketRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        easyRandom = new EasyRandom();
    }

    private List<Ticket> generateTickets(int count) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Ticket ticket = easyRandom.nextObject(Ticket.class);
            ticket.setSection("A");
            ticket.setSeatNumber(i);
            tickets.add(ticket);
        }
        return tickets;
    }

    @Test
    void saveTicket_Success() {
        Ticket ticket = easyRandom.nextObject(Ticket.class);

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket savedTicket = ticketService.saveTicket(ticket);

        Assertions.assertEquals(ticket.getFrom(), savedTicket.getFrom());
        Assertions.assertEquals(ticket.getTo(), savedTicket.getTo());
        Assertions.assertEquals(ticket.getUser(), savedTicket.getUser());
        Assertions.assertEquals(ticket.getPrice(), savedTicket.getPrice());
        Assertions.assertNotNull(savedTicket.getSection());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void saveTicket_TrainFull() {
        Ticket ticket = easyRandom.nextObject(Ticket.class);
        when(ticketRepository.findBySection(anyString())).thenReturn(generateTickets(30));

        Assertions.assertThrows(TrainFullException.class, () -> ticketService.saveTicket(ticket));
    }

    @Test
    void getTicketByUser_ValidUser_ReturnsTicket() {
        User user = easyRandom.nextObject(User.class);
        Ticket ticket = easyRandom.nextObject(Ticket.class);
        when(ticketRepository.findByUser(user)).thenReturn(ticket);

        Ticket foundTicket = ticketService.getTicketByUser(user);

        Assertions.assertNotNull(foundTicket);
        Assertions.assertEquals(ticket.getFrom(), foundTicket.getFrom());
        Assertions.assertEquals(ticket.getTo(), foundTicket.getTo());
        Assertions.assertEquals(ticket.getUser(), foundTicket.getUser());
        Assertions.assertEquals(ticket.getPrice(), foundTicket.getPrice());
        Assertions.assertEquals(ticket.getSection(), foundTicket.getSection());
        Assertions.assertEquals(ticket.getSeatNumber(), foundTicket.getSeatNumber());

        verify(ticketRepository, times(1)).findByUser(user);
    }

    @Test
    void getTicketByUser_InvalidUser_ThrowsException() {
        User user = easyRandom.nextObject(User.class);
        when(ticketRepository.findByUser(user)).thenThrow(new RuntimeException());

        Assertions.assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketByUser(user));
    }

    @Test
    void getTicketsBySection_ValidSection_ReturnsTickets() {
        List<Ticket> tickets = generateTickets(5);
        when(ticketRepository.findBySection("A")).thenReturn(tickets);

        List<Ticket> foundTickets = ticketService.getTicketsBySection("A");

        Assertions.assertEquals(5, foundTickets.size());
        verify(ticketRepository, times(1)).findBySection("A");
    }

    @Test
    void getTicketBySectionAndSeatNumber_ValidSectionAndSeatNumber_ReturnsTicket() {
        Ticket ticket = easyRandom.nextObject(Ticket.class);
        when(ticketRepository.findBySectionAndSeatNumber("A", 1)).thenReturn(ticket);

        Ticket foundTicket = ticketService.getTicketBySectionAndSeatNumber("A", 1);

        Assertions.assertNotNull(foundTicket);
        Assertions.assertEquals(ticket.getFrom(), foundTicket.getFrom());
        Assertions.assertEquals(ticket.getTo(), foundTicket.getTo());
        Assertions.assertEquals(ticket.getUser(), foundTicket.getUser());
        Assertions.assertEquals(ticket.getPrice(), foundTicket.getPrice());
        Assertions.assertEquals(ticket.getSection(), foundTicket.getSection());
        Assertions.assertEquals(ticket.getSeatNumber(), foundTicket.getSeatNumber());

        verify(ticketRepository, times(1)).findBySectionAndSeatNumber("A", 1);
    }

    @Test
    void deleteTicket_ValidTicket_Success() {
        Ticket ticket = easyRandom.nextObject(Ticket.class);

        ticketService.deleteTicket(ticket);

        verify(ticketRepository, times(1)).delete(ticket);
    }
}
