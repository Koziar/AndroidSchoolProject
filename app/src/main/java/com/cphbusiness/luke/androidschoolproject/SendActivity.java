package com.cphbusiness.luke.androidschoolproject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.example.mato.dsdomibyg.R;

public class SendActivity extends Activity {

    // From Dropbox account I created using my email: koz.luk@protonmail.ch, password: nsdomibyg1111
    // app name: NSDomibyg
    static DropboxAPI<AndroidAuthSession> dropboxAPI;
    private static final String APP_KEY = "68okzghak0jlx8e";
    private static final String APP_SECRET = "yel5utqc01prwak";
    private static final String ACCESSTOKEN = "VQp1oWrVDsAAAAAAAAAAB8hcszBhmPmO77vkQ2yq_t_PBVtBfwAPT56RnSYgrwQg";
    private DropboxAPI.UploadRequest request;

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        session.setOAuth2AccessToken(ACCESSTOKEN);
        return session;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        Button sendButton = (Button) findViewById(R.id.send_button);
        final EditText descriptionField = (EditText) findViewById(R.id.description_textfield);
        descriptionField.getText().toString();

        AndroidAuthSession session = buildSession();
        dropboxAPI = new DropboxAPI<AndroidAuthSession>(session);

        // ...
    }

}
