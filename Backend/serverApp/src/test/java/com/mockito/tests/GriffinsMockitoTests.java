package com.mockito.tests;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

import com.serverApp.serverApp.controllers.UserController;
import com.serverApp.serverApp.models.Article;
import com.serverApp.serverApp.models.User;
import com.serverApp.serverApp.other.ArticleRetrieval;
import com.serverApp.serverApp.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Optional;

public class GriffinsMockitoTests {

    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepo;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testArticleRetrieval() {
        ArticleRetrieval ar = new ArticleRetrieval();
        ArrayList<Article> articles;
        try{
            URL articlesURL = new URL("https://newsapi.org/v2/everything?" +
                    "q=" + URLEncoder.encode("tech", "UTF-8") + "&" +
                    "apiKey=b46a1992ed6c457bb31e58178813a3cd");
            articles = ar.getFromURL(articlesURL);
            assertTrue(articles.size() > 0);
            assertTrue(articles.get(0).getUserId() == 0);
        }catch (Exception e){
            //automatically fail the test
            assertTrue(true == false);
        }
    }

    @Test
    public void testAdminDeleteUser(){
        Optional<String> authentication = Optional.empty();
        assertEquals(userController.deleteUser(0, authentication), "{\"error\":\"true\","
                + "\"message\":\"no authentication key\"}");

        authentication = Optional.of("invalid authentication");
        assertEquals(userController.deleteUser(-1, authentication), "{\"error\":\"true\","
                + "\"message\":\"invalid authentication key\"}");
    }

    @Test
    public void testUserRepository(){
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setFirstName("test");
        user.setLastName("ing");
        user.setId(1L);
        user.setType(0);
        user.setPassword("pass");

        when(userRepo.getAdmin("pass")).thenReturn(null);

        //because the user is not an admin
        assertEquals(userRepo.getAdmin("pass"), null);

        user.setType(1);
        when(userRepo.getAdmin("pass")).thenReturn(user);

        User admin = userRepo.getAdmin("pass");
        assertEquals(admin.getEmail(), "test@gmail.com");
        assertEquals(admin.getId(), 1L);
        assertEquals(admin.getType(), 1);

        when(userRepo.checkEmail("test@gmail.com")).thenReturn(1);

        assertTrue(userRepo.checkEmail("test@gmail.com") == 1);
    }

}
