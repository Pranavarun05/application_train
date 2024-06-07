package com.trainticket.demo.exceptions;

public class TrainFullException extends RuntimeException{
    public TrainFullException(String message) {
        super(message);
    }
}
