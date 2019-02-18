package com.serverApp.serverApp.controllers;

import com.serverApp.serverApp.models.Accounts;
import com.serverApp.serverApp.models.User;
import com.serverApp.serverApp.repositories.AccountsRepository;
import com.serverApp.serverApp.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

    @Autowired
    AccountsRepository accountsRepo;

    @RequestMapping("/add")
    public String add(@RequestBody Accounts accounts) {
        //is this right?
        accounts.setId(User.getSerialVersionUID());
        accountsRepo.save(accounts);
        return "";
    }

    @RequestMapping("/remove")
    public String remove(@RequestBody Accounts accounts){
        accounts.setId(User.getSerialVersionUID());
        accountsRepo.delete(accounts);
        return "";
    }
}
