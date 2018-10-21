package com.appmeito.android;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.regex.Pattern;

public class Appmeito {

    public static void EventTracker(String appid, String appsecret, Context context){
        extras extra=new extras(context);
        extra.set_app_id(appid,appsecret);
        extra.set_gaid();
        Log.d("Appmeito","Event Tracker Started");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
            context.startService(new Intent(context,AppmeitoServiceForeground.class));
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                context.startService(new Intent(context, GpsService.class));
            }
        }

        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            Log.d("Appmeito",account.toString());
            if (pattern.matcher(account.name).matches()) {
                String emailAddress = account.name;
                Log.d("Appmeito",emailAddress);
                // grab each of your user's email addresses
            }
        }
    }


}
