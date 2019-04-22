package com.serverApp.serverApp.other;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StockRetrieval {

    public String retrieveStock(String ticker) throws java.io.IOException {
        URL accountURL;
        String url = "https://api.iextrading.com/1.0/stock/"
                + ticker + "/price";
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
        return content.toString();
    }
}
