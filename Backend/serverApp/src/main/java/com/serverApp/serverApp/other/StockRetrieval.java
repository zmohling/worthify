package com.serverApp.serverApp.other;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.hibernate.type.DateType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

/**
 * class to handle the stock apis and return them
 *
 * @author Michael Davis
 */
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

    public String retrieve5yData(String ticker, Date date) throws java.io.IOException {
        URL accountURL;
        String url = "https://api.iextrading.com/1.0/stock/"
                + ticker + "/chart/5y";
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
        JsonParser jsonParser = new JsonParser();
        JsonArray arr = jsonParser.parse(content.toString()).getAsJsonArray();
        String returnStr = "";
        boolean first = true;
        for(int i = 0; i < arr.size(); i++) {
            Date iterDate = java.sql.Date.valueOf(LocalDate.parse(arr.get(i).getAsJsonObject().get("date").getAsString()));
            if(date.compareTo(iterDate) <= 0) {
                if (!first) {
                    returnStr = returnStr + ",";
                }
                returnStr = returnStr + "\"" + iterDate.toString() + "\"" + ": " + "\"" + arr.get(i).getAsJsonObject().get("high") + "\"";
                first = false;
            }
        }
        return returnStr;
    }
}
