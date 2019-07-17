package com.example.haeseong.projectline1.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.adapter.BoardAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentBoard extends android.support.v4.app.Fragment {
    RecyclerView recyclerView;
    BoardAdapter boardAdapter;
    String name;
    String email;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board, container, false);
        recyclerView = rootView.findViewById(R.id.board_recycler_view);
        boardAdapter = new BoardAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())) ;
        recyclerView.setAdapter(boardAdapter);

        return rootView;
    }


}
