package com.example.haeseong.projectline1.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.adapter.PagerAdapter;
import com.example.haeseong.projectline1.adapter.PagerAdapter2;
import com.example.haeseong.projectline1.data.UserData;

import java.util.ArrayList;

public class Fragment2 extends android.support.v4.app.Fragment {
    ViewPager viewPager;
    ArrayList<UserData> userDataArrayList;
    ArrayList<String> photos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_viewpager, container, false);
        viewPager = rootView.findViewById(R.id.find_container_viewpager);
        userDataArrayList = new ArrayList<>();
        photos = new ArrayList<>();

        photos.add("http://image.cine21.com/resize/cine21/person/2017/1222/16_53_14__5a3cb9ea1e0fc[H800-].jpg");
        photos.add("https://img.sbs.co.kr/newsnet/etv/upload/2017/08/22/30000577662_1280.jpg");
        photos.add("http://image.cine21.com/resize/cine21/person/2017/1222/16_53_14__5a3cb9ea1e0fc[H800-].jpg");
        photos.add("http://image.cine21.com/resize/cine21/person/2017/1222/16_53_14__5a3cb9ea1e0fc[H800-].jpg");
        photos.add("http://image.cine21.com/resize/cine21/person/2017/1222/16_53_14__5a3cb9ea1e0fc[H800-].jpg");
        photos.add("http://image.cine21.com/resize/cine21/person/2017/1222/16_53_14__5a3cb9ea1e0fc[H800-].jpg");

        userDataArrayList.add(new UserData(photos,"김태리","28","서울","170"));
        userDataArrayList.add(new UserData(photos,"김태리2","28","서울","170"));
        userDataArrayList.add(new UserData(photos,"김태리3","28","서울","170"));
        userDataArrayList.add(new UserData(photos,"김태리4","28","서울","170"));

        viewPager.setAdapter(new PagerAdapter2(getFragmentManager(),userDataArrayList,getActivity()));
        viewPager.setCurrentItem(0); //앱 실행 시 첫번째 페이지로 초기화

        return rootView;
    }
}
