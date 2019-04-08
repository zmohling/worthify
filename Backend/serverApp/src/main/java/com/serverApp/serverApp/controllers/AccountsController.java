package com.serverApp.serverApp.controllers;

import com.google.gson.reflect.TypeToken;
import com.serverApp.serverApp.models.*;
import com.serverApp.serverApp.repositories.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.*;

import com.serverApp.serverApp.repositories.AccountsRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Date;
import java.util.*;

@RestController
public class AccountsController {
    @Autowired
    AccountsRepository accountsRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    CertificateOfDepositRepository certRepo;
    @Autowired
    RealEstateRepository realEstateRepo;
    @Autowired
    StockRepository stockRepo;

    enum needsAPI
    {
        RealEstate;
    }
    @RequestMapping("/accounts/get/all")
    public String getAccounts(@RequestBody String string, @RequestHeader(value = "Authorization") Optional<String> header) {
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
        JSONObject obj = new JSONObject(string);
        long user = Long.parseLong(obj.get("id").toString());
        Collection<Accounts> allAccounts = accountsRepo.getAccounts(user);
        Iterator<Accounts> iterator = allAccounts.iterator();
        if(user != id) {
            System.out.println("Requested users accounts do not match authentication keys");
            return "{\"error\":\"true\","
                    + "\"message\":\"requested users accounts do not match authentication keys\"}";
        }
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
            } else if(accounts.getType().equals("RealEstate")) {
                rString = rString + ",\"address\":\"" + realEstateRepo.getRealEstate(accounts.getAccountId()).getAddress() + "\"";
                rString = rString + ",\"city\":\"" + realEstateRepo.getRealEstate(accounts.getAccountId()).getCity() + "\"";
                rString = rString + ",\"state\":\"" + realEstateRepo.getRealEstate(accounts.getAccountId()).getState() + "\"}";
            } else if(accounts.getType().equals("Stock")) {
                rString = rString + ",\"ticker\":\"" + stockRepo.getStock(accounts.getAccountId()).getTicker() + "\"}";
            } else {
                rString = rString + "}";
            }
            if(iterator.hasNext()) rString = rString + ",";
        }
        rString = rString + "]}";
        return rString;
    }


    @RequestMapping("/accounts/fetch")
    public String fetchAccounts(@RequestBody String string, @RequestHeader(value = "Authorization") Optional<String> header) throws IOException {
        int exists = -1;
        long ID = -1;
        if(header.isPresent()) {
            exists = userRepo.checkUserExists(header.get());
            if(exists == 0) {
                System.out.println("Unauthorized, invalid key");

                return "{\"error\":\"true\","
                        + "\"message\":\"invalid authentication key\"}";
            } else {
                ID = userRepo.getUserID(header.get());
                System.out.println(ID + " matches the authentication key");
            }
        } else {
            System.out.println("Unauthorized, no key");
            return "{\"error\":\"true\","
                    + "\"message\":\"no authentication key\"}";
        }

        JSONObject obj = new JSONObject(string);
        JSONArray accountsArr = obj.getJSONArray("accountID");
        Type accountType = new TypeToken<ArrayList<String>>(){}.getType();
        Gson g = new Gson();
        URL accountURL;
        String rString = "{";
        for(int i = 0; i < accountsArr.length(); i++) {
            String id = accountsArr.get(i).toString();
            String type = accountsRepo.getAccountsByAccountId(id).getType();

            if(type.equals("RealEstate")) {
                RealEstate realEstate = new RealEstate();
                realEstate.setAccountId(id);
                realEstate.setState(realEstateRepo.getRealEstate(realEstate.getAccountId()).getState());
                realEstate.setAddress(realEstateRepo.getRealEstate(realEstate.getAccountId()).getAddress());
                realEstate.setCity(realEstateRepo.getRealEstate(realEstate.getAccountId()).getCity());
                Scanner scanner = new Scanner(realEstate.getAddress());
                String url = "http://www.zillow.com/webservice/GetSearchResults.htm?zws-id=X1-ZWz1894b6xqbd7_a6mew&" +
                        "address=" + scanner.next();
                while(scanner.hasNext()) {
                    url = url + "+" + scanner.next();
                }
                url = url + "&citystatezip=" + realEstate.getCity() + "%2C+" + realEstate.getState() + "\n";

                accountURL = new URL(url);
                HttpURLConnection con = (HttpURLConnection) accountURL.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                //parse the content
                String[] output = content.toString().split("amount");
                String val = "";
                for(int j = 0; j < output[1].length(); j++) {
                    if(Character.isDigit(output[1].charAt(j))) {
                        val = val + output[1].charAt(j);
                    }
                }
                if(i != 0) rString = rString + ",";
                rString = rString + "\"" + id + "\" :" + val;
            } else if (type.equals("Stock")) {
                Stock stock = new Stock();
                stock.setAccountID(id);
                stock.setTicker(stockRepo.getStock(id).getTicker());
                String url = "https://api.iextrading.com/1.0/stock/"
                        + stock.getTicker() + "/price";
                accountURL = new URL(url);
                HttpURLConnection con = (HttpURLConnection) accountURL.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                if(i != 0) rString = rString + ",";
                rString = rString + "\"" + id + "\" :" + content.toString();
            }
        }
        rString = rString + "}";
        return rString;
    }

    @RequestMapping("/accounts/add")
    public String addAccounts(@RequestBody String string, @RequestHeader(value = "Authorization") Optional<String> header) {
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
        for(int i = 0; i < accountsList.size(); i++) {
            String accountID = accountsList.get(i).getAccountId();
            long userID = Long.parseLong(accountID.substring(0, 8));
            boolean willBreak = false;
            System.out.println("UserID: " + userID + ", id: " + id);
            while(userID != id) {
                i++;
                if(i > accountsList.size()) {
                    willBreak = true;
                    break;
                } else {
                    accountID = accountsList.get(i).getAccountId();
                }
                userID = Long.parseLong(accountID.substring(0, 8));
            }
            if(willBreak) break;

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

            } else if(accountsList.get(i).getType().equals("RealEstate")) { //when the type is RealEstate
                String address = accountsArr.getJSONObject(i).getString("address");
                String city = accountsArr.getJSONObject(i).getString("city");
                String state = accountsArr.getJSONObject(i).getString("state");
                city = city.replace(' ', '-');
                RealEstate realEstate = new RealEstate();
                realEstate.setAccountId(accountsList.get(i).getAccountId());
                realEstate.setAddress(address);
                realEstate.setCity(city);
                realEstate.setState(state);
                realEstateRepo.save(realEstate);

            } else if(accountsList.get(i).getType().equals("Stock")) { //when the type is Stock
                String ticker = accountsArr.getJSONObject(i).getString("ticker");
                Stock stock = new Stock();
                stock.setAccountID(accountsList.get(i).getAccountId());
                stock.setTicker(ticker);
                stockRepo.save(stock);
            }
        }
        String rString =
                "{\"accounts\":[";

        Iterator<Accounts> iterator = accountsList.iterator();
        boolean first = true;
        while((iterator).hasNext()) {
            boolean willBreak = false;
            Accounts accounts = iterator.next();
            String accountID = accounts.getAccountId();
            long userID = Long.parseLong(accountID.substring(0, 8));
            while(userID != id) {
                if((iterator).hasNext()) accounts = iterator.next();
                else {
                    willBreak = true;
                    break;
                }
                accountID = accounts.getAccountId();
                userID = Long.parseLong(accountID.substring(0, 8));
            }
            if(willBreak) {
                break;
            }
            if(first == false){
                rString = rString + ",";
            }
            rString = rString +
                    "{\"accountID\":\"" + accounts.getAccountId() + "\"," +
                    "\"label\":\"" + accounts.getLabel() + "\"," +
                    "\"transactions\":" + accounts.getTransactions() + "," +
                    "\"type\":\"" + accounts.getType() + "\"";
            first = false;
            if(accounts.getType().equals("CertificateOfDeposit")) {
                rString = rString + ",\"maturityDate\":\"" + certRepo.getCertificateOfDeposite(accounts.getAccountId()).getMaturityDate() + "\"}";

            } else if (accounts.getType().equals("RealEstate")) {
                rString = rString + ",\"address\":\"" + realEstateRepo.getRealEstate(accounts.getAccountId()).getAddress() + "\"";
                rString = rString + ",\"city\":\"" + realEstateRepo.getRealEstate(accounts.getAccountId()).getCity() + "\"";
                rString = rString + ",\"state\":\"" + realEstateRepo.getRealEstate(accounts.getAccountId()).getState() + "\"}";

            } else if (accounts.getType().equals("Stock")) {
                rString = rString + ",\"ticker\":\"" + stockRepo.getStock(accounts.getAccountId()).getTicker() + "\"}";

            } else {
                rString = rString + "}";
            }
        }
        rString = rString + "]}";
        return rString;
    }


}
