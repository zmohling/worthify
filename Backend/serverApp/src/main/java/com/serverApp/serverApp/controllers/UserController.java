package com.serverApp.serverApp.controllers;
import com.serverApp.serverApp.other.hashingFunction;
import com.serverApp.serverApp.models.User;
import com.serverApp.serverApp.controllers.AccountsController;
import com.serverApp.serverApp.repositories.AccountsRepository;
import com.serverApp.serverApp.repositories.ArticleRepository;
import com.serverApp.serverApp.repositories.UserRepository;
import com.serverApp.serverApp.websocket.EchoServer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Rest Controller for the User table <br>
 * <br>
 * ENDPOINT /register for registering a user (see register()) <br>
 * ENDPOINT /passwordChange for changing a password (see passwordChange()) <br>
 * ENDPOINT /emailChange for changing an email (see emailChange()) <br>
 * ENDPOINT /login for logging in (see login()) <br>
 * ENDPOINT /getInfo/{auth} for getting the info of a user (see getAdminInfo())<br>
 * ENDPOINT /users/listAll for listing all users (see listAll()) <br>
 * ENDPOINT /users/numOnline for getting all of the online users (see numOnline()) <br>
 * ENDPOINT /users/delete/{userId} for deleting a user <br>
 *
 * @author Griffin Stout and Michael Davis
 *
 */
@RestController
public class UserController{


    EchoServer echoServer = new EchoServer(0000, false);

    /**
     * @Autowired repository to UserRepository
     */
    @Autowired
    UserRepository userRepo;

    /**
     * @Autowired repository to AccountsRepository
     */
    @Autowired
    AccountsRepository accountsRepo;

    /**
     * register a user
     * @param user user to register
     * @return json formatted user
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
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
            return ResponseEntity.ok().body(rString);
        } else {
            System.out.println("Registration Failed!");
            return ResponseEntity.ok().body("");
        }
    }

    /**
     * change a user's password
     * @param string user
     * @param header verification
     * @return json formatted user
     * @throws IOException IOException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    @RequestMapping("/passwordChange")
    public ResponseEntity<String> passwordChange(@RequestBody String string,
                                                 @RequestHeader(value = "Authorization") Optional<String> header) throws IOException, NoSuchAlgorithmException {
        int exists = -1;
        long ID = -1;
        if(header.isPresent()) {
            exists = userRepo.checkUserExists(header.get());
            if(exists == 0) {
                System.out.println("Unauthorized, invalid key");

                return ResponseEntity.ok().body("{\"error\":\"true\","
                        + "\"message\":\"invalid authentication key\"}");
            } else {
                ID = userRepo.getUserID(header.get());
                System.out.println(ID + " matches the authentication key");
            }
        } else {
            System.out.println("Unauthorized, no key");
            return ResponseEntity.ok().body("{\"error\":\"true\","
                    + "\"message\":\"no authentication key\"}");
        }
        JSONObject obj = new JSONObject(string);
        String changedPassword = obj.getString("changedPassword");
        String email = obj.getString("email");
        String password = obj.getString("password");
        User retrievedUser = userRepo.getUser(email);
        if(retrievedUser == null) {
            System.out.println("User does not exist");
            return ResponseEntity.ok().body("");
        }
        String hashedPassword = hashingFunction.hashingFunction(password, retrievedUser.getSalt());
        if(retrievedUser.getPassword().equals(hashedPassword)) {
            byte[] salt = hashingFunction.getSalt();
            retrievedUser.setSalt(salt);
            retrievedUser.setPassword(hashingFunction.hashingFunction(changedPassword, salt));
            userRepo.changePassword(retrievedUser.getSalt(), retrievedUser.getPassword(), retrievedUser.getEmail());
            System.out.println("Changing password for User: " + retrievedUser.getId());
            String rString =
                    "{\"error\":\"false\","
                            + "\"message\":\"user password change success\","
                            + "\"user\":{"
                            + "\"id\":\"" + retrievedUser.getId() + "\"," +
                            "\"lastName\":\"" + retrievedUser.getLastName() + "\"," +
                            "\"firstName\":\"" + retrievedUser.getFirstName() + "\"," +
                            "\"email\":\"" + retrievedUser.getEmail() + "\"," +
                            "\"authorization\":\"" + retrievedUser.getPassword() + "\"," +
                            "\"type\":\"" + retrievedUser.getType() + "\"}}";
            System.out.println("Password change successful for User: " + retrievedUser.getId());
            return ResponseEntity.ok().body(rString);
        }
        return ResponseEntity.ok().body("{\"error\":\"true\","
                + "\"message\":\"incorrect password\"}");
    }

    /**
     * change a user's email
     * @param string user
     * @param header verification
     * @return json formatted user
     * @throws IOException IOException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    @RequestMapping("/emailChange")
    public ResponseEntity<String> emailChange(@RequestBody String string,
                                                 @RequestHeader(value = "Authorization") Optional<String> header) throws IOException, NoSuchAlgorithmException {
        int exists = -1;
        long ID = -1;
        if(header.isPresent()) {
            exists = userRepo.checkUserExists(header.get());
            if(exists == 0) {
                System.out.println("Unauthorized, invalid key");

                return ResponseEntity.ok().body("{\"error\":\"true\","
                        + "\"message\":\"invalid authentication key\"}");
            } else {
                ID = userRepo.getUserID(header.get());
                System.out.println(ID + " matches the authentication key");
            }
        } else {
            System.out.println("Unauthorized, no key");
            return ResponseEntity.ok().body("{\"error\":\"true\","
                    + "\"message\":\"no authentication key\"}");
        }
        JSONObject obj = new JSONObject(string);
        String changedEmail = obj.getString("changedEmail");
        String email = obj.getString("email");
        String password = obj.getString("password");
        User retrievedUser = userRepo.getUser(email);
        if(retrievedUser == null) {
            System.out.println("User does not exist");
            return ResponseEntity.ok().body("");
        }
        String hashedPassword = hashingFunction.hashingFunction(password, retrievedUser.getSalt());
        if(retrievedUser.getPassword().equals(hashedPassword)) {
            if(userRepo.checkEmail(changedEmail) != 0) {
                return ResponseEntity.ok().body("{\"error\":\"true\","
                        + "\"message\":\"email already taken\"}");
            }
            userRepo.changeEmail(changedEmail, email);
            User changedUser = userRepo.getUser(changedEmail);
            System.out.println("Changing email for User: " + changedUser.getId());
            String rString =
                    "{\"error\":\"false\","
                            + "\"message\":\"user email change success\","
                            + "\"user\":{"
                            + "\"id\":\"" + changedUser.getId() + "\"," +
                            "\"lastName\":\"" + changedUser.getLastName() + "\"," +
                            "\"firstName\":\"" + changedUser.getFirstName() + "\"," +
                            "\"email\":\"" + changedEmail + "\"," +
                            "\"authorization\":\"" + changedUser.getPassword() + "\"," +
                            "\"type\":\"" + changedUser.getType() + "\"}}";
            System.out.println("Email change successful for User: " + changedUser.getId());
            return ResponseEntity.ok().body(rString);
        }
        return ResponseEntity.ok().body("{\"error\":\"true\","
                + "\"message\":\"incorrect password\"}");
    }

    /**
     * log into the app
     * @param user user to log in
     * @return a json formatted user
     */
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

