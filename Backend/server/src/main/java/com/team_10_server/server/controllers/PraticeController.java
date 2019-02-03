package com.team_10_server.server.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
public class PraticeController {

    @GetMapping("/practiceGet")
    public void get(){
        System.out.println("GET msg received");
    }

    @PostMapping("/practicePOST")
    public void post(){
        System.out.println("POST msg received");
    }

    @PutMapping("/practicePUT")
    public void put(){
        System.out.println("PUT msg received");
    }

    @DeleteMapping("/practiceDELETE")
    public void delete(){
        System.out.println("DELET msg received");
    }
}
