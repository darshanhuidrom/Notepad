package com.example.integra.notepad.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.integra.notepad.utils.Config;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class OTPReceiver extends BroadcastReceiver {
    private static final String TAG = "OTPReceiver";
    private OtpRecievedInterface otpRecievedInterface;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            Status status = (Status) bundle.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:

                    String message = (String) bundle.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Log.d(TAG + Config.LOG_SEPERATOR, "onReceive: success " + message);
                    if (otpRecievedInterface != null) {
                        String otp = message.replace("<#> Your otp code is : ", "");
                        otpRecievedInterface.onOtpReceived(otp);
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    Log.d(TAG, "onReceive: failure");
                    if (otpRecievedInterface != null) {
                        otpRecievedInterface.onOtpTimeOut();
                    }

            }

        }

    }

    public void setOnOtpListener(OtpRecievedInterface listener) {
        this.otpRecievedInterface = listener;
    }

    public interface OtpRecievedInterface {
        void onOtpReceived(String otp);

        void onOtpTimeOut();
    }
}
