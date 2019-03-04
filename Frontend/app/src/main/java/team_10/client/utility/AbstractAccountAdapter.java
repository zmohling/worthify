package team_10.client.utility;

import com.google.gson.*;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import java.lang.reflect.Type;
import java.time.LocalDate;

import team_10.client.account.*;

public class AbstractAccountAdapter implements JsonSerializer<Account>, JsonDeserializer<Account> {

    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE = "INSTANCE";

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
        Account a = new Account() {
            @Override
            public double getValue(LocalDate d) {
                return 0;
            }
        };

        try {
            JsonObject jsonObject = json.getAsJsonObject();

            JsonPrimitive prim = (JsonPrimitive) jsonObject.get("type");
            String className = "team_10.client.account." + prim.getAsString();
            jsonObject.remove("type");

            Class<?> accountClass = null;
            Class<?>[] transactionClass = null;
            try {
                accountClass = Class.forName(className);
                transactionClass = accountClass.getClasses();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new JsonParseException(e.getMessage());
            }

            //JsonObject transactions = (JsonObject) jsonObject.get("transactions");

            System.out.println("================================");
            System.out.println("TYPE: " + className);
            System.out.println("Transactions TYPE: " + transactionClass[0].getCanonicalName());
            System.out.println(jsonObject.toString());
            System.out.println("================================");

            a = (Account) accountClass.newInstance();

            //a = context.deserialize(jsonObject, accountClass);

            Gson gson = new Gson();
            //a = gson.fromJson(jsonObject, a.getClass());
            if (jsonObject.has("transactions")) {
                JsonElement elem = jsonObject.get("transactions");
                if (elem != null && !elem.isJsonNull()) {

                    Gson gsonDeserializer = new GsonBuilder()
                            .registerTypeAdapter(Transaction.class, new AbstractTransactionAdapter())
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .create();
                    gsonDeserializer.fromJson(jsonObject.get("transactions"), transactionClass[0]);
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return a;
    }
}