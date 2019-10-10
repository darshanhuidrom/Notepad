package com.example.integra.notepad.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.integra.notepad.MyApplication;
import com.example.integra.notepad.R;
import com.example.integra.notepad.receiver.SmsReceiver;
import com.example.integra.notepad.utils.Config;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SmsReceiverActivity extends AppCompatActivity {

    private EditText etOtp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_receiver);
        etOtp=findViewById(R.id.et_otp);
        if (!isSmsPermissionGranted()){
            showRequestPermissionsInfoAlertDialog(true);
        }

        MyApplication.getSmsReceiver().addMessageReceivedListener(new SmsReceiver.MessageReceivedListener() {
            @Override
            public void onMessageReceived(String message) {
                String code = message.replace(Config.SMS_CONDITION,"");
                Toast.makeText(getApplicationContext(),code,Toast.LENGTH_SHORT).show();
                etOtp.setText(code);
                etOtp.setSelection(etOtp.getText().length());
            }
        });

    }


    /***
     * check we have sms permission*/
    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isReadPhonePermissionGranted(){
        return ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)==PackageManager.PERMISSION_GRANTED;
    }

    /***
     * Request runtime sms permission*/

    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
            // You may display a non-blocking explanation here, read more in the documentation:
            // https://developer.android.com/training/permissions/requesting.html
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, Config.SMS_PERMISSION_CODE);
    }

    /***
     * To show permission on runtime*/
    public void showRequestPermissionsInfoAlertDialog(final boolean makeSystemRequest) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_alert_dialog_title); // Your own title
        builder.setMessage(R.string.permission_dialog_message); // Your own message

        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Display system runtime permission request?
                if (makeSystemRequest) {
                    requestReadAndSendSmsPermission();
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Config.SMS_PERMISSION_CODE:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }
                else {

                }

        }
    }
}
