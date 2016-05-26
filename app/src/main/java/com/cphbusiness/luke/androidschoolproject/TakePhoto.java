package com.cphbusiness.luke.androidschoolproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

    Splash ws = new Splash();

    private static String mCurrentPhotoPath;
    private static String timeStamp;
    private TextView tvUserName, cnt;
    private SharedPreferences loginPrefs;
    private String userName;

    private final static String USERNAME_KEY = "username";
    private final static String SAVED_KEY = "saved";

    private boolean isSaved = false;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        init();
        isSaved = loginPrefs.getBoolean(SAVED_KEY, false);
        userName = loginPrefs.getString(USERNAME_KEY, "");
        if (isSaved) {
            tvUserName.setText(userName);
        }

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String counter = String.valueOf(ws.getCounter());
        cnt.setText(counter);

        Button photoButton = (Button) findViewById(R.id.button);
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
        tvUserName = (TextView) findViewById(R.id.receivedUserName);
        cnt = (TextView) findViewById(R.id.textViewCounter);

    }


    private File createImageFile() throws IOException {
        // Create an image file name

        String imageFileName = timeStamp;
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


    public static String getTimeStamp() {
        return timeStamp;
    }

    public static String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

}