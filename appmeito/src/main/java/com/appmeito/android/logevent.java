package com.appmeito.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class logevent {

    public static void generate(Object data) {
        Gson gson=new Gson();
        JsonElement jsonElement = gson.toJsonTree(data);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("event",jsonElement);
        dbhelper.insert_data(jsonObject);
    }
}
