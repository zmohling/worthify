package team_10.client.utility;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;

import team_10.client.object.account.Account;
import team_10.client.object.account.AccountsWrapper;

public class IO {
    public static int writeAccountsToFile(String accounts, Context context) {
        String filename = "accounts_store";
        String fileContents = accounts;
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    public static String readAccountsFromFile(Context context) {
        String filename = "accounts_store";
        StringBuffer accounts = new StringBuffer();
        try {
            FileInputStream stream = context.openFileInput (filename) ;
            InputStreamReader streamReader = new InputStreamReader (stream) ;
            BufferedReader bufferReader = new BufferedReader (streamReader) ;

            String readString = bufferReader.readLine () ;
            while (readString != null) {
                accounts.append(readString);
                readString = bufferReader.readLine () ;
            }

            streamReader.close () ;
        } catch (Exception e) {
            e.printStackTrace () ;
            if(e instanceof FileNotFoundException) {
                File directory = context.getFilesDir();
                File file = new File(directory, filename);
            }
        }

        return accounts.toString();
    }

    public static String serializeAccounts(List<Account> accounts) {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapter(Account.class, new AbstractAccountAdapter());
        Gson g = b.create();

        AccountsWrapper a = new AccountsWrapper();
        a.setAccounts(accounts);

        return (g.toJson(a, AccountsWrapper.class));
    }

    public static List<Account> deserializeAccounts(String accounts) {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapter(Account.class, new AbstractAccountAdapter());
        Gson g = b.create();

        AccountsWrapper a = g.fromJson(accounts, AccountsWrapper.class);

        if (a == null) {
            return null;
        } else {
            return (a.getAccounts());
        }
    }
}
