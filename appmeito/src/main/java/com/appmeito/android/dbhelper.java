package com.appmeito.android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Environment;
import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class dbhelper {
    public static final String db_name="Appmeito.db";
    public static final String table_name="appmeito_events";
    static SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/Android/data/" +db_name,null,null);;

    private static void dbstarter(){
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + table_name + "'", null);
        if(cursor.getCount()==0) {
            String create_table = "create table if not exists " + table_name + "(data varchar)";
            Log.d("Appmeito", "table created");
            db.execSQL(create_table);
        }
    }
    public static void insert_data(JsonObject object){
        dbstarter();
        Location location=GpsService.getLocation();
        object.addProperty("timestamp",System.currentTimeMillis());
        if(location==null){
            object.addProperty("lattitude",0);
            object.addProperty("longitude",0);
        }else {
            object.addProperty("lattitude",location.getLatitude());
            object.addProperty("longitude",location.getLongitude());
        }
        object.addProperty("code",0);
        String  insert_table="insert into "+table_name+" values("+"'"+object.toString()+"'"+")" ;
        db.execSQL(insert_table);
        Log.d("Appmeito","Data inserted");
    }

    public static void drop_table()
    {
        db.execSQL("drop table if exists "+table_name);
     }

    public static JsonArray get_data() {
        dbstarter();
        Cursor cursor=db.rawQuery("select * from "+table_name,null);
        JsonArray data=new JsonArray();

        if (cursor.moveToFirst()) {
            do {
               String query=cursor.getString(cursor.getColumnIndex("data"));
                JsonParser parser = new JsonParser();
                JsonObject obj = parser.parse(query).getAsJsonObject();
                data.add(obj);
            } while (cursor.moveToNext());
        }
        return data;
    }
    public static void run_random(String query){
     db.execSQL(query);
    }


}
