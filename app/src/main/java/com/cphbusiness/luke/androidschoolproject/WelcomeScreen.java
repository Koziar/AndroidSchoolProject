package com.cphbusiness.luke.androidschoolproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;

import com.example.mato.dsdomibyg.R;

public class WelcomeScreen extends Activity {

    public int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);


        counter = 5;

        /* adapt the image to the size of the display */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.mipmap.ic_logo), size.x, size.y, true);

        ImageView image = (ImageView) findViewById(R.id.WelcomeImage);
        image.setImageBitmap(bmp);


        image.setOnClickListener((v) -> {
            Intent intent = new Intent(WelcomeScreen.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
