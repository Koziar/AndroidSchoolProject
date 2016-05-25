package com.cphbusiness.luke.androidschoolproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.mato.dsdomibyg.R;

public class Splash extends Activity {

    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long millisUntilFinished){}

            @Override
            public void onFinish(){
                Intent intent = new Intent(Splash.this, LoginActivity.class);
                Splash.this.startActivity(intent);
            }
        }.start();

        counter = 5;

//        /* adapt the image to the size of the display */
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
//                getResources(), R.mipmap.ic_logo), size.x, size.y, true);

//        ImageView image = (ImageView) findViewById(R.id.WelcomeImage);
//        image.setImageBitmap(bmp);
    }

    public int getCounter() {
        return counter;
    }
}
