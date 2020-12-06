package com.server.endamti.controller;

import com.server.endamti.model.User;
import com.server.endamti.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    private boolean register(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PostMapping("/login")
    private boolean login(@RequestBody User user) {
        return userService.isValid(user);
    }
}

