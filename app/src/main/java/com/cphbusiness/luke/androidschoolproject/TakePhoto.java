package com.cphbusiness.luke.androidschoolproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mato.dsdomibyg.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhoto extends Activity {

    private static String mCurrentPhotoPath, timeStamp;
    private TextView tvUserName, tvCounter;
    private String userName, imageFileName;

    private int counter;

    Button photoButton;
    LocationManager locationManager;

    private SharedPreferences loginPrefs;
    private SharedPreferences counterControlPrefs;
    private SharedPreferences.Editor counterEditor;

    private final static String USERNAME_KEY = "username";
    private final static String COUNTER_KEY = "counter";
    private final static String SAVED_KEY = "saved";

    private boolean isUserSaved = false;
    private boolean isCounterCreated = false;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        init();

        if (!isCounterCreated) {
            counter = 5;
            counterEditor.putInt(COUNTER_KEY, counter);
            counterEditor.commit();
            isCounterCreated = true;
        }
        tvCounter.setText("" + counterControlPrefs.getInt(COUNTER_KEY, 0));

        // check if location services is enabled
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            TakePhoto.this.buildAlertMessageNoGps();
        }

        isUserSaved = loginPrefs.getBoolean(SAVED_KEY, false);
        userName = loginPrefs.getString(USERNAME_KEY, "");
        if (isUserSaved) {
            tvUserName.setText(userName);
        } else {
            tvUserName.setText(LoginActivity.getUserName());
        }

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        photoButton = (Button) findViewById(R.id.button);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File

                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }

            }
        });
    }

    private void init() {
        loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        counterControlPrefs = getSharedPreferences("counterControlPrefs", MODE_PRIVATE);
        counterEditor = counterControlPrefs.edit();
        tvUserName = (TextView) findViewById(R.id.receivedUserName);
        tvCounter = (TextView) findViewById(R.id.tvCounter);
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
    }


    private File createImageFile() throws IOException {
        // Create an image file name

        imageFileName = timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Intent intent = new Intent(TakePhoto.this, ReviewPhoto.class);
            intent.putExtra("path", mCurrentPhotoPath);
            startActivity(intent);
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        TakePhoto.this.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public static String getTimeStamp() {
        return timeStamp;
    }

    public static String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

}