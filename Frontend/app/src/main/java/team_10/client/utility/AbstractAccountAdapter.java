package team_10.client.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;

import team_10.client.object.account.Account;
import team_10.client.object.account.Transaction;

public class AbstractAccountAdapter implements JsonSerializer<Account>, JsonDeserializer<Account> {

    private static final String PACKAGE = "team_10.client.object.account";

    @Override
    public JsonElement serialize(Account src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        Gson gson = new Gson();
        String className = src.getClass().getSimpleName();
        JsonElement elem = gson.toJsonTree(src, src.getClass());
        JsonObject retValue = elem.getAsJsonObject();
        retValue.addProperty("type", className);

        return retValue;
    }

    @Override
    public Account deserialize(JsonElement json, Type typeOfT,
                               JsonDeserializationContext context) throws JsonParseException {
        /* Dummy instantiation */
        Account account = null;

        try {
            JsonObject jsonObject = json.getAsJsonObject();

            Class<?> accountClass = null;
            Class<?> transactionClass = null;
            try {
                /* Get account type */
                JsonElement prim = jsonObject.get("type");
                String accountClassString = PACKAGE + "." + prim.getAsString();
                jsonObject.remove("type");

                /* Get classes from String */
                accountClass = Class.forName(accountClassString);
                transactionClass = (accountClass.getClasses())[0];
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new JsonParseException(e.getMessage());
            }

            /* Gson builders with custom Type Adapters for parsing */
            GsonBuilder outerBuilder = new GsonBuilder();
            final Class<?> finalTransactionClass = transactionClass;
            outerBuilder.registerTypeAdapter(Transaction.class, new JsonDeserializer<Transaction>() {
                @Override
                public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    GsonBuilder innerBuilder = new GsonBuilder();
                    innerBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
                    Gson innerGson = innerBuilder.create();

                    return (Transaction) innerGson.fromJson(json, finalTransactionClass);
                }
            });
            outerBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());

            Gson outerGson = outerBuilder.create();

            account = (Account) outerGson.fromJson(jsonObject, accountClass);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return account;
    }
}