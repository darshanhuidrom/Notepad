package com.example.integra.notepad.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.integra.notepad.utils.Config;

public class SmsReceiver extends BroadcastReceiver {
    private MessageReceivedListener messageReceivedListener;
    private String phoneNo, smsCondition;
    private static final String TAG = "SmsReceiver";

    public SmsReceiver() {
        phoneNo = Config.PHONE_NO;
        smsCondition = Config.SMS_CONDITION;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

            String sender = "";
            String msgBody = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    sender = smsMessage.getDisplayOriginatingAddress();
                    msgBody += smsMessage.getMessageBody();

                }
                Log.d(TAG + Config.LOG_SEPERATOR, "Msg Body: " + msgBody);
            } else {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus == null) {
                        Log.d(TAG + Config.LOG_SEPERATOR, "SmsBundle had no pdus key");
                        return;
                    }
                    SmsMessage[] smsMessages = new SmsMessage[pdus.length];
                    for (int j = 0; j < smsMessages.length; j++) {
                        smsMessages[j] = SmsMessage.createFromPdu((byte[]) pdus[j]);
                        msgBody += smsMessages[j].getMessageBody();
                    }
                    sender = smsMessages[0].getDisplayOriginatingAddress();
                }
                Log.d(TAG + Config.LOG_SEPERATOR, "Msg Body: " + msgBody);
            }


            if (phoneNo.equals(sender) && msgBody.contains(smsCondition)) {
                int start = msgBody.indexOf(Config.SMS_CONDITION);
                int end = Config.SMS_CONDITION.length() + 5;
                String codes = msgBody.substring(start, start + end);
                Log.d(TAG + Config.LOG_SEPERATOR, "Code is: " + codes);
                Log.d(TAG + Config.LOG_SEPERATOR, "onMessageReceived is called");
                if (messageReceivedListener != null) {
                    messageReceivedListener.onMessageReceived(codes);
                }

            }
        }

    }

    public void addMessageReceivedListener(MessageReceivedListener listener) {
        this.messageReceivedListener = listener;
    }

    public interface MessageReceivedListener {
        void onMessageReceived(String message);
    }
}
