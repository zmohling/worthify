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

import com.serverApp.serverApp.repositories.AccountsRepository;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.*;

@RestController
public class AccountsController {
    @Autowired
    AccountsRepository accountsRepo;
    @Autowired
    CertificateOfDepositRepository certRepo;

    @RequestMapping("/accounts/get/all")
    public String getAccounts(@RequestBody String string) {
        JSONObject obj = new JSONObject(string);
        long user = Long.parseLong(obj.get("id").toString());
        Collection<Accounts> allAccounts = accountsRepo.getAccounts(user);
        Iterator<Accounts> iterator = allAccounts.iterator();
        String rString =
                "{\"accounts\":[";
        while((iterator).hasNext()) {
            Accounts accounts = iterator.next();
            rString = rString +
                    "{\"accountID\":\"" + accounts.getAccountId() + "\"," +
                    "\"label\":\"" + accounts.getLabel() + "\"," +
                    "\"transactions\":" + accounts.getTransactions() + "," +
                    "\"type\":\"" + accounts.getType() + "\"";
            if(accounts.getType().equals("CertificateOfDeposit")) {
                rString = rString + ",\"maturityDate\":\"" + certRepo.getCertificateOfDeposite(accounts.getAccountId()).getMaturityDate() + "\"}";
            } else {
                rString = rString + "}";
            }
            if(iterator.hasNext()) rString = rString + ",";
        }
        rString = rString + "]}";
        return rString;
    }

    @RequestMapping("/accounts/add")
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
        //storing the accounts into the Accounts table
        for(int i = 0; i < accountsArr.length(); i++) {
            accountsList.get(i).setTransactions(transactionArr.get(i));
            accountsList.get(i).setIsActive(1);
            accountsRepo.save(accountsList.get(i));
            //when the account type is CertificateOfDeposit
            if(accountsList.get(i).getType().equals("CertificateOfDeposit")) {
                Date maturityDate = Date.valueOf(accountsArr.getJSONObject(i).getString("maturityDate"));
                CertificateOfDeposit certificateOfDeposit = new CertificateOfDeposit();
                certificateOfDeposit.setMaturityDate(maturityDate);
                certificateOfDeposit.setAccountsId(accountsList.get(i).getAccountId());
                certRepo.save(certificateOfDeposit);
            } else if(accountsList.get(i).getType().equals("RealEstate")) {
                String address = accountsArr.getJSONObject(i).getString("address");
                String city = accountsArr.getJSONObject(i).getString("city");
                String state = accountsArr.getJSONObject(i).getString("state");
            }
        }
        String rString =
                "{\"accounts\":[";
        Iterator<Accounts> iterator = accountsList.iterator();
        while((iterator).hasNext()) {
            Accounts accounts = iterator.next();
            rString = rString +
                    "{\"accountID\":\"" + accounts.getAccountId() + "\"," +
                    "\"label\":\"" + accounts.getLabel() + "\"," +
                    "\"transactions\":" + accounts.getTransactions() + "," +
                    "\"type\":\"" + accounts.getType() + "\"";
            if(accounts.getType().equals("CertificateOfDeposit")) {
                rString = rString + ",\"maturityDate\":\"" + certRepo.getCertificateOfDeposite(accounts.getAccountId()).getMaturityDate() + "\"}";
            } else {
                rString = rString + "}";
            }
            if(iterator.hasNext()) rString = rString + ",";
        }
        rString = rString + "]}";
        return rString;
    }
}
