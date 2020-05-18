package com.chenjim.andlibs.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by jimi on 2018/3/27.
 */

public class JsonHelper {
    private static Gson gson = new GsonBuilder().setLenient().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private static JsonParser jsonParser = new JsonParser();

    /**
     * @param json
     * @param type Type type = new TypeToken<List<CheckInItem>>(){}.getType();
     * @param <T>
     * @return
     */
    public static <T> List<T> parseArray(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public static <T> T parse(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T parse(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static String parseString(String jsonData, String key) {
        JsonObject jsonObject = jsonParser.parse(jsonData).getAsJsonObject();
        return jsonObject.get(key).getAsString();
    }

    public static Float parseFloat(String jsonData, String key) {
        JsonObject jsonObject = jsonParser.parse(jsonData).getAsJsonObject();
        return jsonObject.get(key).getAsFloat();
    }

    public static Double parseDouble(String jsonData, String key) {
        JsonObject jsonObject = jsonParser.parse(jsonData).getAsJsonObject();
        return jsonObject.get(key).getAsDouble();
    }

}
