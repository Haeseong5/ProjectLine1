package com.example.haeseong.projectline1.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.adapter.LargeViewPagerAdapter;

public class LargeProfileActivity extends AppCompatActivity {
    ViewPager viewPager;
    LargeViewPagerAdapter largeViewPagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_profile);
        viewPager = findViewById(R.id.large_view_pager);
        largeViewPagerAdapter = new LargeViewPagerAdapter(getApplicationContext());
        viewPager.setAdapter(largeViewPagerAdapter);
    }

}
