package com.example.integra.notepad.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.integra.notepad.BuildConfig;
import com.example.integra.notepad.R;
import com.example.integra.notepad.utils.Config;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.FileNotFoundException;

public class PictureBarcodeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private Button btnOpenCamera;
    private TextView tvResultHeader, tvResultBody;
    private BarcodeDetector barcodeDetector;
    private static final int CAMERA_REQUEST_PERMISSION = 112;
    private Uri imageUri;
    private static final int CAMERA_REQUEST = 113;
    private static final String TAG = "Picturect" + Config.LOG_SEPERATOR;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_barcode);
        intViews();

        if (savedInstanceState != null) {
            if (imageUri != null) {
                imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));
                tvResultBody.setText(savedInstanceState.getString(SAVED_INSTANCE_RESULT));
            }
        }
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE).build();

        if (!barcodeDetector.isOperational()) {
            tvResultBody.setText("Initialization Failed.");
            return;
        }


    }

    private void intViews() {
        imageView = findViewById(R.id.imageView);
        tvResultHeader = findViewById(R.id.txtResultsHeader);
        tvResultBody = findViewById(R.id.txtResultsBody);
        btnOpenCamera = findViewById(R.id.btnOpenCamera);
        btnOpenCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    takeBarCodePicture();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if(imageUri!=null){
            outState.putString(SAVED_INSTANCE_URI,imageUri.toString());
            outState.putString(SAVED_INSTANCE_RESULT,tvResultBody.getText().toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            launcheMediaScanIntent();
            try {
                Bitmap bitmap = decodeBitmapUri(this, imageUri);
                if (barcodeDetector.isOperational() && bitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);
                    for (int i = 0; i < barcodes.size(); i++) {
                        Barcode barcode = barcodes.valueAt(i);
                        tvResultBody.setText(tvResultBody.getText() + "\n" + barcode.displayValue + "\n");
                        int type = barcode.valueFormat;
                        logBarcodeType(type, barcode);
                    }
                    if(barcodes.size()==0){
                        tvResultBody.setText("No barcode could be detected.Please try again.");
                    }
                    else {
                        tvResultBody.setText("Detect initialization failed");
                    }

                }


            } catch (Exception e) {
                e.getStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
                Log.e(TAG, e.toString());

            }
        }
    }

    private void logBarcodeType(int type, Barcode code) {
        switch (type) {

            case Barcode.CONTACT_INFO:
                Log.i(TAG, code.contactInfo.title);
                break;
            case Barcode.EMAIL:
                Log.i(TAG, code.displayValue);
                break;
            case Barcode.ISBN:
                Log.i(TAG, code.rawValue);
                break;
            case Barcode.PHONE:
                Log.i(TAG, code.phone.number);
                break;
            case Barcode.PRODUCT:
                Log.i(TAG, code.rawValue);
                break;
            case Barcode.SMS:
                Log.i(TAG, code.sms.message);
                break;
            case Barcode.TEXT:
                Log.i(TAG, code.displayValue);
                break;
            case Barcode.URL:
                Log.i(TAG, "url: " + code.displayValue);
                break;
            case Barcode.WIFI:
                Log.i(TAG, code.wifi.ssid);
                break;
            case Barcode.GEO:
                Log.i(TAG, code.geoPoint.lat + ":" + code.geoPoint.lng);
                break;
            case Barcode.CALENDAR_EVENT:
                Log.i(TAG, code.calendarEvent.description);
                break;
            case Barcode.DRIVER_LICENSE:
                Log.i(TAG, code.driverLicense.licenseNumber);
                break;
            default:
                Log.i(TAG, code.rawValue);
                break;

        }

    }

    private Bitmap decodeBitmapUri(Context context, Uri imageUri) {
        int targetW=600;
        int targetH=600;
        BitmapFactory.Options bmOptions=new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds=true;
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri),null,bmOptions);
            int photoW=bmOptions.outWidth;
            int photoH=bmOptions.outHeight;
            int scaleFactor= Math.min(photoW/targetW,photoH/targetH);
            bmOptions.inJustDecodeBounds=false;
            bmOptions.inSampleSize=scaleFactor;
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri),null,bmOptions);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void launcheMediaScanIntent() {

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(imageUri);
        this.sendBroadcast(intent);
    }

    private void takeBarCodePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo=new File(Environment.getExternalStorageDirectory(),"pic.jpg");
        imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".provider",photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,CAMERA_REQUEST);
    }
}
