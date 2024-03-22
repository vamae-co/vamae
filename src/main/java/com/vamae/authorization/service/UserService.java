package com.vamae.authorization.service;

import com.vamae.authorization.model.User;
import com.vamae.authorization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public int changeBalance(String username, int offset) {
        User user = findUserByUsername(username);
        int userBalance = user.getBalance();
        if((offset < 0 && userBalance >= -offset) || offset > 0) {
            user.setBalance(user.getBalance() + offset);
        }
        else if(offset < 0) {
            throw new IllegalArgumentException("User have not enough money!");
        }

        userRepository.save(user);

        return user.getBalance();
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found!"));
    }
}
