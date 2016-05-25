package com.cphbusiness.luke.androidschoolproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mato.dsdomibyg.R;

public class LoginActivity extends Activity {

    private static String name;
    private static String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

    public static String getName() {
        return name;
    }

    public static String getPhone() {
        return phone;
    }

}