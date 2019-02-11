package com.serverApp.serverApp.controllers;

import com.serverApp.serverApp.models.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController{
    @RequestMapping("/login")
    public String login(@RequestBody User user){
        System.out.println("Received user object");
        return "";
    }
}
