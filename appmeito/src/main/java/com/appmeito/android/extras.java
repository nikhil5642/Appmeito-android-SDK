package com.appmeito.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.reflect.Method;

public class extras {
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;
    static Context context;
    public extras(Context context) {
        preferences=context.getApplicationContext().getSharedPreferences(String.valueOf(R.string.appmeito_data_file),0);
        editor=preferences.edit();
        this.context=context;
    }


    public static String get_gaid(){
        String gaid = preferences.getString(String.valueOf(R.string.appmeito_adid), null);
        return gaid;
     }
    public static void set_gaid(){
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String advertId = null;
                try{
                    advertId = idInfo.getId();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                return advertId;
            }
            @Override
            protected void onPostExecute(String advertId) {
                editor.putString(String.valueOf(R.string.appmeito_adid),advertId);
                editor.commit();
                Log.d("Appmeito","Adid saved");
            }

        };
        task.execute();
    }

    public static String get_android_id(){
        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public static JsonObject get_device_info(){
        JsonObject object=new JsonObject();
        object.addProperty("brand",Build.BRAND);
        object.addProperty("model",Build.MODEL);
        object.addProperty("platform","android");
        object.addProperty("api_level",Build.VERSION.SDK_INT);
        Log.d("Appmeito","Device_info: "+object.toString());
        return object;
    }

    public static void set_app_id(String app_id,String app_secret){
        editor.putString(String.valueOf(R.string.appmeito_appid),app_id);
        editor.putString(String.valueOf(R.string.appmeito_appsecret),app_secret);
        editor.commit();
    }
    public static String  get_app_id(){
        return preferences.getString(String.valueOf(R.string.appmeito_appid),null);
    }
    public static void app_installated(){
        editor.putBoolean(String.valueOf(R.string.appmeito_installation),true);
        editor.commit();
    }
    public static Boolean  get_installation_status(){
        return preferences.getBoolean(String.valueOf(R.string.appmeito_installation),false);
    }
    public static String  get_app_secret(){
        return preferences.getString(String.valueOf(R.string.appmeito_appsecret),null);
    }

}
