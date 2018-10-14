package com.appmeito.android;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;

public class Appmeito {

    public static void EventTracker(String appid,String appsecret,Context context) throws IOException {
        extras extra=new extras(context);
        extra.set_app_id(appid,appsecret);
        extra.set_gaid();
        Log.d("Appmeito","Event Tracker Started");
        context.startService(new Intent(context,AppmeitoServiceForeground.class));
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
            apihandler.UserInstallation(appid,appsecret,object,context);
        }

    }
}
