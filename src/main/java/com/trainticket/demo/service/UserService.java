package com.trainticket.demo.service;

import com.trainticket.demo.entities.Ticket;
import com.trainticket.demo.entities.User;
import com.trainticket.demo.exceptions.EmailAlreadyExistsException;
import com.trainticket.demo.exceptions.UserNotFoundException;
import com.trainticket.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketService ticketService;

    public User saveUser(User user){
        try{
            return userRepository.save(user);
        }catch (Exception e){
            throw new EmailAlreadyExistsException("User with the given email address already made a purchase: "+user.getEmail());
        }
    }

    public User getUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        }catch (Exception e) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
    }

    public List<User> getUsersBySection(String section) {
        return ticketService.getTicketsBySection(section).stream()
                .map(Ticket::getUser)
                .collect(Collectors.toList());
    }

    public void deleteUser(String email) {
        User user = getUserByEmail(email);
        Ticket ticket = ticketService.getTicketByUser(user);
        ticketService.deleteTicket(ticket);
        userRepository.delete(user);
    }

    public User modifyUser(User updatedUser) {
        User user = getUserByEmail(updatedUser.getEmail());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        return userRepository.save(user);
    }
}
