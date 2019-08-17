package com.example.haeseong.projectline1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.adapter.CommentAdapter;
import com.example.haeseong.projectline1.adapter.LargeViewPagerAdapter;
import com.example.haeseong.projectline1.data.Comment;
import com.example.haeseong.projectline1.firebase_helper.FireBaseApi;
import com.example.haeseong.projectline1.helper.GlobalUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.haeseong.projectline1.activity.PostActivity.UPDATE_CODE;

public class DetailActivity extends AppCompatActivity {
    private String TAG = "DetailActivity";
    FirebaseFirestore db;
    FirebaseUser mFirebaseUser;
    TextView tvName;
    TextView tvTime;
    TextView tvTitle;
    TextView tvContent;
    ImageView ivPhoto;
    RecyclerView recyclerView_comment;
    EditText etComment;
    Button btCommentWrite;
    ViewPager viewPager;

    CommentAdapter commentAdapter;
    LargeViewPagerAdapter largeViewPagerAdapter;
    ArrayList<Comment> comments;
    String name;
    String title;
    String content;
    String docID;
    String[]boardId = new String [5];
    GlobalUser globalUser;
    ImageView ivBackButton;
    ImageView ivMenu;
    int position;
    int i;
    boolean isMyPost;
     @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvName = findViewById(R.id.detail_name);
        tvTime = findViewById(R.id.detail_time);
        ivPhoto = findViewById(R.id.detail_image);
        tvTitle = findViewById(R.id.detail_title);
        recyclerView_comment = findViewById(R.id.detail_comment_recyclerview);
        etComment = findViewById(R.id.detail_input_comment);
        btCommentWrite = findViewById(R.id.detail_comment_ok);
        tvContent = findViewById(R.id.detail_content);
        ivBackButton = findViewById(R.id.detail_back_button);
        ivMenu = findViewById(R.id.detail_post_menu);
         viewPager = findViewById(R.id.large_view_pager);


         largeViewPagerAdapter = new LargeViewPagerAdapter(getApplicationContext());
        db = FireBaseApi.firestore;
         mFirebaseUser = FireBaseApi.firebaseUser;
         globalUser = GlobalUser.getInstance();
         comments = new ArrayList<>();
         boardId[0] = "free_board";
         boardId[1] = "study_board";
         boardId[2] = "univ_board";
         boardId[3] = "market_board";

        Intent intent = getIntent();
        if(intent != null){
            title = intent.getExtras().getString("title");
            content = intent.getExtras().getString("content");
            name = intent.getExtras().getString("name");
            String time = intent.getExtras().getString("time");
            docID = intent.getExtras().getString("docID");
            position = intent.getExtras().getInt("position");
            i = intent.getExtras().getInt("i");
            isMyPost = intent.getExtras().getBoolean("isMyPost");
            tvName.setText(name);
            tvTime.setText(time);
            tvContent.setText(content);
            tvTitle.setText(title);
            println(String.valueOf(isMyPost));
        }
         commentAdapter = new CommentAdapter(comments,getApplicationContext());
         LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
         mLayoutManager.setReverseLayout(true);
         mLayoutManager.setStackFromEnd(true);
         recyclerView_comment.setLayoutManager(mLayoutManager);
         recyclerView_comment.setAdapter(commentAdapter);
         readComment_fireStore();
         btCommentWrite.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String comment = etComment.getText().toString();
                 if(comment.length()<4){
                     Toast.makeText(getApplicationContext(), "4자 이상 입력해주세요",Toast.LENGTH_SHORT).show();
                 }else{
                     SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                     Date date = new Date();
                     String time = format1.format(date);
                     writeComment_fireStore(etComment.getText().toString(),time);

                     //키보드내리기
                     InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                     imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);
                     commentAdapter.notifyDataSetChanged();
                 }
             }
         });
         ivBackButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });
         ivMenu.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                openDialog(i);
             }
         });
    }

    void openDialog(final int i) //포스트 아이템 포지션
    {
        Log.d("update dialog", String.valueOf(i));
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("수정");
        ListItems.add("삭제");
        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게시물 수정 및 삭제");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
//                Toast.makeText(DetailActivity.this, selectedText, Toast.LENGTH_SHORT).show();
                switch (pos){
                    case 0: //수정
                        if(isMyPost == true){
                            Intent intent = new Intent(DetailActivity.this, WriteActivity.class);
                            intent.putExtra("title", title);
                            intent.putExtra("content", content);
                            intent.putExtra("position", position);
                            intent.putExtra("docID", docID);
                            intent.putExtra("REQUEST_CODE",UPDATE_CODE);
                            startActivityForResult(intent,UPDATE_CODE );
                        }else {
                            Toast.makeText(DetailActivity.this, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case 1: //삭제
                        if (isMyPost == true){
                            deletePost();
                        }else{
                            Toast.makeText(DetailActivity.this, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
        builder.show();
    }

    protected void readComment_fireStore(){
        FireBaseApi.firestore.collection(boardId[position]).document(docID).collection("comment")
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Comment comment = document.toObject(Comment.class);
                                if(comment != null){
                                    comments.add(comment);
                                }
                                println("readPost Success");
                                commentAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    protected void writeComment_fireStore(String content, String time){
        GlobalUser globalUser = GlobalUser.getInstance();
         final Comment comment = new Comment();
         comment.setName(globalUser.getNickName());
         comment.setUid(FireBaseApi.firebaseUser.getUid());
         comment.setContent(content);
        comment.setTime(time);
        db.collection(boardId[position]).document(docID).collection("comment")
                .add(comment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        println("댓글 작성 성공");
                        etComment.getText().clear();
                        comments.add(comment);
                        commentAdapter.notifyDataSetChanged();
                        sendGson();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        println("댓글 작성 실패");
                    }
                });
    }
    protected void deletePost(){
         FireBaseApi.deleteFireStore(boardId[position],docID,new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                 println("success delete");
                 FireBaseApi.deleteArrayFieldFireStore("users",mFirebaseUser.getUid(), docID);
                 Intent resultIntent = new Intent();
                 setResult(RESULT_OK,resultIntent);
                 finish();
             }
         },new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 println("failure delete");
             }
         });
    }

    private void sendGson() {
         println(globalUser.getToken());
        FireBaseApi.sendNotification(getApplicationContext(), globalUser.getToken(), globalUser.getNickName(), "댓글등록이 되었습니다.");
    }

    void println(String message){
        Log.d(TAG, message);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        commentAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //onResume 직전 호출
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
// MainActivity 에서 요청할 때 보낸 요청 코드 (3000)
                case UPDATE_CODE:
                    println("update onActivityResult");
                    String updateTitle = data.getStringExtra("update_title");
                    String updateContent = data.getStringExtra("update_content");
                    tvTitle.setText(updateTitle);
                    tvContent.setText(updateContent);
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK,resultIntent);
                    break;
            }
        }
    }
}
