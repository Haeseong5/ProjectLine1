package com.example.haeseong.projectline1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.data.UserData;
import com.example.haeseong.projectline1.fragment.FragmentUserPhoto;

import java.util.ArrayList;

public class PagerAdapter extends android.support.v4.view.PagerAdapter {
    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    private Context context = null ;

    ArrayList<UserData> userDataArrayList;
    ArrayList<FragmentUserPhoto> fragmentUserPhotos ;
    public PagerAdapter(FragmentManager fragmentManager, ArrayList<UserData> userDataArrayList, Context context)
    {
//        super(fragmentManager);
        this.context = context;
        this.userDataArrayList = userDataArrayList;
        fragmentUserPhotos = new ArrayList<>();
        for(int i=0; i<userDataArrayList.get(0).getPhotos().size(); i++)
        {
            fragmentUserPhotos.add(new FragmentUserPhoto(userDataArrayList));
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null ;

        if (context != null) {
            // LayoutInflater를 통해 "/res/layout/page.xml"을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_user_photo, container, false);

            ImageView ivUserPhoto = view.findViewById(R.id.user_photo);
            Glide.with(context).load(userDataArrayList.get(0).getPhotos().get(position)).into(ivUserPhoto);

        }

        // 뷰페이저에 추가.
        container.addView(view) ;

        return view ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }
//    @Override
//    public Fragment2 getItem(int position) {
//            return fragmentUserPhotos.get(position);
//    }

    @Override
    public int getCount() {
        return userDataArrayList.get(0).getPhotos().size(); //뷰페이저에 포함된 전체 페이지 수
    }
}
