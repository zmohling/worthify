package com.serverApp.serverApp.controllers;
import com.serverApp.serverApp.other.hashingFunction;
import com.serverApp.serverApp.models.User;
import com.serverApp.serverApp.controllers.AccountsController;
import com.serverApp.serverApp.repositories.UserRepository;
import com.serverApp.serverApp.websocket.EchoServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
public class UserController{

    EchoServer server = new EchoServer(0000, false);

    @Autowired
    UserRepository userRepo;

    @RequestMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) throws NoSuchAlgorithmException {
    // System.out.println("Login: Id: " + user.getId() + ": " + user.getFirstName() + " " +
    // user.getLastName());userRepo.save(user);
        byte[] salt = hashingFunction.getSalt();
        user.setSalt(salt);
        user.setPassword(hashingFunction.hashingFunction(user.getPassword(), salt));
        System.out.println("registering new user...");
        if(userRepo.checkEmail(user.getEmail()) == 0) {
            user = userRepo.save(user);
            userRepo.flush();
            String rString =
                    "{\"error\":\"false\","
                            + "\"message\":\"user login success\","
                            + "\"user\":{"
                            + "\"id\":\"" + user.getId() + "\"," +
                            "\"lastName\":\"" + user.getLastName() + "\"," +
                            "\"firstName\":\"" + user.getFirstName() + "\"," +
                            "\"email\":\"" + user.getEmail() + "\"," +
                            "\"type\":\"" + user.getType() + "\"}}";
            System.out.println("Inserting user: " + rString);
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.set("Authorization", user.getPassword());
            return ResponseEntity.ok().headers(responseHeader).body(rString);
        } else {
            System.out.println("Registration Failed!");
            return ResponseEntity.ok().body("");
        }
    }

    @RequestMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
        User retrievedUser = userRepo.getUser(user.getEmail());
        if(retrievedUser == null) {
            System.out.println("User does not exist");
            return ResponseEntity.ok().body("");
        }
        String hashedPassword = hashingFunction.hashingFunction(user.getPassword(), retrievedUser.getSalt());
        if(retrievedUser.getPassword().equals(hashedPassword))
        {
            System.out.println("Password Successful!");
            String rString =
                    "{\"error\":\"false\","
                            + "\"message\":\"user login success\","
                            +  "\"user\":{"
                            + "\"id\":\"" + retrievedUser.getId() + "\"," +
                            "\"lastName\":\"" + retrievedUser.getLastName() + "\"," +
                            "\"firstName\":\"" + retrievedUser.getFirstName() + "\"," +
                            "\"email\":\"" + retrievedUser.getEmail() + "\"," +
                            "\"type\":\"" + retrievedUser.getType() + "\"}}";
            System.out.println("Login: " + retrievedUser.getEmail() + ", " + "\n" + retrievedUser.getPassword());
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.set("Authorization", retrievedUser.getPassword());
            return ResponseEntity.ok().headers(responseHeader).body(rString);
        }
        else {
            System.out.println("Password Unsuccessful!");
            return ResponseEntity.ok().body("");
        }
    }

    @RequestMapping("/adminlogin")
    public String adminLogin(@RequestBody User user){
        User retrievedUser = userRepo.getAdmin(user.getEmail(), user.getPassword());

        String rString =
                "{\"error\":\"false\","
                        + "\"message\":\"login success\","
                        +  "\"user\":{"
                        + "\"id\":\"" + retrievedUser.getId() + "\"," +
                        "\"lastName\":\"" + retrievedUser.getLastName() + "\"," +
                        "\"firstName\":\"" + retrievedUser.getFirstName() + "\"," +
                        "\"email\":\"" + retrievedUser.getEmail() + "\"," +
                        "\"type\":\"" + retrievedUser.getType() + "\"}}";
        System.out.println("Admin Login: " + retrievedUser.getEmail() + ", " + retrievedUser.getPassword());
        return rString;
    }

    @GetMapping("/users/numOnline")
    public String getNumOnline(){
        String rString = "{\"num\":\"" + server.getNumOnlineUsers() +"\"}";
        return rString;
    }
}
