package com.serverApp.serverApp.other;

import com.serverApp.serverApp.models.RealEstate;
import com.serverApp.serverApp.models.Stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * class to handle the realestate api (zillow)
 *
 * @author Michael Davis
 *
 */
public class RealEstateRetrieval {
    public String retrieveRealEstate(RealEstate realEstate) throws IOException {
        URL accountURL;
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
    return val;
    }
}
