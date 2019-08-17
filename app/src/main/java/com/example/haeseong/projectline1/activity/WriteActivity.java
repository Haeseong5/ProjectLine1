package com.example.haeseong.projectline1.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.data.Post;
import com.example.haeseong.projectline1.firebase_helper.FireBaseApi;
import com.example.haeseong.projectline1.helper.GlobalUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.haeseong.projectline1.activity.PostActivity.UPDATE_CODE;
import static com.example.haeseong.projectline1.activity.PostActivity.WRITE_CODE;
import static com.example.haeseong.projectline1.activity.RegistProfileActivity.CAMERA_REQUEST;

public class WriteActivity extends AppCompatActivity {
    private static final String TAG = "WriteActivityLog";
    private int requestCode;
    EditText etTitle;
    ImageView ivBackButton;
    ImageView ivOpenGallery;
    Button btFinish;
    EditText etContent;
    int position = -1;
    GlobalUser globalUser;
    String [] boardId = new String[5];
    int i;
    String docID;
    FirebaseUser mFirebaseUser;
    FirebaseFirestore mFireStore;
    Bitmap bitmap;
    Uri uri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        etTitle = findViewById(R.id.write_title);
        ivBackButton = findViewById(R.id.write_back_button);
        btFinish = findViewById(R.id.write_finish);
        etContent = findViewById(R.id.write_content);
        ivOpenGallery = findViewById(R.id.write_add_photo);
        boardId[0] = "free_board";
        boardId[1] = "study_board";
        boardId[2] = "univ_board";
        boardId[3] = "market_board";

        globalUser = GlobalUser.getInstance();
        mFirebaseUser = FireBaseApi.firebaseUser;
        mFireStore = FireBaseApi.firestore;

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
                    String nickName = globalUser.getNickName();
                    String time = format1.format(date);
                    String title = etTitle.getText().toString();
                    String content = etContent.getText().toString();
                    String uid = mFirebaseUser.getUid();
                    writePost_fireStore(title, content, uid, nickName, time, position);
                } else if(requestCode == UPDATE_CODE){ //수정하려는 경우는 제목과 내용만 변경
                    String title = etTitle.getText().toString();
                    String content = etContent.getText().toString();
                    updatePost_fireStore(title, content, docID);
                }
            }
        });
        ivOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, CAMERA_REQUEST);
            }
        });
    }
    protected void updatePost_fireStore(final String title, final String content, String docID){
        println(docID);
        println(String.valueOf(position));
        DocumentReference postRef = mFireStore.collection(boardId[position]).document(docID);

        postRef.update("title", title,
                "content", content)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                            println("update success");
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("update_title",title);
                            resultIntent.putExtra("update_content",content);
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

               mFireStore
                .collection(boardId[position])
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        println("게시물 작성 성공");
                        FireBaseApi.updateArrayFieldFireStore("users",mFirebaseUser.getUid(), documentReference.getId());
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED){
            if (requestCode == CAMERA_REQUEST) {
                uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                    LinearLayout layout = findViewById(R.id.write_photo_layout);
                    float width = getResources().getDimension(R.dimen.image_width);
                    float height = getResources().getDimension(R.dimen.image_height);
                    ImageView iv = new ImageView(this);  // 새로 추가할 imageView 생성

                    iv.setImageBitmap(bitmap);  // imageView에 내용 추가
                    layout.addView(iv); // 기존 linearLayout에 imageView 추가

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
