package com.cphbusiness.luke.androidschoolproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class SendActivity extends Activity {


    static DropboxAPI<AndroidAuthSession> dropboxAPI = LoginActivity.dropboxAPI;
    private static final String APP_KEY = "68okzghak0jlx8e";
    private static final String APP_SECRET = "yel5utqc01prwak";
    private static final String ACCESSTOKEN = "VQp1oWrVDsAAAAAAAAAAB8hcszBhmPmO77vkQ2yq_t_PBVtBfwAPT56RnSYgrwQg";
    private DropboxAPI.UploadRequest request;

    private GPSTracker gps;

    private Button sendButton;
    private EditText descriptionField;

    private String description, userName, userPhone, linkToGoogleMaps, address, fileName;
    private static ArrayList<String> directories = new ArrayList<>();

    private SharedPreferences loginPrefs;
    private final static String USERNAME_KEY = "username";
    private final static String USERPHONE_KEY = "userphone";
    private final static String COUNTER_KEY = "counter";
    private final static String SAVED_KEY = "saved";
    private boolean isSaved = false;
    private SharedPreferences counterControlPrefs;
    private SharedPreferences.Editor counterEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        init();

        sendButton = (Button) findViewById(R.id.send_button);

        descriptionField = (EditText) findViewById(R.id.description_textfield);
        AndroidAuthSession session = buildSession();
        dropboxAPI = new DropboxAPI<AndroidAuthSession>(session);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_addresses);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, directories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationManager locationManager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);

                // check if location services is enabled
//                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    SendActivity.this.buildAlertMessageNoGps();
//                }

                gps = new GPSTracker(SendActivity.this);

                description = descriptionField.getText().toString();
                userName = loginPrefs.getString(USERNAME_KEY, "");
                userPhone = loginPrefs.getString(USERPHONE_KEY, "");
                Spinner sp = (Spinner) findViewById(R.id.spinner_addresses);
                address = sp.getSelectedItem().toString();

                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    linkToGoogleMaps = "https://www.google.com/maps/place/" + latitude + "," + longitude;


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (this) {
                                String dataToTxtFile =
                                        "Name of employee: " + userName + "\n" +
                                                "Phone number: " + userPhone + "\n" +
                                                "Description: " + description + "\n" +
                                                "Place: " + address + "\n" +
                                                "Date: " + dateStamp() + "\n" +
                                                "Picture taken at: " + linkToGoogleMaps;


                                try {
                                    fileName = TakePhoto.getTimeStamp();

                                    File fileTxt = new File(createTxtFile(dataToTxtFile));
                                    FileInputStream inputStream1 = new FileInputStream(fileTxt);
                                    DropboxAPI.Entry responseTxt = dropboxAPI.putFile("/ROOT/" + address
                                                    + "/" + fileName + "_" + userName + ".txt", inputStream1,
                                            fileTxt.length(), null, null);

                                    File fileJpg = new File(TakePhoto.getmCurrentPhotoPath());
                                    FileInputStream inputStream2 = new FileInputStream(fileJpg);
                                    DropboxAPI.Entry responseJpg = dropboxAPI.putFile("/ROOT/" + address
                                                    + "/" + fileName + "_" + userName + ".jpg", inputStream2,
                                            fileJpg.length(), null, null);

                                    int counter = counterControlPrefs.getInt(COUNTER_KEY, 0);
                                    if (counter > 0) {
                                        --counter;
                                        counterEditor.putInt(COUNTER_KEY, counter);
                                        counterEditor.commit();
                                    }

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

                } else {
                    gps.showSettingsAlert();
                }
            }
        });

    }

    private String dateStamp() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();

        return df.format(today);
    }

    private void init() {
        loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        counterControlPrefs = getSharedPreferences("counterControlPrefs", MODE_PRIVATE);
        counterEditor = counterControlPrefs.edit();

    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        session.setOAuth2AccessToken(ACCESSTOKEN);
        return session;
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

    public void setDirectories(ArrayList<String> directories) {
        this.directories = directories;
    }
}