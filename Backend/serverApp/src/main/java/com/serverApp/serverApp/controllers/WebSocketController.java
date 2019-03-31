package com.serverApp.serverApp.controllers;


import com.serverApp.serverApp.models.Ping;
import com.serverApp.serverApp.models.PingResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/ping")
    @SendTo("/topic/echo")
    public PingResponse echo(Ping ping) throws Exception {
        System.out.println("Ping received");
        return new PingResponse(ping.getId());
    }



}
