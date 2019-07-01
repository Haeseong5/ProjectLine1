package com.example.haeseong.projectline1.fragment;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.adapter.FriendAdapter;
import com.example.haeseong.projectline1.data.FriendData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentFind extends Fragment {
    private final String TAG = "FragmentFind";
    View rootView;
    RecyclerView recyclerView;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FriendAdapter adapter;
    ArrayList<FriendData> friendList = new ArrayList<>();
    Button button;
    String myEmail;
    FirebaseUser user;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //리소스 넘겨받은 것들 초기화
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            myEmail = user.getEmail();
        }
//        readData();
    }

    @Nullable
    @Override //Fragment가 자신의 UI를 그릴 때 호출합니다. UI를 그리기 위해 메서드에서는 View를 Return 해야 합니다.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_find, container, false);
        button = rootView.findViewById(R.id.friend_add_button);
        recyclerView = rootView.findViewById(R.id.friend_recyclerview);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog();
            }
        });
        //데이터 세팅
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        // 리사이클러뷰가 아이템을 화면에 표시할 때, 아이템 뷰들이 리사이클러뷰 내부에서 배치되는 형태를 관리하는 요소
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())) ;
        // 리사이클러뷰에 어뎁터 객체 지정.
        adapter = new FriendAdapter(friendList);
        recyclerView.setAdapter(adapter);
        println("onCreateView");
        return rootView;
    }
    void readData(){
        println("readDATA");
        DocumentReference docRef = firestore.collection("friends").document(myEmail);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                println("readData Success");
                FriendData friendData = documentSnapshot.toObject(FriendData.class);
                friendList.add(friendData); //프래그먼트 이동할때마다 데이터가 추가되는 이슈 발생
                adapter.notifyDataSetChanged();
            }
        });
    }
    void readAddedFriend(){
        final DocumentReference docRef = firestore.collection("friends").document(myEmail);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    FriendData friendData = snapshot.toObject(FriendData.class);
                    friendList.add(friendData);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

    }
    void addData(final String inputEmail){
// Update one field, creating the document if it does not already exist.
        Map<String, Object> newFriend = new HashMap<>();
        newFriend.put("name", inputEmail);

        firestore.collection("friends").document(myEmail)
                .set(newFriend, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        println(inputEmail+" firestore 친구추가 성공");
                        readAddedFriend();                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });;
        //컬렉션 - 도큐먼트 - 필드
//        FriendData friendData = new FriendData(inputEmail,"");
//        firestore.collection("friends").document(myEmail).set(friendData)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                        println(inputEmail+" firestore 친구추가 성공");
//                        readAddedFriend();                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
////                   }
//                });
        // Add a new document with a generated ID
    }
    void openAddDialog(){
        final EditText edittext = new EditText(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("AlertDialog Title");
        builder.setMessage("AlertDialog Content");
        builder.setView(edittext);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addData(edittext.getText().toString());
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
    void println(String message){
        Log.d(TAG, message);
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

}
