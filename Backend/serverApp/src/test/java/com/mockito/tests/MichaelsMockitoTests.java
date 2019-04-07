package com.mockito.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.serverApp.serverApp.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.serverApp.serverApp.controllers.AccountsController;
import com.serverApp.serverApp.repositories.AccountsRepository;
import com.serverApp.serverApp.models.Accounts;
import com.serverApp.serverApp.other.hashingFunction;



public class MichaelsMockitoTests {

    @InjectMocks
    AccountsController accounts;

    @Mock
    AccountsRepository repo;

    @Mock
    UserRepository userRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAccountByIdTest() {
        Accounts account = new Accounts();
        account.setId(1);
        account.setIsActive(1);
        account.setLabel("this label");
        account.setAccountId("000006960000");
        account.setType("Loan");

        when(repo.getAccountsByAccountId("000006960000")).thenReturn(account);

        Accounts testAccount = repo.getAccountsByAccountId("000006960000");
        assertEquals(testAccount.getAccountId(), "000006960000");
        assertEquals(testAccount.getType(), "Loan");
        assertEquals(testAccount.getLabel(), "this label");
        assertEquals(testAccount.getIsActive(), 1);

    }

    @Test
    public void getHashedPassword() throws NoSuchAlgorithmException {
        String password1 = "password";
        String password2 = "password";
        byte[] salt1 = hashingFunction.getSalt();
        byte[] salt2 = hashingFunction.getSalt();


        assertEquals(hashingFunction.hashingFunction(password1, salt1), hashingFunction.hashingFunction(password2, salt1));
        assertNotEquals(hashingFunction.hashingFunction(password1, salt1), hashingFunction.hashingFunction(password2, salt2));

    }

    @Test
    public void authorizationTest() throws NoSuchAlgorithmException {
        String jsonString = "{\"id\": 696}";
        Optional<String> header = Optional.empty();
        assertEquals(accounts.getAccounts(jsonString, header), "{\"error\":\"true\","
                + "\"message\":\"no authentication key\"}");
        header = Optional.of("wsd");
        assertEquals(accounts.getAccounts(jsonString, header), "{\"error\":\"true\","
                + "\"message\":\"invalid authentication key\"}");
        header = Optional.of("9726ab39bafd5758f98fa5ff7ac894db7813c67ec71a0dd0106846bd477b2a8ed7ed2986687a3776ec08c7222db27dbbe45606997dfe0e53538391fedb45c6c9");
        
    }
}
