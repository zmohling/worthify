package com.serverApp.serverApp.controllers;


import com.serverApp.serverApp.models.Ping;
import com.serverApp.serverApp.models.PingResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Controller
public class WebSocketController {

    @MessageMapping(value = "/ping")
    @SendTo("/topic/echo")
    public PingResponse echo(Ping ping) {
        System.out.println("Ping received");
        return new PingResponse(ping.getId());
    }



}
