package com.serverApp.serverApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class ServerAppApplication{

	public static void main(String[] args) throws NoSuchAlgorithmException {

		//SpringApplication.run(ServerAppApplication.class, args);
		byte[] salt = hashingFunction.getSalt();
	}

}

