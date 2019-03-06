package com.serverApp.serverApp.controllers;

import com.serverApp.serverApp.models.Accounts;
import com.serverApp.serverApp.models.LoanTransaction;
import com.serverApp.serverApp.models.User;
import com.serverApp.serverApp.repositories.AccountsRepository;
import com.serverApp.serverApp.repositories.UserRepository;
import com.serverApp.serverApp.repositories.LoanTransactionRepository;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import org.hibernate.annotations.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@RestController
public class AccountsController {
    @Autowired
    AccountsRepository accountsRepo;

    @RequestMapping("/getAccounts")
    public String getAccounts(@RequestBody User user) {
        Collection<Accounts> allAccounts = new ArrayList<Accounts>();
        allAccounts = accountsRepo.getAccounts(user.getId());
        Iterator<Accounts> iterator = allAccounts.iterator();
        String rString =
                "{\"error\":\"false\","
                        + "\"message\":\"account addition success\"";
        while(((Iterator) iterator).hasNext()) {
            rString = rString + ",\"id\":\"" + iterator.next().getId() + "\"," +
                    "\"type\":\"" + iterator.next().getType() + "\"," +
                    "\"label\":\"" + iterator.next().getLabel() + "\"," +
                    "\"userId\":\"" + iterator.next().getUserId() + "\"";
        }
        return "";
    }

    @RequestMapping("/addAccount")
    public String add(@RequestBody String string) {
        Accounts accounts = new Accounts();
        String string1 = "";
        add(accounts);
        /*accounts.setId(user.getId());
        accountsRepo.save(accounts);

        String rString =
                "{\"error\":\"false\","
                        + "\"message\":\"account addition success\","
                        + "\"account\":{"
                        + "\"id\":\"" + accounts.getId() + "\"," +
                        "\"type\":\"" + accounts.getType() + "\"," +
                        "   \"label\":\"" + accounts.getLabel() + "\"," +
                        "\"userId\":\"" + accounts.getUserId() + "\"}}";
        return rString;*/
        return "";
    }

    public String add(@RequestBody Accounts accounts){
        System.out.println(accounts.getType() + "yoyoy ");
        return "";
    }
    @RequestMapping("/remove")
    public String remove(@RequestBody Accounts accounts){
        accounts.setId(User.getSerialVersionUID());
        accountsRepo.delete(accounts);
        String rString =
                "{\"error\":\"false\","
                        + "\"message\":\"account removal success\","
                        + "\"account\":{"
                        + "\"id\":\"" + accounts.getId() + "\"," +
                        "\"type\":\"" + accounts.getType() + "\"," +
                        "\"label\":\"" + accounts.getLabel() + "\"," +
                        "\"userId\":\"" + accounts.getUserId() + "\"}}";
        return rString;
    }
}
