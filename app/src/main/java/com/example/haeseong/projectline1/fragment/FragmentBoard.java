package com.example.haeseong.projectline1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.adapter.BoardAdapter;

public class FragmentBoard extends Fragment {
    RecyclerView recyclerView;
    BoardAdapter boardAdapter;
    String name;
    String email;

    private static FragmentBoard instance = null;

    public static FragmentBoard getInstance(){
        if(instance == null){
            synchronized (FragmentBoard.class){
                if(instance == null){
                    instance = new FragmentBoard();
                }
            }
        }
        return instance;
    }
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
