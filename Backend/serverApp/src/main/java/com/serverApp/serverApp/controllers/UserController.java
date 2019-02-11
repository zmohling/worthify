package com.serverApp.serverApp.controllers;

import com.serverApp.serverApp.models.User;
import com.serverApp.serverApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController{

    @Autowired
    UserRepository userRepo;

    @RequestMapping("/login")
    public String login(@RequestBody User user){
    // System.out.println("Login: Id: " + user.getId() + ": " + user.getFirstName() + " " +
    // user.getLastName());userRepo.save(user);

        //userRepo.save(user);
        String rString =
        "\"error\":\"false\","
            + "\"message\":\"user login success\","
            +  "\"user\":{"
            + "\"id\":\"" + user.getId() + "\"," +
                "\"username\":\"" +  user.getUserName() + "\"," +
                "\"email\":\"" + user.getEmail() + "\"}";

        return rString;
    }
}
