package com.example.haeseong.projectline1.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.fragment.FragmentBoard;
import com.example.haeseong.projectline1.fragment.FragmentFind;
import com.example.haeseong.projectline1.fragment.FragmentChat;
import com.example.haeseong.projectline1.fragment.FragmentHome;
import com.example.haeseong.projectline1.fragment.FragmentProfile;
import com.example.haeseong.projectline1.helper.BottomNavigationHelper;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    FragmentManager fragmentManager = getSupportFragmentManager();     // Activity 내의 Fragment를 관리하기 위해서는 FragmentManager를 사용
    Fragment fragment1, fragment2, fragment3, fragment4;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

    public Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        setBottomNavigationView();
        context = this;
        fragment1 = new FragmentHome();
        fragment2 = new FragmentBoard();
        fragment3 = new FragmentChat();
        fragment4 = new FragmentProfile();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); //Fragment의 추가, 제거, 변경 등의 작업
        fragmentTransaction.add(R.id.main_content, fragment1).commit(); //첫 프래그먼트 지정
    } //end onCreate

    protected  void setToolbar()
    {
        toolbar = findViewById(R.id.toolbar);
    }
    protected void setBottomNavigationView()
    {
        bottomNavigationView = findViewById(R.id.main_bottom_navigation_view);
        // BottomNavigationView 메뉴를 선택할 때마다 위치가 변하지 않도록
        BottomNavigationHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_menu1: {
                        transaction.replace(R.id.main_content, fragment1).addToBackStack(null).commit();
                        break;
                    }
                    case R.id.navigation_menu2: {
                        transaction.replace(R.id.main_content, fragment2).addToBackStack(null).commit();
                        break;
                    }
                    case R.id.navigation_menu3: {
                        transaction.replace(R.id.main_content, fragment3).addToBackStack(null).commit();
                        break;
                    }
                    case R.id.navigation_menu4: {
                        transaction.replace(R.id.main_content, fragment4).addToBackStack(null).commit();
                        break;
                    }
                }
                return true;
            }
        });
    }
    void println(String message){
        Log.d(TAG, message);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}
