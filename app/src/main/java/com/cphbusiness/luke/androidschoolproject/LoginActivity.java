package com.cphbusiness.luke.androidschoolproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.example.mato.dsdomibyg.R;

public class LoginActivity extends Activity {

    private static String name;
    private static String phone;

    public static DropboxAPI<AndroidAuthSession> dropboxAPI;
    private static final String APP_KEY = "68okzghak0jlx8e";
    private static final String APP_SECRET = "yel5utqc01prwak";
    private static final String ACCESSTOKEN = "VQp1oWrVDsAAAAAAAAAAB8hcszBhmPmO77vkQ2yq_t_PBVtBfwAPT56RnSYgrwQg";
    private DropboxAPI.UploadRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AndroidAuthSession session = buildSession();
        dropboxAPI = new DropboxAPI<AndroidAuthSession>(session);

        new NetworkOperations().execute("/ROOT/");

        Button loginButton = (Button) findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText nameField = (EditText) LoginActivity.this.findViewById(R.id.userName);
                name = nameField.getText().toString();
                if(name.matches("")){
                    Toast.makeText(getApplicationContext(), "Enter your name",Toast.LENGTH_LONG ).show();
                    return;
                }
                final EditText phoneField = (EditText) LoginActivity.this.findViewById(R.id.userPhone);
                phone = phoneField.getText().toString();
                if(phone.matches("")){
                    Toast.makeText(getApplicationContext(), "Enter your telephone number",Toast.LENGTH_LONG ).show();
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, TakePhoto.class);
                LoginActivity.this.startActivity(intent);
            }
        });
    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        session.setOAuth2AccessToken(ACCESSTOKEN);
        return session;
    }

    public static String getName() {
        return name;
    }

    public static String getPhone() {
        return phone;
    }

}