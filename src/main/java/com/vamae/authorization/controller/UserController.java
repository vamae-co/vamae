package com.vamae.authorization.controller;

import com.vamae.authorization.model.User;
import com.vamae.authorization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public User createAmsAccount(@RequestBody User user) throws Exception {
        return userService.createAccount(user);
    }
}
