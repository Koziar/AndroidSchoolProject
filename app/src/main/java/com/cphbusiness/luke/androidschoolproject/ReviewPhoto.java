package com.cphbusiness.luke.androidschoolproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mato.dsdomibyg.R;

/**
 * Created by Mato on 13.05.16.
 */
public class ReviewPhoto extends Activity {

    TakePhoto tp = new TakePhoto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_photo);

        ImageView preview = (ImageView) findViewById(R.id.imageView);

        preview.setImageURI(tp.getFileUri());

        Button back = (Button) findViewById(R.id.button2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tpIntent = new Intent(ReviewPhoto.this, TakePhoto.class);
                startActivity(tpIntent);
            }
        });

//        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!finnish this after new class is finished!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

//        Button ok = (Button) findViewById(R.id.button3);
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent okIntent = new Intent(ReviewPhoto.this,);
//                startActivity(okIntent);
//            }
//        });
    }


}
