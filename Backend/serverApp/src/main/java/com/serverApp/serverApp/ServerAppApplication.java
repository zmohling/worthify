package com.serverApp.serverApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableScheduling
@EnableWebSocket
@EnableWebSocketMessageBroker
public class ServerAppApplication{

	public static void main(String[] args) { SpringApplication.run(ServerAppApplication.class, args); }

}

