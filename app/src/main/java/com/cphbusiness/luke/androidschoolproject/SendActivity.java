package com.cphbusiness.luke.androidschoolproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.example.mato.dsdomibyg.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class SendActivity extends Activity {

    // From Dropbox account I created using my email: koz.luk@protonmail.ch, password: nsdomibyg1111
    // app name: NSDomibyg
    static DropboxAPI<AndroidAuthSession> dropboxAPI;
    private static final String APP_KEY = "68okzghak0jlx8e";
    private static final String APP_SECRET = "yel5utqc01prwak";
    private static final String ACCESSTOKEN = "VQp1oWrVDsAAAAAAAAAAB8hcszBhmPmO77vkQ2yq_t_PBVtBfwAPT56RnSYgrwQg";
    private DropboxAPI.UploadRequest request;

    private ArrayList<String> data;
    private GPSTracker gps;


    private Button sendButton;
    private EditText descriptionField;
//    private TextView testTextView; // to be deleted later

    private String description;
    private String userName;
    private String userPhone;
    private String linkToGoogleMaps;
    private String address;

    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/toDropbox";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        Button sendButton = (Button) findViewById(R.id.send_button);

        final EditText descriptionField = (EditText) findViewById(R.id.description_textfield);
        AndroidAuthSession session = buildSession();
        dropboxAPI = new DropboxAPI<AndroidAuthSession>(session);

        // Populating the spinner, in further version to be replaced with real names of folders from dropbox account
        Spinner spinner = (Spinner) findViewById(R.id.spinner_addresses);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.addresses_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

//        Spinner spinner = (Spinner) findViewById(R.id.spinner_addresses);
//        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, xxxxxxxxx ));


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationManager locationManager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);

                // check if location services is enabled
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    SendActivity.this.buildAlertMessageNoGps();
                }

                gps = new GPSTracker(SendActivity.this);

                description = descriptionField.getText().toString();
                userName = LoginActivity.getName();
                userPhone = LoginActivity.getPhone().toString();
                Spinner sp = (Spinner) findViewById(R.id.spinner_addresses);
                address = sp.getSelectedItem().toString();

                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    linkToGoogleMaps = "https://www.google.com/maps/place/" + latitude + "," + longitude;
//                            Toast.makeText(getApplicationContext(),
//                                    "Your location is: \nLat: " + latitude + "\nLong: " + longitude,
//                                    Toast.LENGTH_LONG).show();
                } else {
                    gps.showSettingsAlert();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            String dataToTxtFile =
                                    "Name of employee: " + userName + "\n" +
                                            "Phone number: " + userPhone + "\n" +
                                            "Description: " + description + "\n" +
                                            "Place: " + address + "\n" +
                                            "Picture taken at: " + linkToGoogleMaps;
                            try {
                                File fileTxt = new File(createTxtFile(dataToTxtFile));
                                FileInputStream inputStream1 = new FileInputStream(fileTxt);
                                DropboxAPI.Entry responseTxt = dropboxAPI.putFile("/ROOT/" + address + "/" + TakePhoto.getTimeStamp() + ".txt", inputStream1,
                                        fileTxt.length(), null, null);

                                File fileJpg = new File(TakePhoto.getmCurrentPhotoPath());
                                FileInputStream inputStream2 = new FileInputStream(fileJpg);
                                DropboxAPI.Entry responseJpg = dropboxAPI.putFile("/ROOT/" + address + "/" + TakePhoto.getTimeStamp() + ".jpg", inputStream2,
                                        fileJpg.length(), null, null);

                                Log.i("DbExampleLog", "The uploaded file's rev is: " + responseTxt.rev);
                                Log.i("DbExampleLog", "The uploaded file's rev is: " + responseJpg.rev);

                            } catch (IOException | DropboxException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

                Intent intent = new Intent(SendActivity.this, TakePhoto.class);
                SendActivity.this.startActivity(intent);
            }
        });


        // ... In progress ...

    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        session.setOAuth2AccessToken(ACCESSTOKEN);
        return session;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        SendActivity.this.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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

    private String createTxtFile(String data) throws IOException {
        String imageFileName = TakePhoto.getTimeStamp();
        String filePath = this.getFilesDir().getPath().toString() + "/" + imageFileName + ".txt";
        File logFile = new File(filePath);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(data);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return logFile.getPath();
    }

}
