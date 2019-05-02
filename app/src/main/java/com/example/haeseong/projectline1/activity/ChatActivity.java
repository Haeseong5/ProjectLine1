package com.example.haeseong.projectline1.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.adapter.ChatAdapter;
import com.example.haeseong.projectline1.data.ChatData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity: ";
    Button btSend;
    EditText etMessage;
    Button btFinish;
    TextView tvMessge;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String[] myDataSet = {"안녕", "오늘","뭐했어","영화볼래?"};
    String email;

    ArrayList<ChatData> mChat;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setView();

        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //현재 파이어베이스 유저를 담고 있는 객체.
        if (user != null) { //유저가 로그인이 되어있으면
            // Name, email address, and profile photo Url
            email = user.getEmail();
        }
        println(email);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();

                if (message.equals("") || message.isEmpty()){
                    Toast.makeText(ChatActivity.this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatActivity.this, email+": "+message, Toast.LENGTH_SHORT).show();

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("chats").child(formattedDate);

                    Hashtable<String, String> chat = new Hashtable<String, String>();
                    chat.put("email", email);
                    chat.put("text", message);
                    myRef.setValue(chat);
                }
            }
        });
        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mChat = new ArrayList<>();
        // specify an adapter (see also next example)
        mAdapter = new ChatAdapter(mChat,email,ChatActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        DatabaseReference myRef = firebaseDatabase.getReference("chats"); //채팅방이름
        myRef.addChildEventListener(new ChildEventListener() {
            @Override //https://firebase.google.com/docs/database/android/retrieve-data?hl=ko
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            //항목 목록을 검색하거나 항목 목록에 대한 추가를 수신 대기합니다.
//                 Firebase 데이터베이스의 항목 목록을 검색하는 데 사용
                ChatData chat = dataSnapshot.getValue(ChatData.class);
                Log.d("chat data:", chat.getText()+"/"+chat.getEmail());
                // [START_EXCLUDE]
                // Update RecyclerView
                mChat.add(chat);
                mRecyclerView.scrollToPosition(mChat.size() - 1);
                mAdapter.notifyItemInserted(mChat.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    void setView(){
        btSend = findViewById(R.id.chat_button_send);
        etMessage = findViewById(R.id.chat_edit_message);
        btFinish = findViewById(R.id.chat_button_finish);
        mRecyclerView = findViewById(R.id.chat_recyclerView);
    }
    void println(String message){
        Log.d(TAG, message);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}





