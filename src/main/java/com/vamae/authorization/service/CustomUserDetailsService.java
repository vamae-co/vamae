package com.vamae.authorization.service;

import com.vamae.authorization.model.User;
import com.vamae.authorization.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsername(username);
            String userName = user.getUsername();
            String password = user.getPassword();
            return new org.springframework.security.core.userdetails.User(userName, password, new ArrayList<>());
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found: " + e);
        }
    }
}
