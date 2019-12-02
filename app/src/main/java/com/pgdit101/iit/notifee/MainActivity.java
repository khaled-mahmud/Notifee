package com.pgdit101.iit.notifee;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.regex.Pattern;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{


    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    //private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //scannerView = new ZXingScannerView(this);
        scannerView = (ZXingScannerView)(findViewById(R.id.zxscan));
        //button = findViewById(R.id.button);
        //scannerView.addView(scannerView);
        //setContentView(scannerView);
        //setContentView(R.layout.activity_main);

        /*ZXingScannerView mscannerView = (ZXingScannerView) findViewById(R.id.zxscan);
        scannerView = new ZXingScannerView(this); // Programmatically initialize the scanner view
        scannerView.addView(mscannerView);
        setContentView(scannerView);*/



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                Toast.makeText(MainActivity.this, "Permission is Granted", Toast.LENGTH_LONG).show();
            }else{
                requestPermission();
            }
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(MainActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(scannerView == null) {
                    //scannerView = new ZXingScannerView(this);
                    scannerView = (ZXingScannerView)(findViewById(R.id.zxscan));
                    //setContentView(scannerView);
                    setContentView(R.layout.activity_main);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public void handleResult(Result result) {
        final String myResult = result.getText();
        //final String myContent = result.getBarcodeFormat().toString();
        //final String getContents = result.get;
        Log.wtf("QRCodeScanner", result.getText());
        Log.wtf("QRCodeScanner", result.getBarcodeFormat().toString());


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(MainActivity.this);
            }
        });


        if(Pattern.matches("[0-9]{1,13}", myResult)) {
            builder.setNeutralButton("Save Product", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent upcIntent = new Intent(MainActivity.this, AddProduct.class);
                    upcIntent.putExtra("isBarCodeScan", true);
                    upcIntent.putExtra("upc_code", myResult);
                    startActivity(upcIntent);
                }
            });
        } else {
            builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myResult));
                    startActivity(browserIntent);
                }
            });
        }


        /*builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myResult));
                startActivity(browserIntent);
            }
        });*/
        builder.setMessage(result.getText());
        //builder.setMessage(result.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();

    }


    public void addProduct(View view){
        {
            Intent intent = new Intent(this, AddProduct.class);
            startActivity(intent);
        }
    }
}
