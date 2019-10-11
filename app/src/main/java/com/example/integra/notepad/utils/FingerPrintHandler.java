package com.example.integra.notepad.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.TextView;

import com.example.integra.notepad.R;
import com.example.integra.notepad.view.BarcodeActivity;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerPrintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    private static final String TAG = "FingerPH" + Config.LOG_SEPERATOR;

    public FingerPrintHandler(Context context) {
        this.context = context;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Fingerprint permission not granted");
            return;
        }
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

    }


    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        update("Fingerprint authentication error\n" + errString, false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        update("Fingerprint authentication help\n " + helpString, false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        update("Fingerprint authentication success", true);
    }

    @Override
    public void onAuthenticationFailed() {
        update("Fingerprint authentication failed", false);
    }

    public void update(String e, Boolean success) {

        TextView tv = ((Activity) context).findViewById(R.id.tv);
        tv.setText(e);
        if (success) {
            tv.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            ((Activity) context).finish();
            startActivity();

        }

    }

    public void startActivity() {
        Intent intent = new Intent(((Activity) context), BarcodeActivity.class);
        ((Activity) context).startActivity(intent);
    }
}
