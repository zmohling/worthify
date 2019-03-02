package com.serverApp.serverApp.controllers;

import com.serverApp.serverApp.models.Accounts;
import com.serverApp.serverApp.models.User;
import com.serverApp.serverApp.repositories.AccountsRepository;
import com.serverApp.serverApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

    @Autowired
    AccountsRepository accountsRepo;

    @RequestMapping("/getAccounts")
    public String getAccounts(@RequestBody User user) {
        accountsRepo.getAccounts(user.getId());

        return "";
    }

    @RequestMapping("/add")
    public String add(@RequestBody User user, @RequestBody Accounts accounts) {

        accounts.setId(user.getId());
        accountsRepo.save(accounts);
        String rString =
                "{\"error\":\"false\","
                        + "\"message\":\"account addition success\","
                        + "\"user\":{"
                        + "\"id\":\"" + accounts.getId() + "\"," +
                        "\"type\":\"" + accounts.getType() + "\"," +
                        "\"label\":\"" + accounts.getLabel() + "\"," +
                        "\"userId\":\"" + accounts.getUserId() + "\"}}";
        return rString;
    }

    @RequestMapping("/remove")
    public String remove(@RequestBody Accounts accounts){
        accounts.setId(User.getSerialVersionUID());
        accountsRepo.delete(accounts);
        return "";
    }
}
