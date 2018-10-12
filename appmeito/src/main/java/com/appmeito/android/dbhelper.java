package com.appmeito.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.appmeito.android.R.string.appmeito_adid;

public class dbhelper {
    public static final String db_name="Appmeito.db";
    public static final String table_name="appmeito_events";
    Context context;
    static SQLiteDatabase db;
    SharedPreferences preferences;
    static SharedPreferences.Editor editor;
    public dbhelper(Context context) {
        db=SQLiteDatabase.openOrCreateDatabase(context.getApplicationInfo().dataDir+"/"+db_name,null,null);
        preferences=context.getSharedPreferences(String.valueOf((R.string.appmeito_data_file)),0);
        editor=preferences.edit();
        this.context = context;
    }

    private void dbstarter(JsonObject object){
        String create_table="create table if not exists "+table_name+"(data varchar)";
        Log.d("Appmeito",create_table);
        db.execSQL(create_table);
        editor.putBoolean(String.valueOf(R.string.appmeito_db_created),true);
        editor.commit();
    }
    public void insert_data(JsonObject object){
         if(!preferences.getBoolean(String.valueOf(R.string.appmeito_db_created),false)){
            dbstarter(object);
        }

        object.addProperty("adid",ids.get_gaid());
        object.addProperty("android_id",ids.get_android_id());
         if(object.get("adid")==null){
            Log.d("Appmeito","Problem receiving adid");
            return;
        }
        object.addProperty("timestamp",System.currentTimeMillis());
        String  insert_table="insert into "+table_name+" values("+"'"+object.toString()+"'"+")" ;
        db.execSQL(insert_table);
        editor.putBoolean(String.valueOf(R.string.appmeito_has_data),true);
        editor.commit();
        Log.d("Appmeito","Data inserted");

    }

    public static void drop_table(){
        db.execSQL("drop table if exists "+table_name);
        editor.putBoolean(String.valueOf(R.string.appmeito_db_created),false);
        editor.commit();
    }
    public JsonArray get_data() {
        if(!preferences.getBoolean(String.valueOf(R.string.appmeito_db_created),false)){
            Log.d("Appmeito","Nothing to display");
            return null;
        }

        Cursor cursor=db.rawQuery("select * from "+table_name,null);
        JsonArray data=new JsonArray();

        if (cursor.moveToFirst()) {
            do {
               String query=cursor.getString(cursor.getColumnIndex("data"));
               JsonElement obj= (JsonElement) (new Gson()).toJsonTree(query);
               data.add(obj);
            } while (cursor.moveToNext());
        }
        return data;
    }
}
