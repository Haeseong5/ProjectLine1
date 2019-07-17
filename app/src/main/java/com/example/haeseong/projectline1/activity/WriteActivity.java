package com.example.haeseong.projectline1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.data.Post;
import com.example.haeseong.projectline1.data.UserData;
import com.example.haeseong.projectline1.helper.GlobalUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteActivity extends AppCompatActivity {
    private static final String TAG = "WriteActivityLog";
    EditText etTitle;
    ImageView ivBackButton;
    Button btFinish;
    EditText etContent;
    int position = -1;
    GlobalUser globalUser;
    String [] boardId = new String[5];

    FirebaseUser user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        globalUser = GlobalUser.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        etTitle = findViewById(R.id.write_title);
        ivBackButton = findViewById(R.id.write_back_button);
        btFinish = findViewById(R.id.write_finish);
        etContent = findViewById(R.id.write_content);

       boardId[0] = "free_board";
       boardId[1] = "study_board";
       boardId[2] = "univ_board";
       boardId[3] = "market_board";

        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent =  new Intent(this.getIntent());

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
                String time = format1.format(date);
                String title = etTitle.getText().toString();
                String content = etContent.getText().toString();
                String uid = user.getUid();
                writePost_fireStore(title, content, uid, name, time, position);

            }
        });
    }
    protected void writePost_fireStore(String title, String content, String uid, String writer, String time, int position){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUid(uid);
        post.setWriter(writer);
        post.setTimeStamp(time);

        db.collection(boardId[position])
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        println("게시물 작성 성공");
                        Intent intent = new Intent(WriteActivity.this, PostActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        println("게시물 작성 실패");
                    }
                });
    }
    void println(String message){
        Log.d(TAG, message);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}
