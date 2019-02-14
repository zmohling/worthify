package com.serverApp.serverApp.controllers;

import com.serverApp.serverApp.models.Article;
import com.serverApp.serverApp.models.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {
    @RequestMapping("/article")
    public String article(@RequestBody Article article) {
        System.out.println("Revieved article object");
        return "";
    }
}
