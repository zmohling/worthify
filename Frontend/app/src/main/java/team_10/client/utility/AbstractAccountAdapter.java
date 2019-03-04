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

import team_10.client.account.*;

public class AbstractAccountAdapter implements JsonSerializer<Account>, JsonDeserializer<Account> {

    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE  = "INSTANCE";

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
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get("type");
        String className = "team_10.client.account." + prim.getAsString();

        Class<?> _class = null;
        try {
            _class = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
        return context.deserialize(jsonObject.get(INSTANCE), _class);
    }
}