package com.example.haeseong.projectline1.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.data.ChatData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentChat extends Fragment {
    RecyclerView recyclerView;
    ListView chat_view;
    EditText etMessage;
    Button btSend;
    FirebaseUser firebaseUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("message");
    String email;
    @Nullable
    @Override //Fragment가 자신의 UI를 그릴 때 호출합니다. UI를 그리기 위해 메서드에서는 View를 Return 해야 합니다.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        etMessage = rootView.findViewById(R.id.chat_edit_message);
        btSend = rootView.findViewById(R.id.chat_button_send);
//        recyclerView = rootView.findViewById(R.id.chat_recyclerView);
        chat_view = rootView.findViewById(R.id.chat_listview);
        openChat("chat_test");
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatData chatData = new ChatData("haeseong", etMessage.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
                databaseReference.child("chat").push().setValue(chatData);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기
                etMessage.setText("");
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            email = user.getEmail();
        }


        return rootView;
    }
    private void openChat(String chatName) {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_view.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").child(chatName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot, adapter);
                Log.e("LOG", "s:"+s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatData chatDTO = dataSnapshot.getValue(ChatData.class);
        adapter.add(chatDTO.getEmail() + " : " + chatDTO.getText());
    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatData chatDTO = dataSnapshot.getValue(ChatData.class);
        adapter.remove(chatDTO.getEmail() + " : " + chatDTO.getText());
    }
}