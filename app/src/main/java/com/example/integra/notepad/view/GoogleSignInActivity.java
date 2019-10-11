package com.example.integra.notepad.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.integra.notepad.R;
import com.example.integra.notepad.utils.Config;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;

public class GoogleSignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SIGN_IN_REQUEST = 227;
    private SignInButton signInButton;
    private Button btnSignOut;
    private GoogleSignInClient googleSignInClient;
    private static final String TAG = "GSign" + Config.LOG_SEPERATOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);
        initViews();
        configureGoogleSignIn();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST && resultCode == Activity.RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSigInResult(task);

        }
    }

    private void googleSignOut() {
        Auth.GoogleSignInApi.signOut(googleSignInClient.asGoogleApiClient())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.d(TAG, "Status message: " + status.getStatusMessage());
                        Log.d(TAG, "Status code: " + status.getStatusCode());
                    }
                });


    }

    private void handleSigInResult(Task<GoogleSignInAccount> task) {
        try {

            GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
            updateUI(googleSignInAccount);
        } catch (Exception e) {
            e.printStackTrace();
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount googleSignInAccount) {
        Toast.makeText(getApplicationContext(), "EmailId: " + googleSignInAccount.getEmail(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Token:" + googleSignInAccount.getIdToken());
        Log.d(TAG, "ID:" + googleSignInAccount.getId());
        Log.d(TAG, "DisplayName:" + googleSignInAccount.getDisplayName());
        Log.d(TAG, "GivenName:" + googleSignInAccount.getGivenName());
        Log.d(TAG, "Email:" + googleSignInAccount.getEmail());


    }

    private void initViews() {
        signInButton = findViewById(R.id.sign_in_button);
        btnSignOut = findViewById(R.id.btn_signout);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
    }

    private void configureGoogleSignIn() {

        String clientId = getResources().getString(R.string.client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestIdToken(clientId)
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    private void googleSignIn() {

        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, SIGN_IN_REQUEST);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.sign_in_button:
                googleSignIn();
                break;
            case R.id.btn_signout:
                googleSignOut();
                break;

        }

    }
}
