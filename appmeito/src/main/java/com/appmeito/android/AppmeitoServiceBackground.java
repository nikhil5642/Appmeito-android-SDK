package com.appmeito.android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

public class AppmeitoServiceBackground extends Service {
    public Handler handler = null;
    public static Runnable runnable = null;

    public AppmeitoServiceBackground() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        final extras extra = new extras(getApplicationContext());
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                String adid = extra.get_gaid();
                String android_id = extra.get_android_id();
                String appid = extra.get_app_id();
                String app_secret = extra.get_app_secret();
                JsonArray event_data = dbhelper.get_data();
                if (event_data.size() == 0) {
                    Log.d("Appmeito", "No data to process, Killing the Service");
                    stopSelf();
                    return;
                }
                JsonObject object = new JsonObject();
                object.addProperty("adid", adid);
                object.addProperty("macid", android_id);
                object.addProperty("appid", appid);
                object.add("event_json", event_data);
                Log.d("Appmeito", object.toString());

                try {
                    apihandler.sendEventData(appid, app_secret, object);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(runnable, 600000);
            }
        };
        handler.postDelayed(runnable, 600000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopSelf();
    }
}