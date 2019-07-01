package com.example.haeseong.projectline1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.adapter.PostAdapter;
import com.example.haeseong.projectline1.data.Post;
import com.example.haeseong.projectline1.helper.GlobalUser;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private static final String TAG = "PostActivity";
    private final int WRITE_CODE = 101;
    private final int UPDATE_CODE = 102;

    ListView listView;
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    android.support.v7.widget.Toolbar board_toolbar;
    TextView tvBoardTitle;
    ImageView ivSearch;
    ImageView ivWrite;
    int position;
    String[] boardTitle = new String[5];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        listView = findViewById(R.id.post_listview);
        board_toolbar = findViewById(R.id.post_board_toolbar);
        tvBoardTitle = findViewById(R.id.post_board_title);
        ivSearch = findViewById(R.id.post_search_image);
        ivWrite = findViewById(R.id.post_write_image);
        posts = new ArrayList<>();
        boardTitle[0] = "자유게시판";
        boardTitle[1] = "과외게시판";
        boardTitle[2] = "입시게시판";
        boardTitle[3] = "중고 장터";
        Intent intent = getIntent();
        if(intent != null){
            position = intent.getExtras().getInt("position");
            tvBoardTitle.setText(boardTitle[position]);
        }
        Toast.makeText(getApplicationContext(), position+boardTitle[position], Toast.LENGTH_SHORT).show();
        for(int i=0; i<5; i++){
            posts.add(new Post("포스트제목"+i,"포스트내용"+i,"방금","익명"));
        }
        postAdapter = new PostAdapter(getApplicationContext(), posts, position);
        listView.setAdapter(postAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * ListView의 Item을 Click 할 때 수행할 동작
             * @param parent 클릭이 발생한 AdapterView.
             * @param view 클릭 한 AdapterView 내의 View(Adapter에 의해 제공되는 View).
             * @param position 클릭 한 Item의 position
             * @param id 클릭 된 Item의 Id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // adapter.getItem(position)의 return 값은 Object 형
                // 실제 Item의 자료형은 CustomDTO 형이기 때문에
                // 형변환을 시켜야 getResId() 메소드를 호출할 수 있습니다.
//                String title = ((Post)postAdapter.getItem(position)).getTitle();
                position = posts.size()-1-position;
                // new Intent(현재 Activity의 Context, 시작할 Activity 클래스)
                Intent intent = new Intent(PostActivity.this, DetailActivity.class);;
                intent.putExtra("name", posts.get(position).getName());
                intent.putExtra("title", posts.get(position).getTitle());
                intent.putExtra("content", posts.get(position).getContent());
                intent.putExtra("time", posts.get(position).getTime());
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        ivWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, WriteActivity.class);
                startActivityForResult(intent,WRITE_CODE);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                position = posts.size()-position-1;
                openDialog(position);
                println(String.valueOf(position));
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
// MainActivity 에서 요청할 때 보낸 요청 코드 (3000)
                case WRITE_CODE:
                    String title = data.getStringExtra("title");
                    String content = data.getStringExtra("content");
                    String time = data.getStringExtra("time");
                    String name = data.getStringExtra("name");
                    posts.add(new Post(title,content,time,name));
                    println(time);
                    break;
                case UPDATE_CODE:
                    String updateTitle = data.getStringExtra("title");
                    String updateContent = data.getStringExtra("content");
                    String updateTime = data.getStringExtra("time");
                    String updateName = data.getStringExtra("name");
                    int position = data.getIntExtra("position",-1);
                    println(String.valueOf(position));
                    Post updatePost = new Post(updateTitle,updateContent,updateTime,updateName);
                    posts.set(position, updatePost);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        postAdapter.notifyDataSetChanged();
    }
    void println(String message){
        Log.d(TAG, message);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    void openDialog(final int position)
    {
        Log.d("update dialog", String.valueOf(position));
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
                    case 0:
                        Intent intent = new Intent(PostActivity.this, WriteActivity.class);
                        intent.putExtra("title", posts.get(position).getTitle());
                        intent.putExtra("content", posts.get(position).getContent());
                        intent.putExtra("position", position);
                        startActivityForResult(intent,UPDATE_CODE );
                        break;
                    case 1:
                        //삭제
                        posts.remove(position);
                        listView.clearChoices(); //리스트뷰 선택초기화
                        postAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
        builder.show();
    }
}
