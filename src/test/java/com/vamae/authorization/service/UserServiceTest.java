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

    private final static User USER = User.builder()
            .id("id")
            .username("testUsername")
            .password("testPassword")
            .balance(1000)
            .build();

    private final static String ID = "id";
    private static final String INVALID_ID = "invalid_id";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void changeBalance() {
        int offset = 100;

        when(userRepository.findUserById(ID)).thenReturn(Optional.of(USER));

        int result = userService.changeBalance(ID, offset);

        assertEquals(1100, result);
        verify(userRepository, times(1)).save(USER);
    }

    @Test
    public void findUserById_Positive() {
        when(userRepository.findUserById(ID)).thenReturn(Optional.of(USER));

        User foundUser = userService.findUserById(ID);

        assertEquals(USER, foundUser);
        verify(userRepository, times(1)).findUserById(ID);
    }

    @Test
    public void findUserById_Negative() {
        when(userRepository.findUserById(INVALID_ID)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.findUserById(INVALID_ID));
        verify(userRepository, times(1)).findUserById(INVALID_ID);
    }
}