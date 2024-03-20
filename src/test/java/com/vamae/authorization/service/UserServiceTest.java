package com.vamae.authorization.service;

import com.vamae.authorization.model.User;
import com.vamae.authorization.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final static String USERNAME = "testUsername";
    private static final String INVALID_USERNAME = "invalid_username";

    private final static User USER = User.builder()
            .username(USERNAME)
            .password("testPassword")
            .balance(1000)
            .build();

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void changeBalance() {
        int offset = 100;

        when(userRepository.findUserByUsername(USERNAME)).thenReturn(Optional.of(USER));

        int result = userService.changeBalance(USERNAME, offset);

        assertEquals(1100, result);
        verify(userRepository, times(1)).save(USER);
    }

    @Test
    public void findUserById_Positive() {
        when(userRepository.findUserByUsername(USERNAME)).thenReturn(Optional.of(USER));

        User foundUser = userService.findUserByUsername(USERNAME);

        assertEquals(USER, foundUser);
        verify(userRepository, times(1)).findUserByUsername(USERNAME);
    }

    @Test
    public void findUserById_Negative() {
        when(userRepository.findUserByUsername(INVALID_USERNAME)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.findUserByUsername(INVALID_USERNAME));
        verify(userRepository, times(1)).findUserByUsername(INVALID_USERNAME);
    }
}