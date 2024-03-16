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

    public int changeBalance(String userId, int offset) {
        User user = findUserById(userId);
        user.setBalance(user.getBalance() + offset);
        userRepository.save(user);

        return user.getBalance();
    }

    public User findUserById(String id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found!"));
    }
}