    /**
     * administrators can
     * @param auth authorization
     * @return the users info
     */
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

    /**
     * lists all the users
     * @return alist of all the users
     */
    @GetMapping("users/listAll")
    public String listAll(){
        User[] userList = userRepo.listAll();
        String rString = "{";
        rString += "\"numUsers\":\"" + userList.length + "\", \"users\": [";
        for(int i = 0; i < userList.length; i ++){
            if (i != userList.length - 1) {
                rString =
                    rString +
                        "{"
                        + "\"id\":\""
                        + userList[i].getId()
                            + "\","
                        + "\"email\":\""
                        + userList[i].getEmail()
                        + "\","
                        + "\"firstName\":\""
                        + userList[i].getFirstName()
                        + "\","
                        + "\"lastName\":\""
                        + userList[i].getLastName()
                        + "\","
                        + "\"numAccounts\":\""
                        + accountsRepo.getAccountsById(userList[i].getId()).length
                        + "\"},";
            }else{
                rString =
                        rString
                                + "{"
                                + "\"id\":\""
                                + userList[i].getId()
                                + "\","
                                + "\"email\":\""
                                + userList[i].getEmail()
                                + "\","
                                + "\"firstName\":\""
                                + userList[i].getFirstName()
                                + "\","
                                + "\"lastName\":\""
                                + userList[i].getLastName()
                                + "\","
                                + "\"numAccounts\":\""
                                + accountsRepo.getAccountsById(userList[i].getId()).length
                                + "\"}]}";
            }
        }
        return rString;
    }

    /**
     * a json formatted number of people online
     * @return the number of people online
     */
    @GetMapping("/users/numOnline")
    public String getNumOnline(){
        String rString = "{\"num\":\"" + echoServer.getNumOnlineUsers() +"\"}";
        return rString;
    }

    /**
     * delete a user (only administrators)
     * @param userId id of the admin
     * @param header verification
     * @return error/success message
     */
    @DeleteMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable int userId, @RequestHeader(value = "Authorization") Optional<String> header) {
        //System.out.println(userId);
        int exists = -1;
        long id = -1;
        if(header.isPresent()) {
            exists = userRepo.checkUserExists(header.get());
            if(exists == 0) {
                System.out.println("Unauthorized, invalid key");
                return "{\"error\":\"true\","
                        + "\"message\":\"invalid authentication key\"}";
            } else {
                id = userRepo.getUserID(header.get());
                System.out.println(id + " matches the authentication key");
            }
        } else {
            System.out.println("Unauthorized, no key");
            return "{\"error\":\"true\","
                    + "\"message\":\"no authentication key\"}";
        }

        try{
            if(userRepo.deleteUser(userId) == 0){
                return "{\"error\":\"true\","
                        + "\"message\":\"delete failed\"}";
            }else{
                accountsRepo.deleteAllTiedToUser(userId);
            }
        }catch(Exception e){
            return "{\"error\":\"true\","
                    + "\"message\":\"delete failed\"}";
        }

        return "{\"error\":\"false\","
                + "\"message\":\"deleted user\"}";
    }

}
