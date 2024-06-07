package com.trainticket.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "departure")
    private String from;
    @Column(name = "destination")
    private String to;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private double price;
    @Column(name = "section")
    private String section;
    @Column(name = "seat_number")
    private int seatNumber;
}
