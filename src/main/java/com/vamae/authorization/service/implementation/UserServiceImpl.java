package com.vamae.authorization.service.implementation;

import com.vamae.authorization.model.User;
import com.vamae.authorization.repository.UserRepository;
import com.vamae.authorization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public User createAccount(User account) {
        return userRepository.save(account);
    }
}
