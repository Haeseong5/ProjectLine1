package com.example.haeseong.projectline1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.adapter.CommentAdapter;
import com.example.haeseong.projectline1.data.Comment;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    TextView tvName;
    TextView tvTime;
    TextView tvTitle;
    TextView tvContent;
    ImageView ivPhoto;
    RecyclerView recyclerView_comment;
    EditText etComment;
    Button btCommentWrite;
    CommentAdapter commentAdapter;
    ArrayList<Comment> comments;
    String name;
    ImageView ivBackButton;
    ImageView ivMenu;
    int position;
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
        comments = new ArrayList<>();


        Intent intent = getIntent();
        if(intent != null){
            String title = intent.getExtras().getString("title");
            String content = intent.getExtras().getString("content");
            name = intent.getExtras().getString("name");
            String time = intent.getExtras().getString("time");
            position = intent.getExtras().getInt("position");
            tvName.setText(name);
            tvTime.setText(time);
            tvContent.setText(content);
            tvTitle.setText(title);
            comments.add(new Comment(name,"댓글테스트"));
        }
         commentAdapter = new CommentAdapter(comments,getApplicationContext());
         LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
         mLayoutManager.setReverseLayout(true);
         mLayoutManager.setStackFromEnd(true);
         recyclerView_comment.setLayoutManager(mLayoutManager);
         recyclerView_comment.setAdapter(commentAdapter);

         btCommentWrite.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String comment = etComment.getText().toString();
                 if(comment.length()<4){
                     Toast.makeText(getApplicationContext(), "4자 이상 입력해주세요",Toast.LENGTH_SHORT).show();
                 }else{
                     comments.add(new Comment(name,comment));
                     etComment.getText().clear();
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
                 show(position);
             }
         });
    }

    @Override
    protected void onResume() {
        super.onResume();
        commentAdapter.notifyDataSetChanged();
    }
    void show(int position)
    {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("수정");
        ListItems.add("삭제");
        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게시물 수정 및 삭제");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
                Toast.makeText(DetailActivity.this, selectedText, Toast.LENGTH_SHORT).show();
                switch (pos){
                    case 0:
                        //수정
                    case 1:
                        //삭제

                }
            }
        });
        builder.show();
    }

}
