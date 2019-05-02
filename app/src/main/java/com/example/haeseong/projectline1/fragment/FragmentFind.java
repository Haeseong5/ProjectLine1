package com.example.haeseong.projectline1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.activity.ChatActivity;
import com.example.haeseong.projectline1.adapter.PagerAdapter;
import com.example.haeseong.projectline1.data.ChatData;
import com.example.haeseong.projectline1.data.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentFind extends Fragment {
    private final String TAG = "FragmentFind";
    View rootView;
    ViewPager viewPager;
    ArrayList<UserData> userDataArrayList;
    ArrayList<String> photos;
    TextView tvName;
    TextView tvHeight;
    TextView tvArea;
    TextView tvAge;
    RatingBar rbUserScore;
    ImageButton ibLike;
    ImageButton ibUnLike;
    Button btProfile;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    int index =0; //인덱스 값은 서버에서 넘겨줄 것임.
    @Nullable
    @Override //Fragment가 자신의 UI를 그릴 때 호출합니다. UI를 그리기 위해 메서드에서는 View를 Return 해야 합니다.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_find, container, false);
        setView();
        userDataArrayList = new ArrayList<>();
        init();
        readUserData();



        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                println(String.valueOf(rbUserScore.getRating()));
                index = index++;
            }
        });
        ibUnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                println(String.valueOf(rbUserScore.getRating()));
                index = index++;
            }
        });
        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
    void init(){
        photos = new ArrayList<>();
        photos.add("http://image.cine21.com/resize/cine21/person/2017/1222/16_53_14__5a3cb9ea1e0fc[H800-].jpg");
        photos.add("https://img.sbs.co.kr/newsnet/etv/upload/2017/08/22/30000577662_1280.jpg");
        photos.add("http://image.cine21.com/resize/cine21/person/2017/1222/16_53_14__5a3cb9ea1e0fc[H800-].jpg");
        photos.add("http://image.cine21.com/resize/cine21/person/2017/1222/16_53_14__5a3cb9ea1e0fc[H800-].jpg");
        photos.add("http://image.cine21.com/resize/cine21/person/2017/1222/16_53_14__5a3cb9ea1e0fc[H800-].jpg");
        photos.add("http://image.cine21.com/resize/cine21/person/2017/1222/16_53_14__5a3cb9ea1e0fc[H800-].jpg");
    }

    private void readUserData() {
        DatabaseReference myRef = firebaseDatabase.getReference("users"); //채팅방이름
        myRef.addChildEventListener(new ChildEventListener() {
            @Override //https://firebase.google.com/docs/database/android/retrieve-data?hl=ko
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //항목 목록을 검색하거나 항목 목록에 대한 추가를 수신 대기합니다.
//                 Firebase 데이터베이스의 항목 목록을 검색하는 데 사용
//                UserData userData = dataSnapshot.getValue(UserData.class);
//                println(userData.getName());
                UserData userData = new UserData();
                userData.setName("김태리");
                userData.setPhotos(photos);
                userData.setHeight("170");
                userData.setAge("28");
                userData.setArea("서울");
                userDataArrayList.add(userData);
                tvName.setText(userDataArrayList.get(0).getName());
                tvAge.setText(userDataArrayList.get(0).getAge());
                tvHeight.setText(userDataArrayList.get(0).getHeight());
                tvArea.setText(userDataArrayList.get(0).getArea());


                viewPager.setAdapter(new PagerAdapter(getFragmentManager(),userDataArrayList,getActivity()));
                viewPager.setCurrentItem(0); //앱 실행 시 첫번째 페이지로 초기화
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
    void println(String message){
        Log.d(TAG, message);
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
    void setView(){
        viewPager = rootView.findViewById(R.id.find_viewpager);
        tvName = rootView.findViewById(R.id.find_user_name);
        tvArea = rootView.findViewById(R.id.find_user_area);
        tvHeight = rootView.findViewById(R.id.find_user_height);
        tvAge = rootView.findViewById(R.id.find_user_age);
        rbUserScore = rootView.findViewById(R.id.find_user_rating_bar);
        ibLike = rootView.findViewById(R.id.find_img_btn_like);
        ibUnLike = rootView.findViewById(R.id.find_img_btn_unlike);
        btProfile = rootView.findViewById(R.id.find_view_profile_button);
    }
}
