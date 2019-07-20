package com.example.haeseong.projectline1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.data.Post;
import com.example.haeseong.projectline1.helper.GlobalUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.haeseong.projectline1.activity.PostActivity.UPDATE_CODE;
import static com.example.haeseong.projectline1.activity.PostActivity.WRITE_CODE;

public class WriteActivity extends AppCompatActivity {
    private static final String TAG = "WriteActivityLog";
    private int requestCode;
    EditText etTitle;
    ImageView ivBackButton;
    Button btFinish;
    EditText etContent;
    int position = -1;
    GlobalUser globalUser;
    String [] boardId = new String[5];
    FirebaseFirestore db;
    int i;
    String docID;
    FirebaseUser user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        globalUser = GlobalUser.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
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
            requestCode = intent.getIntExtra("REQUEST_CODE",-1);
            println(String.valueOf(requestCode));
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            position = intent.getIntExtra("position",-1);
            println(String.valueOf(position));
            i = intent.getIntExtra("i",-1);
            docID = intent.getStringExtra("docID");
//            println(docID);
            etTitle.setText(title);
            etContent.setText(content);
        }
        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requestCode == WRITE_CODE){ //글 작성하기를 선택한 경우
                    SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    GlobalUser globalUser = GlobalUser.getInstance();
                    String name = globalUser.getName();
                    String time = format1.format(date);
                    String title = etTitle.getText().toString();
                    String content = etContent.getText().toString();
                    String uid = user.getUid();
                    writePost_fireStore(title, content, uid, name, time, position);
                } else if(requestCode == UPDATE_CODE){ //수정하려는 경우는 제목과 내용만 변경
                    String title = etTitle.getText().toString();
                    String content = etContent.getText().toString();
                    updatePost_fireStore(title, content, docID);
                }


            }
        });
    }
    protected void updatePost_fireStore(String title, String content, String docID){
        println(docID);
        println(String.valueOf(position));
        DocumentReference postRef = db.collection(boardId[position]).document(docID);

        postRef.update("title", title,
                "content", content)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                            println("update success");
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK,resultIntent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                            println("update failure");
                        }
                    });

    }
    protected void writePost_fireStore(String title, String content, String uid, String writer, String time, int position){
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
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK,resultIntent);
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
