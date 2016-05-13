package com.cphbusiness.luke.androidschoolproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mato.dsdomibyg.R;

public class LoginActivity extends Activity {

    private static String name;
    private static String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button loginButton = (Button) findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(v -> {

            final EditText nameField = (EditText) findViewById(R.id.userName);
            name = nameField.getText().toString();
            final EditText phoneField = (EditText) findViewById(R.id.userPhone);
            phone = phoneField.getText().toString();

            Intent intent = new Intent(this, TakePhoto.class);
            startActivity(intent);
        });
    }

    public static String getName() {
        return name;
    }

    public static String getPhone() {
        return phone;
    }

}