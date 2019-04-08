package com.serverApp.serverApp.controllers;
import com.serverApp.serverApp.other.hashingFunction;
import com.serverApp.serverApp.models.User;
import com.serverApp.serverApp.controllers.AccountsController;
import com.serverApp.serverApp.repositories.UserRepository;
import com.serverApp.serverApp.websocket.EchoServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
public class UserController{

    EchoServer echoServer = new EchoServer(0000, false);

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
            user.setType(0);
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
                            "\"authorization\":\"" + user.getPassword() + "\"," +
                            "\"type\":\"" + user.getType() + "\"}}";
            System.out.println("Inserting user: " + rString);
            HttpHeaders responseHeader = new HttpHeaders();
            return ResponseEntity.ok().body(rString);
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
                            "\"authorization\":\"" + retrievedUser.getPassword() + "\"," +
                            "\"type\":\"" + retrievedUser.getType() + "\"}}";
            System.out.println("Login: " + retrievedUser.getEmail() + ", " + "\n" + retrievedUser.getPassword());
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.set("Authorization", retrievedUser.getPassword());
            return ResponseEntity.ok().body(rString);
        }
        else {
            System.out.println("Password Unsuccessful!");
            return ResponseEntity.ok().body("");
        }
    }

    @RequestMapping("/getInfo/{auth}")
    public String getAdminInfo(@PathVariable String auth){
        User admin = userRepo.getAdmin(auth);
        if (admin != null) {
              String rString =
                "{ \"id\":\""
              + admin.getId()
              + "\","
              + "\"lastName\":\""
              + admin.getLastName()
              + "\","
              + "\"firstName\":\""
              + admin.getFirstName()
              + "\","
              + "\"email\":\""
              + admin.getEmail()
              + "\","
              + "\"authorization\":\""
              + admin.getPassword()
              + "\","
              + "\"type\":\""
              + admin.getType()
              + "\",\"error\": \"auth success\"}";
            return rString;
        }else{
            return "{ \"error\": \"auth failed\"}";
        }

    }


    @GetMapping("/users/numOnline")
    public String getNumOnline(){
        String rString = "{\"num\":\"" + echoServer.getNumOnlineUsers() +"\"}";
        return rString;
    }
}
