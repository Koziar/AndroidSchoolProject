package com.cphbusiness.luke.androidschoolproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mato.dsdomibyg.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhoto extends Activity {

    WelcomeScreen ws = new WelcomeScreen();
    LoginActivity la;

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private Uri fileUri;
    private static String name, telNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        TextView cnt = (TextView) findViewById(R.id.textViewCounter);


        String counter = String.valueOf(ws.getCounter());
        cnt.setText(counter);


        name = la.getName();
        telNo = la.getPhone();

//!!!!!!!!!!!!!!!!!do not forger to move this to correct class since this would be buggy!!!!!!!!!!!!!!!!!!!
        Button photoButton = (Button) findViewById(R.id.button);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                photoInt.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name


                TakePhoto.this.startActivityForResult(photoInt, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */


    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }


// Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    timeStamp + "_" + name + ".jpg");
        } else {
            return null;
        }

        return mediaFile;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved " + getFileUri()
                        , Toast.LENGTH_LONG).show();
                Intent review = new Intent(TakePhoto.this, ReviewPhoto.class);
                startActivity(review);

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

    }

    public Uri getFileUri() {
        return fileUri;
    }
}