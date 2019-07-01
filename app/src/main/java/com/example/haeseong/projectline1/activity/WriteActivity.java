package com.example.haeseong.projectline1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.helper.GlobalUser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteActivity extends AppCompatActivity {
    EditText etTitle;
    ImageView ivBackButton;
    Button btFinish;
    EditText etContent;
    int position = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        etTitle = findViewById(R.id.write_title);
        ivBackButton = findViewById(R.id.write_back_button);
        btFinish = findViewById(R.id.write_finish);
        etContent = findViewById(R.id.write_content);
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        if(intent != null){
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            position = intent.getIntExtra("position",-1);
            etTitle.setText(title);
            etContent.setText(content);
        }
        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                GlobalUser globalUser = GlobalUser.getInstance();
                String name = globalUser.getName();
                String email = globalUser.getEmail();
                String time = format1.format(date);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("title",etTitle.getText().toString());
                resultIntent.putExtra("content",etContent.getText().toString());
                resultIntent.putExtra("position",position);
                resultIntent.putExtra("time",time);
                if(email != "" && email != null){
                    resultIntent.putExtra("name",email);
                }else if(name != null && name != ""){
                    resultIntent.putExtra("name",name);
                }
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });
    }
}
