package com.serverApp.serverApp.controllers;

import com.google.gson.reflect.TypeToken;
import com.serverApp.serverApp.models.Accounts;
import com.serverApp.serverApp.models.User;
import com.serverApp.serverApp.models.CertificateOfDeposit;
import com.serverApp.serverApp.repositories.AccountsRepository;
import com.serverApp.serverApp.repositories.CertificateOfDepositRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.*;

@RestController
public class AccountsController {
    @Autowired
    AccountsRepository accountsRepo;
    @Autowired
    CertificateOfDepositRepository certRepo;

    @RequestMapping("/getAccounts")
    public String getAccounts(@RequestBody User user) {
        Collection<Accounts> allAccounts = accountsRepo.getAccounts(user.getId());
        Iterator<Accounts> iterator = allAccounts.iterator();
        String rString =
                "{\"error\":\"false\","
                        + "\"message\":\"account retrieval success\"" +
                        ",\"accounts\":[";
        while((iterator).hasNext()) {
            Accounts accounts = iterator.next();
            rString = rString +
                    ",\"accountID\":\"" + accounts.getAccountId() + "\"," +
                    "\"label\":\"" + accounts.getLabel() + "\"," +
                    "\"transactions\":\"" + accounts.getTransactions() + "\"," +
                    "\"type\":\"" + accounts.getType() + "\"}";
            if(iterator.hasNext()) rString = rString + ",";
        }
        rString = rString + "]}";
        return rString;
    }

    @RequestMapping("/addAccount")
    public String addAccounts(@RequestBody String string) {
        JSONObject obj = new JSONObject(string);
        JSONArray accountsArr = obj.getJSONArray("accounts");
        Type accountType = new TypeToken<ArrayList<Accounts>>(){}.getType();
        Gson g = new Gson();
        ArrayList<String> transactionArr = new ArrayList<String>();
        for(int i = 0; i < accountsArr.length(); i++) {
            transactionArr.add(accountsArr.getJSONObject(i).getJSONObject("transactions").toString());
            accountsArr.getJSONObject(i).remove("transactions");
        }
        ArrayList<Accounts> accountsList = g.fromJson(accountsArr.toString(), accountType);
        for(int i = 0; i < accountsArr.length(); i++) {
            accountsList.get(i).setTransactions(transactionArr.get(i));
            accountsList.get(i).setIsActive(1);
            System.out.println(accountsList.get(i).getType());
            accountsRepo.save(accountsList.get(i));
        }
        String rString =
                "{\"error\":\"false\","
                        + "\"message\":\"account addition success\"" +
                        ",\"accounts\":[";
        Iterator<Accounts> iterator = accountsList.iterator();
        while((iterator).hasNext()) {
            Accounts accounts = iterator.next();
            rString = rString +
                    "{\"accountID\":\"" + accounts.getAccountId() + "\"," +
                    "\"label\":\"" + accounts.getLabel() + "\"," +
                    "\"transactions\":" + accounts.getTransactions() + "," +
                    "\"type\":\"" + accounts.getType() + "\"}";
            if(iterator.hasNext()) rString = rString + ",";
        }
        rString = rString + "]}";
        return rString;
    }
}
