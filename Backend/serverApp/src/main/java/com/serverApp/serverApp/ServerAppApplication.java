package com.serverApp.serverApp;

import com.serverApp.serverApp.websocket.EchoServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServerAppApplication{


	public static void main(String[] args) {
		SpringApplication.run(ServerAppApplication.class, args);


		new EchoServer(4444, true);
	}

}

