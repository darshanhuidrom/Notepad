package com.example.integra.notepad.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.integra.notepad.R;

public class BarcodeActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnTakePic, btnScanBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        initViews();

    }

    private void initViews() {
        btnTakePic = findViewById(R.id.btnTakePicture);
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        btnTakePic.setOnClickListener(this);
        btnScanBarcode.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnTakePicture:
                startActivity(this, PictureBarcodeActivity.class);
                break;
            case R.id.btnScanBarcode:
                startActivity(this, ScanBarcodeActivity.class);

        }
    }

    private void startActivity(Context currentActivity, Class<?> destinationActivity) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        startActivity(intent);
    }
}
