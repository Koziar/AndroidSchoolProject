package com.cphbusiness.luke.androidschoolproject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.example.mato.dsdomibyg.R;

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

    private Button sendButton;
    private EditText descriptionField;
    private TextView testTextView; // to be deleted later

    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        sendButton = (Button) findViewById(R.id.send_button);
        descriptionField = (EditText) findViewById(R.id.description_textfield);

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

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(SendActivity.this);

                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    String linkToGoogleMaps = "https://www.google.com/maps/place/" + latitude + "," + longitude;
                    testTextView = (TextView) findViewById(R.id.testTextView);
                    testTextView.setTextColor(Color.BLACK);
                    testTextView.setText(Html.fromHtml("<a href=" + linkToGoogleMaps + ">See in Google Maps</a> "));
                    Toast.makeText(getApplicationContext(),
                            "Your location is: \nLat: " + latitude + "\nLong: " + longitude,
                            Toast.LENGTH_LONG).show();
                } else {
                    gps.showSettingsAlert();
                }
            }
        });

    } // end of onCreate

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        session.setOAuth2AccessToken(ACCESSTOKEN);
        return session;
    }
}
