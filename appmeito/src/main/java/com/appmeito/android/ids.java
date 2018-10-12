package com.appmeito.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.lang.reflect.Method;

public class ids {
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;
    static Context context;
    String gaid;
    public ids(Context context) {
        preferences=context.getApplicationContext().getSharedPreferences(String.valueOf(R.string.appmeito_data_file),0);
        editor=preferences.edit();
        this.context=context;
    }


    public static String get_gaid(){
        final String[] gaid = {preferences.getString(String.valueOf(R.string.appmeito_adid), null)};
        if(gaid[0] !=null){
            return gaid[0];
        }
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
                gaid[0] =advertId;
                editor.putString(String.valueOf(R.string.appmeito_adid),advertId);
                editor.commit();
            }

        }.execute();
        return gaid[0];
    }

    public static String get_android_id(){
        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return androidId;
    }

}
