package com.serverApp.serverApp.controllers;

import com.google.gson.Gson;
import com.serverApp.serverApp.models.Article;
import com.serverApp.serverApp.models.User;
import com.serverApp.serverApp.other.ArticleRetrieval;
import com.serverApp.serverApp.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

@RestController
public class ArticleController {

    @Autowired
    ArticleRepository articleRepo;

    @RequestMapping("/article")
    public String article(@RequestBody Article article) {
        System.out.println("Revieved article object");
        return "";
    }

    @RequestMapping("/article/updateDB")
    public String listTitles(){
        //API key is b46a1992ed6c457bb31e58178813a3cd
        ArticleRetrieval r = new ArticleRetrieval();
        ArrayList<Article> articles;
        Gson g = new Gson();
        try{
            URL articlesURL = new URL("https://newsapi.org/v2/top-headlines?" +
                "sources=financial-times&" +
                "apiKey=b46a1992ed6c457bb31e58178813a3cd");
            articles = r.getFromURL(articlesURL);

            for(int i = 0; i < articles.size(); i ++){
                articleRepo.save(articles.get(i));
            }

            return g.toJson(articles);
        }catch(MalformedURLException e){
            System.out.println("Malformed URL in listTitles()");
        }
        return null;
    }


    @GetMapping("/article/getAll")
    public String getAll(){

        String rString = "{";

        Article[] articles = articleRepo.getAllArticles();
        rString += "\"numArticles\":\"" + articles.length +"\",";

        for(int i = 0; i < articles.length; i ++){
            if(i == articles.length - 1){
                rString =
                        rString
                                + "\"article"
                                + i
                                + "\": {"
                                + "\"id\":\""
                                + articles[i].getId()
                                + "\","
                                + "\"title\":\""
                                + articles[i].getTitle()
                                + "\","
                                + "\"description\":\""
                                + articles[i].getDescription()
                                + "\","
                                + "\"pictureUrl\":\""
                                + articles[i].getUrlToImage()
                                + "\","
                                + "\"url\":\""
                                + articles[i].getUrl()
                                + "\","
                                + "\"isActive\":\""
                                + articles[i].getIsActive()
                                + "\"}";
            } else {
                rString =
                        rString + "\"article"
                                + i
                                + "\": {"
                                + "\"id\":\""
                                + articles[i].getId()
                                + "\","
                                + "\"title\":\""
                                + articles[i].getTitle()
                                + "\","
                                + "\"description\":\""
                                + articles[i].getDescription()
                                + "\","
                                + "\"pictureUrl\":\""
                                + articles[i].getUrlToImage()
                                + "\","
                                + "\"url\":\""
                                + articles[i].getUrl()
                                + "\","
                                + "\"isActive\":\""
                                + articles[i].getIsActive()
                                + "\"},";
            }
        }
        rString += "}";
        return rString;
    }

}
