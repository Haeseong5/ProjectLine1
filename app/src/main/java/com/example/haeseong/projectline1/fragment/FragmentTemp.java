package com.example.haeseong.projectline1.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.haeseong.projectline1.R;

public class FragmentTemp extends Fragment {
    @Nullable
    @Override //Fragment가 자신의 UI를 그릴 때 호출합니다. UI를 그리기 위해 메서드에서는 View를 Return 해야 합니다.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board, container, false);

        return rootView;
    }
}
