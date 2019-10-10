package com.example.integra.notepad.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.integra.notepad.R;

public class EmailActivity extends AppCompatActivity {


    private Button btnSendEmail;
    private EditText etSubject;
    private EditText etBody;
    private TextView tvEmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        initViews();
        if(getIntent().getStringExtra("email_address")!=null){
            tvEmailAddress.setText("Recipient: "+getIntent().getStringExtra("email_address"));
        }

    }

    private void initViews() {
        btnSendEmail= findViewById(R.id.btnSendEmail);
        etSubject=findViewById(R.id.inSubject);
        etBody=findViewById(R.id.inBody);
        tvEmailAddress=findViewById(R.id.txtEmailAddress);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{tvEmailAddress.getText().toString()});
                intent.putExtra(Intent.EXTRA_SUBJECT,etSubject.getText().toString().trim());
                intent.putExtra(Intent.EXTRA_TEXT,etBody.getText().toString());
                startActivity(intent);
            }
        });

    }
}
