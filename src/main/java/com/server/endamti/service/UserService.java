package com.server.endamti.service;

import com.server.endamti.model.User;
import com.server.endamti.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    UserRepository userRepo;

    public boolean isValid(User user) {
        if (userRepo.existsByUsername(user.getUsername())) {
            User realUser = userRepo.findDistinctByUsername(user.getUsername());
            return realUser.getPassword().equals(user.getPassword());
        }
        return false;
    }

    public boolean addUser(User user) {
        if (userRepo.existsByUsername(user.getUsername())) {
            return false;
        } else {
            userRepo.save(user);
            return true;
        }
    }
}
