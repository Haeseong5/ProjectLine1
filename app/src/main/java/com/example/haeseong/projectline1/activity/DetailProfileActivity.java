package com.example.haeseong.projectline1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.haeseong.projectline1.R;

public class DetailProfileActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);
        imageView = findViewById(R.id.detail_profile_image);
        Intent intent = getIntent();
        if(intent!=null){
            String uri = intent.getStringExtra("image");
            Glide.with(getApplicationContext()).load(uri).into(imageView);
        }
    }
}
