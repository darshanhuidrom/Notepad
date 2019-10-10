package com.example.integra.notepad.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;

public class AppSignatureHelper extends ContextWrapper {

    private static final String TAG = "AppSignatureHelper";
    private static final String HASH_TYPE = "SHA-256";
    private static final int NUM_HASHED_BYTES = 9;
    private static final int NUM_BASE_64_CHAR = 11;

    public AppSignatureHelper(Context base) {
        super(base);
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    public ArrayList<String> getAppSignature(){
        ArrayList<String> appCodes=new ArrayList<String>();
        try {
            //get all package signature from current package
            String packageName=getPackageName();
            PackageManager packageManager =getPackageManager();
            Signature[] signatures =packageManager.getPackageInfo(packageName,PackageManager.GET_SIGNATURES).signatures;

            // for each signature create a compatible hash
            for (Signature signature:signatures){

                String hash = hash(packageName,signature.toCharsString());
                if(hash!=null){
                    appCodes.add(hash);
                }
            }
        }
        catch (Exception e){
            Log.e(TAG, "Unable to find package to obtain hash.", e);
        }
        return appCodes;
    }

    private static String hash(String packageName ,String signature){
        String appInfo= packageName+signature;
       return null;

    }
}
