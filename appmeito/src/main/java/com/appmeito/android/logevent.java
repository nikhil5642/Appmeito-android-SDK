package com.appmeito.android;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class logevent {

    public static void generate(Object data,Context context) {
        Gson gson=new Gson();
        JsonElement jsonElement = gson.toJsonTree(data);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("event",jsonElement);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                dbhelper.insert_data(jsonObject);
            }
        }else {
            dbhelper.insert_data(jsonObject);
        }
    }
}
