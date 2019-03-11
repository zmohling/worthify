package com.serverApp.serverApp.other;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.serverApp.serverApp.models.Article;
import org.json.JSONArray;
import org.json.JSONObject;


public class ArticleRetrieval {

    private HttpURLConnection conn;

    public ArrayList<Article> getFromURL(URL url){
        try{
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int status = conn.getResponseCode();

            Scanner scan = new Scanner(conn.getInputStream());
            String builtJSON = "";

            while(scan.hasNextLine()){
                builtJSON += scan.nextLine();
            }
            //System.out.println(builtJSON);

            Gson g = new Gson();
            JSONObject o = new JSONObject(builtJSON);

            JSONArray arr = o.getJSONArray("articles");

            Type articleType = new TypeToken<ArrayList<Article>>(){}.getType();
            ArrayList<Article> articles = g.fromJson(arr.toString(), articleType);
            return articles;
        }catch(IOException e){
            System.out.println("Error connecting to: " + url.getPath() + ": " +
                    e.getMessage());
            return null;
        }
    }

}
