package com.example.haeseong.projectline1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.adapter.PostAdapter;
import com.example.haeseong.projectline1.data.Post;
import com.example.haeseong.projectline1.firebase_helper.FireBaseApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private static final String TAG = "PostActivity";
    public static final int WRITE_CODE = 101;
    public static final int UPDATE_CODE = 102;
    public static final int DETAIL_CODE = 103;
    FirebaseFirestore mFireStore;

    ListView listView;
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    androidx.appcompat.widget.Toolbar board_toolbar;
    TextView tvBoardTitle;
    ImageView ivSearch;
    ImageView ivWrite;
    int position;
    String[] boardTitle = new String[5];
    String[] boardId = new String[5];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        listView = findViewById(R.id.post_listview);
        board_toolbar = findViewById(R.id.post_board_toolbar);
        tvBoardTitle = findViewById(R.id.post_board_title);
        ivSearch = findViewById(R.id.post_search_image);
        ivWrite = findViewById(R.id.post_write_image);

        boardTitle[0] = "자유게시판";    boardId[0] = "free_board";
        boardTitle[1] = "과외게시판";    boardId[1] = "study_board";
        boardTitle[2] = "입시게시판";    boardId[2] = "univ_board";
        boardTitle[3] = "중고 장터";     boardId[3] = "market_board";

        mFireStore = FirebaseFirestore.getInstance();

        posts = new ArrayList<>();
        Intent intent = getIntent();
        if(intent != null){
            position = intent.getExtras().getInt("position");
            tvBoardTitle.setText(boardTitle[position]);
        }
        Toast.makeText(getApplicationContext(), position+boardTitle[position], Toast.LENGTH_SHORT).show();
        postAdapter = new PostAdapter(getApplicationContext(), posts, position);
        readPosts();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                i = posts.size()-1-i;
                Intent intent = new Intent(PostActivity.this, DetailActivity.class);;
                intent.putExtra("name", posts.get(i).getWriter());
                intent.putExtra("title", posts.get(i).getTitle());
                intent.putExtra("content", posts.get(i).getContent());
                intent.putExtra("time", posts.get(i).getTimeStamp());
                intent.putExtra("docID", posts.get(i).getDocID());
                intent.putExtra("i", i);
                intent.putExtra("position", position);
                intent.putExtra("boardId", boardId);
                intent.putExtra("isMyPost", posts.get(i).isMyPost());
                println(String.valueOf(posts.get(i).isMyPost()));
                startActivityForResult(intent, DETAIL_CODE);
            }
        });
        ivWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, WriteActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("REQUEST_CODE",WRITE_CODE);
                println(String.valueOf(position));
                startActivityForResult(intent,WRITE_CODE);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
                i = posts.size()-i-1;
                openDialog(i);
                println(String.valueOf(i));
                return false;
            }
        });
    }

    protected void deletePost(int i){
        mFireStore.collection(boardId[position]).document(posts.get(i).getDocID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        println("success delete");
                        readPosts();
                        postAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
    protected void readPosts(){ //갯수한정해야함. 페이징?기법 찾아보기
        mFireStore.collection(boardId[position])
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Post post = document.toObject(Post.class);
                                if(post != null){
//                                    Log.d("post", post.getTitle());
                                    post.setDocID(document.getId());
                                    println(post.getUid());
                                    println(FireBaseApi.firebaseUser.getUid());
                                    if(post.getUid().equals(FireBaseApi.firebaseUser.getUid()) ){
                                        post.setMyPost(true);
                                    }else{
                                        post.setMyPost(false);
                                    }
                                    posts.add(post);
                                    Log.d("post", String.valueOf(posts.size()));
                                }
                                listView.setAdapter(postAdapter);

//                                for(int i=0; i<5; i++){
//                                    posts.add(new Post("포스트제목"+i,"포스트내용"+i,"방금","익명"));
//                                }
                                println("readPost Success");

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //onResume 직전 호출
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
// MainActivity 에서 요청할 때 보낸 요청 코드 (3000)
                case WRITE_CODE:
                    posts.clear();
                    println("write onActivityResult");
                    readPosts();
//                    postAdapter.notifyDataSetChanged();
                    break;
                case UPDATE_CODE:
                    println("update onActivityResult");
                    posts.clear();
                    readPosts();
                    break;
                case DETAIL_CODE: //삭제가 이루어 졌을 때만 호출
                    posts.clear();
                    readPosts();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        postAdapter.notifyDataSetChanged();
        println("onResume");
    }
    void println(String message){
        Log.d(TAG, message);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PostActivity.this, selectedText, Toast.LENGTH_SHORT).show();
                switch (pos){
                    case 0: //수정
                        Intent intent = new Intent(PostActivity.this, WriteActivity.class);
                        intent.putExtra("title", posts.get(i).getTitle());
                        intent.putExtra("content", posts.get(i).getContent());
                        intent.putExtra("position", position);
                        intent.putExtra("docID",posts.get(i).getDocID());
                        intent.putExtra("REQUEST_CODE",UPDATE_CODE);
                        println(String.valueOf(i));
                        startActivityForResult(intent,UPDATE_CODE );
                        break;
                    case 1: //삭제
                        deletePost(i);
                        break;
                }
            }
        });
        builder.show();
    }
}
