package com.appmeito.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class logevent {

    public static void generate(Object data, Context context) throws ClassNotFoundException {
        Gson gson=new Gson();
        JsonElement jsonElement = gson.toJsonTree(data);
        JsonObject jsonObject = (JsonObject) jsonElement;

        ids ids=new ids(context);

        dbhelper dbhelper=new dbhelper(context);
        dbhelper.insert_data(jsonObject);
        Log.d("Appmeito",dbhelper.get_data().toString());
        //dbhelper.drop_table();

    }

}
