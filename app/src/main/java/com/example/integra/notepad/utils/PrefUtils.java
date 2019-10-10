package com.example.integra.notepad.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

    private static SharedPreferences getSharePreferences(Context context){
        return context.getSharedPreferences("APP_PREF",Context.MODE_PRIVATE);
    }

    public static void storeApiKey(Context context,String apiKey){
        SharedPreferences.Editor editor = getSharePreferences(context).edit();
        editor.putString("API_KEY",apiKey).commit();
    }


    public static String getApiKey(Context context){
        return getSharePreferences(context).getString("API_KEY",null);
    }
}
