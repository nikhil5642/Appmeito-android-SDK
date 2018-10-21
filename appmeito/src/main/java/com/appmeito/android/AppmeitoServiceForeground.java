package com.appmeito.android;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

public class AppmeitoServiceForeground extends Service {
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    public AppmeitoServiceForeground() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        stopService(new Intent(context,AppmeitoServiceBackground.class));
        final extras extra=new extras(getApplicationContext());
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if(!isStoragePermissionGranted()){
                    Log.d("Appmeito","Storage Permission not granted");
                    handler.postDelayed(runnable,30000);
                    return;
                }
                String adid=extra.get_gaid();
                String android_id=extra.get_android_id();
                String appid=extra.get_app_id();
                String app_secret=extra.get_app_secret();

                if(adid == null){
                    Log.d("Appmeito","Unable to load GAID");
                    handler.postDelayed(runnable,30000);
                    return;
                }

                if(!extra.get_installation_status()){
                    JsonObject object=new JsonObject();
                    JsonObject activity=new JsonObject();
                    activity.addProperty("timestamp",System.currentTimeMillis());
                    activity.addProperty("action","install");
                    object.addProperty("adid",extra.get_gaid());
                    object.addProperty("macid",extra.get_android_id());
                    object.addProperty("appid",appid);
                    object.add("device_info",extra.get_device_info());
                    object.add("activity",activity);
                    try {
                        apihandler.UserInstallation(appid,app_secret,object,context);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
               JsonArray event_data=dbhelper.get_data();
                if(event_data.size()==0){
                    Log.d("Appmeito","No data to process");
                    handler.postDelayed(runnable, 30000);
                    return;
                }

                JsonObject object=new JsonObject();
                object.addProperty("adid",adid);
                object.addProperty("macid",android_id);
                object.addProperty("appid",appid);
                object.add("event_json",event_data);
                Log.d("Appmeito",object.toString());

                try {
                    apihandler.sendEventData(appid,app_secret,object);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(runnable, 30000);
            }
        };
        handler.postDelayed(runnable, 30000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Log.d("Appmeito", "App destroyed");
        //Code here
        startService(new Intent(context,AppmeitoServiceBackground.class));
        stopSelf();
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }


}

