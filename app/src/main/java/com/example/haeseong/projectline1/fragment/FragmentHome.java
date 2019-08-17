package com.example.haeseong.projectline1.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.activity.MealActivity;
import com.example.haeseong.projectline1.adapter.GridAdapter;
import com.example.haeseong.projectline1.data.GridItem;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    View rootView;
    GridView gridView;
    ViewFlipper flipper;
    ArrayList<GridItem> items;
    String [] images;
    Animation toLeft;

    private static FragmentHome instance = null;
    float downX;
    float upX;
    public static FragmentHome getInstance(){
        if(instance == null){
            synchronized (FragmentHome.class){
                if(instance == null){
                    instance = new FragmentHome();
                }
            }
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);



        items = new ArrayList<>();
        items.add(new GridItem(R.drawable.ic_app,"동아리"));
        items.add(new GridItem(R.drawable.ic_app,"학원"));
        items.add(new GridItem(R.drawable.ic_app,"스터디 카페"));
        items.add(new GridItem(R.drawable.ic_app,"독서실"));
        items.add(new GridItem(R.drawable.ic_app,"급식"));
        items.add(new GridItem(R.drawable.ic_app,"기타"));
        toLeft= AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_in);


                flipper = rootView.findViewById(R.id.image_slide);
        Resources res = getActivity().getResources();
        images = res.getStringArray(R.array.ad_banner_link);
        FrameLayout linear[]=new FrameLayout[4];
        for(int i=0; i<images.length; i++){
            linear[i]=new FrameLayout(getActivity());
        }

        linear[0].setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        flipper.addView(linear[0]);
        linear[1].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        flipper.addView(linear[1]);
        linear[2].setBackgroundColor(getResources().getColor(R.color.colorAccent));
        flipper.addView(linear[2]);

        flipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                //ACTION_DOWN is constant that means "A person's hand is put on touch panel".
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    downX = motionEvent.getX();
                }
                //ACTION_UP is constant that means "A person's hand is put off on touch panel".
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upX = motionEvent.getX();
                    if (upX < downX) {
                        flipper.showNext();

                    } else if (upX > downX) {
                        flipper.showPrevious();
                    }
                }
                return true;

            }
        });


//        출처: https://satisfactoryplace.tistory.com/14 [만족]

        gridView = rootView.findViewById(R.id.home_gridview);
        GridAdapter gridAdapter = new GridAdapter(getActivity(),R.layout.item_grid_service,items);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();

                switch (position) {
                    case 4:
                        Intent intent = new Intent(getActivity(), MealActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        return rootView;
    }


//        출처: https://satisfactoryplace.tistory.com/14 [만족]
    // 이미지 슬라이더 구현 메서드

//        Glide.with(getActivity()).load(image).into(imageView);


}

