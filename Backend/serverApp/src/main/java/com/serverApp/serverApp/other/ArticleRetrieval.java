package com.serverApp.serverApp.other;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ArticleRetrieval {

    private HttpURLConnection conn;

    public String getFromURL(URL url){

        try{
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int status = conn.getResponseCode();

            Scanner scan = new Scanner(conn.getInputStream());
            String builtJSON = "";

            while(scan.hasNextLine()){
                builtJSON += scan.nextLine();
            }

            return builtJSON;
        }catch(IOException e){
            System.out.println("Error connecting to: " + url.getPath() + ": " +
                    e.getMessage());
            return "error";
        }
    }

}
