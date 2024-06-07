package com.trainticket.demo.service;

import com.trainticket.demo.entities.User;
import com.trainticket.demo.exceptions.EmailAlreadyExistsException;
import com.trainticket.demo.repository.UserRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        easyRandom = new EasyRandom();
    }

    @Test
    void saveUser_EmailAlreadyExists_ThrowsException() {
        User user = easyRandom.nextObject(User.class);
        when(userRepository.save(user)).thenThrow(new RuntimeException());

        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> userService.saveUser(user));
    }

    @Test
    void getUserByEmail_ValidEmail_ReturnsUser() {
        User user = easyRandom.nextObject(User.class);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        User foundUser = userService.getUserByEmail(user.getEmail());

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.getFirstName(), foundUser.getFirstName());
        Assertions.assertEquals(user.getLastName(), foundUser.getLastName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());

        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }
}
