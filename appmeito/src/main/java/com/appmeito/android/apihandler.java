package com.appmeito.android;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class apihandler{

    //private String base_url="http://13.127.189.127";
    private static String base_url="http://172.25.13.63:5000";
    private static String EventDataUrl=base_url+"/event_data";
    private static String UserInstallationUrl=base_url+"/user_installation";
    private static String UserUninstallationUrl=base_url+"/user_uninstallation";

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    static OkHttpClient client = new OkHttpClient();

    public static void sendEventData(String app_id, String app_secret, final JsonObject data) throws IOException {
         RequestBody body = RequestBody.create(JSON, data.toString());

         final Request request = new Request.Builder()
                .url(EventDataUrl)
                .addHeader("app_id", app_id)
                .addHeader("app_secret",app_secret)
                .post(body)
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Appmeito","Request problem");
                Log.d("Appmeito",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    JsonParser jsonParser = new JsonParser();
                    JsonObject obj = jsonParser.parse(response.body().string()).getAsJsonObject();
                if(obj.get("success").getAsBoolean()==true){
                        JsonArray obj2= (JsonArray) data.get("event_json");
                        for(int i=0;i<obj2.size();i++){
                            dbhelper.run_random("delete from "+dbhelper.table_name+" where data='"+obj2.get(i).toString()+"'");
                        }
               }else {
                    Log.d("Appmeito",obj.get("message").toString());

                }
            }

            }
        });

    }

    public static void UserInstallation(String app_id, String app_secret, final JsonObject data, final Context context) throws IOException {
        RequestBody body = RequestBody.create(JSON, data.toString());
        final Request request = new Request.Builder()
                .url(UserInstallationUrl)
                .addHeader("app_id", app_id)
                .addHeader("app_secret",app_secret)
                .post(body)
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Appmeito","Appmeito Installation Failed");
                Log.d("Appmeito",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    JsonParser jsonParser = new JsonParser();
                    JsonObject obj = jsonParser.parse(response.body().string()).getAsJsonObject();
                    if(obj.get("success").getAsBoolean()==true){
                        Log.d("Appmeito",obj.get("Appmeito registered").toString());
                        extras extra=new extras(context);
                        extra.app_installated();
                    }
                }

            }
        });
    }

    public static String UserUninstallation(String app_id, String app_secret, JsonObject data) throws IOException {
        RequestBody body = RequestBody.create(JSON, data.toString());
        Request request = new Request.Builder()
                .url(UserUninstallationUrl)
                .addHeader("app_id", app_id)
                .addHeader("app_secret",app_secret)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
