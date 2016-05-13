package com.cphbusiness.luke.androidschoolproject;

import android.app.Activity;
import android.os.Bundle;
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
        //getIntent().
        //preview.setImageURI();

    }




}
