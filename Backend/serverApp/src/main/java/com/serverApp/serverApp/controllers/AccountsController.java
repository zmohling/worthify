package com.serverApp.serverApp.controllers;

import com.google.gson.reflect.TypeToken;
import com.serverApp.serverApp.models.Accounts;
import com.serverApp.serverApp.models.LoanTransaction;
import com.serverApp.serverApp.models.User;
import com.serverApp.serverApp.models.Vehicle;
import com.serverApp.serverApp.repositories.AccountsRepository;
import com.serverApp.serverApp.repositories.UserRepository;
import com.serverApp.serverApp.repositories.LoanTransactionRepository;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import org.hibernate.annotations.Target;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.*;

import java.awt.*;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.util.*;

@RestController
public class AccountsController {
    @Autowired
    AccountsRepository accountsRepo;

    @Autowired
    LoanTransactionRepository loanTransactionRepo;

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
                    "\"label\":\"" + iterator.next().getLabel() + "\",";
         //           "\"userId\":\"" + iterator.next().getUserId() + "\"";
        }
        return "";
    }

    public int getSize(String string) {
        int i = 0;
        int bracketCount = -1;
        for(int j = 0; j < string.length(); j++) {
            if(string.charAt(j) == '[') bracketCount = 0;
            if(string.charAt(j) == '{' && bracketCount >= 0) bracketCount++;
            if(string.charAt(j) == '}' && bracketCount >= 0) bracketCount--;
            if(string.charAt(j) == ',' && bracketCount == 0) i++;
            if(string.charAt(j) == ']') {
                i++;
                return i;
            }
        }
        return -1;
    }

    public Accounts parseAccounts(String string, int count) {
        int i = 0;
        int bracketCount = -1;
        Accounts accounts = new Accounts();
        boolean idChecked, labelChecked, typeChecked;
        idChecked = labelChecked = typeChecked = false;
        for(int j = 0; j < string.length(); j++) {
            if(string.charAt(j) == '[') bracketCount = 0;
            if(string.charAt(j) == '{' && bracketCount >= 0) bracketCount++;
            if(string.charAt(j) == '}' && bracketCount >= 0) bracketCount--;
            if(string.charAt(j) == ',' && bracketCount == 0) i++;
            if(string.charAt(j) == ']') i++;
            if(string.charAt(j) == 'l' && string.charAt(j+1) == 'a'
                    && string.charAt(j+2) == 'b' && string.charAt(j+3) == 'e'
                    && string.charAt(j+4) == 'l'
                && !idChecked) {
                j = j + 6;
                while(true) {
                   if(string.charAt(j) == '"') {
                       j++;
                       String label = "";
                       while(string.charAt(j) != '"') {
                           label = label + string.charAt(j);
                           j++;
                       }
                       accounts.setLabel(label);

                   }
                   else j++;
                }
            }
        }
        return accounts;
    }

    @RequestMapping("/addAccounts")
    public String add(@RequestBody String string){
        int i = getSize(string);
        while(i > 0) {
            Accounts accounts = parseAccounts(string, i);
            accountsRepo.save(accounts);
        }
        return "Size = " + i;
    }

    @RequestMapping("/addAccount")
    public String addAccounts(@RequestBody String string) {
        JSONObject obj = new JSONObject(string);
        JSONArray accountsArr = obj.getJSONArray("accounts");
        Type accountType = new TypeToken<ArrayList<Accounts>>(){}.getType();
        Type isActiveType = new TypeToken<ArrayList<Integer>>(){}.getType();
        Gson g = new Gson();
        ArrayList<Accounts> accountsList = g.fromJson(accountsArr.toString(), accountType);
        //for(int i = 0; i < 1; i++) {
            //JSONObject elem = accountsArr.getJSONObject(i).getJSONObject("transactions");
            //System.out.println(elem.toString());
            //System.out.println(accountsList.get(i).getId());
            ///System.out.println(accountsList.get(i).getLabel());
            //System.out.println(accountsList.get(i).getType());
            //System.out.println(accountsList.get(i).getIsActive());
            //System.out.println(accountsList.get(i).getTransactions());
        //}
        return "";
    }

    @RequestMapping("/remove")
    public String remove(@RequestBody Accounts accounts){
        accountsRepo.delete(accounts);
        String rString =
                "{\"error\":\"false\","
                        + "\"message\":\"account removal success\","
                        + "\"account\":{"
                        + "\"id\":\"" + accounts.getId() + "\"," +
                        "\"type\":\"" + accounts.getType() + "\"," +
                        "\"label\":\"" + accounts.getLabel() + "\",";
        //                "\"userId\":\"" + accounts.getUserId() + "\"}}";
        return rString;
    }
}
