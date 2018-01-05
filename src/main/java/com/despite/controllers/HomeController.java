package com.despite.controllers;

import com.despite.entities.Role;
import com.despite.entities.User;
import com.despite.repository.UserRepository;
import com.despite.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class HomeController {

    @Autowired
    private IUserService userService;

    @GetMapping(value = "/")
    public String home() {
        return "Hello!";
    }

    @GetMapping(value = "/addtestuser")
    public User addTestUser() {
        return userService.save(new User("user", "user", Arrays.asList(new Role("ROLE_USER"))));
    }

}
