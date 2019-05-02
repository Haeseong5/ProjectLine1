package com.example.haeseong.projectline1.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.data.UserData;

import java.util.ArrayList;

public class FragmentUserPhoto extends Fragment {
    ImageView ivUserPhoto;
    ArrayList<UserData> userDataArrayList;

    int position;
    @SuppressLint("ValidFragment")
    public FragmentUserPhoto(ArrayList userDataArrayList){
        this.userDataArrayList = userDataArrayList;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_photo, container, false);
        ivUserPhoto = rootView.findViewById(R.id.user_photo);

        Log.d("FragmentUserPhoto", String.valueOf(userDataArrayList.size()));
        Log.d("FragmentUserPhoto",userDataArrayList.get(0).getPhotos().get(0));
        Glide.with(getActivity()).load(userDataArrayList.get(0).getPhotos().get(position)).into(ivUserPhoto);

        return rootView;
    }
    public FragmentUserPhoto(){}

}
