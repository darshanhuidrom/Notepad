package com.example.integra.notepad;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Telephony;

import com.example.integra.notepad.Database.MyDatabaseHelper;
import com.example.integra.notepad.receiver.SmsReceiver;
import com.example.integra.notepad.utils.Config;

public class MyApplication extends Application {

    private static MyApplication instance;
    private static MyDatabaseHelper databaseHelper;
    private static  SmsReceiver smsReceiver;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        databaseHelper = new MyDatabaseHelper(getAppContext());
        smsReceiver = new SmsReceiver();
        registerReceiver(smsReceiver,new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));


    }

    public static SmsReceiver getSmsReceiver(){
        return smsReceiver;
    }

    public static MyApplication getInstance(){
        return  instance;
    }

    public static Context getAppContext(){
       return instance.getApplicationContext();
    }

    public static MyDatabaseHelper getDatabaseHelper(){
        return databaseHelper;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(smsReceiver);
    }


}
