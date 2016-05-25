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

<<<<<<< Updated upstream
    private static String name;
    private static String phone;
=======
    private static String userName, userPhone;
    private EditText etUserName, etUserPhone;
    private Button btnLogin;
    private CheckBox cbRememberMe;
    private SharedPreferences loginPrefs;
    private SharedPreferences.Editor loginEditor;
    private final static String USERNAME_KEY = "username";
    private final static String USERPHONE_KEY = "userphone";
    private final static String SAVED_KEY = "saved";
    private Intent logIntent;
    private boolean isSaved = false;
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
        new NetworkOperations().execute("/ROOT/");
=======
        if (cbRememberMe.isChecked()) {
            isSaved = true;
            saveData(userName, userPhone);
        } else {
            loginEditor.clear();
            loginEditor.commit();
            isSaved = false;
        }

        return isSaved;
    }

    private void saveData(String userName, String userPhone) {

        loginEditor.putString(USERNAME_KEY, userName);
        loginEditor.putString(USERPHONE_KEY, userPhone);
        loginEditor.putBoolean(SAVED_KEY, isSaved);
        loginEditor.commit();
    }

    private void init() {

        btnLogin = (Button) findViewById(R.id.buttonLogin);
        etUserName = (EditText) findViewById(R.id.userName);
        etUserPhone = (EditText) findViewById(R.id.userPhone);
        cbRememberMe = (CheckBox) findViewById(R.id.checkBox);

        loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginEditor = loginPrefs.edit();


        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonLogin) {

            userName = etUserName.getText().toString();
            userPhone = etUserPhone.getText().toString();
            checkedSaveOption(userName, userPhone);
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
    public static String getName() {
        return name;
    }
=======
    public static String getUserName() {
        return userName;
    }

>>>>>>> Stashed changes

    public static String getPhone() {
        return phone;
    }
}

