package com.trainticket.demo.controller;

import com.trainticket.demo.entities.Ticket;
import com.trainticket.demo.entities.User;
import com.trainticket.demo.service.TicketService;
import com.trainticket.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TrainController {

    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;

    @PostMapping("/purchase")
    public Ticket purchaseTicket(@RequestBody Ticket ticket) {
        User savedUser = userService.saveUser(ticket.getUser());
        ticket.setUser(savedUser);
        return ticketService.saveTicket(ticket);
    }

    @GetMapping("/receipt/{email}")
    public Ticket getReceipt(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ticketService.getTicketByUser(user);
    }

    @GetMapping("/users/{section}")
    public List<User> getUsersBySection(@PathVariable String section) {
        return userService.getUsersBySection(section);
    }

    @DeleteMapping("/user/{email}")
    public void removeUser(@PathVariable String email) {
        userService.deleteUser(email);
    }

    @PutMapping("/user")
    public User modifyUser(@RequestBody User updatedUser) {
        return userService.modifyUser(updatedUser);
    }
}
