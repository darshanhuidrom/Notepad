package com.example.integra.notepad.view;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.integra.notepad.R;
import com.example.integra.notepad.receiver.OTPReceiver;
import com.example.integra.notepad.utils.Config;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class OtpActivity extends AppCompatActivity implements OTPReceiver.OtpRecievedInterface,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG="OtpActivity";
    private GoogleApiClient googleApiClient;
    private OTPReceiver otpReceiver;
    private int HINT_RESOLVER = 1;
    private EditText etMobileNo, etOTP;
    private Button btnGetOtp, btnVerifyOtp;
    private ConstraintLayout layoutInput, layoutVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        initViews();
        otpReceiver = new OTPReceiver();
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
        otpReceiver.setOnOtpListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(otpReceiver, intentFilter);
        getHintPhoneNo();
        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSmsListener();
            }
        });
    }

    private void startSmsListener() {
        SmsRetrieverClient client=SmsRetriever.getClient(this);
        Task<Void> task =client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                layoutInput.setVisibility(View.GONE);
                layoutVerify.setVisibility(View.VISIBLE);
                Toast.makeText(OtpActivity.this, "SMS Retriever starts", Toast.LENGTH_LONG).show();

            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OtpActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getHintPhoneNo() {
        Log.d(TAG+ Config.LOG_SEPERATOR,"getHintPhoneNo");
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true).build();
        PendingIntent pendingIntent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);
        try {
            startIntentSenderForResult(pendingIntent.getIntentSender(),HINT_RESOLVER,null,0,0,0);
        } catch (Exception e) {

            e.getStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG+ Config.LOG_SEPERATOR,"onActivityResult");
        if(requestCode==HINT_RESOLVER&&resultCode== Activity.RESULT_OK&&data!=null){
            Credential credential=data.getParcelableExtra(Credential.EXTRA_KEY);
            Log.d(TAG+ Config.LOG_SEPERATOR,credential.getId());
            etMobileNo.setText(credential.getId());
        }
    }

    private void initViews() {
        etMobileNo = findViewById(R.id.editTextInputMobile);
        etOTP = findViewById(R.id.editTextOTP);
        btnGetOtp = findViewById(R.id.buttonGetOTP);
        btnVerifyOtp = findViewById(R.id.buttonVerify);
        layoutInput = findViewById(R.id.getOTPLayout);
        layoutVerify = findViewById(R.id.verifyOTPLayout);
    }

    @Override
    public void onOtpReceived(String otp) {
        Toast.makeText(this, "Otp Received " + otp, Toast.LENGTH_LONG).show();
        etOTP.setText(otp);
    }

    @Override
    public void onOtpTimeOut() {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
