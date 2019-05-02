package com.example.haeseong.projectline1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.data.ChatData;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    final int MY_MESSAGE = 1;
    final int YOUR_MESSAGE = 2;
    private String[] mDataset;
    ArrayList<ChatData> mChat;
    String stEmail; //db의 텍스트가 내꺼인지 상대거인지 확인하기 위해 로그인된 아이디와 텍스트의 아이디 일치여부를 체크.
    Context context;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.mTextView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatAdapter(ArrayList<ChatData> mChat, String email, Context context) {
        this.mChat = mChat;
        this.stEmail = email;
        this.context = context;
        Log.d("ChatAdapter", email+"/"+String.valueOf(mChat.size()));
    }

    @Override
    public int getItemViewType(int position) {
    // position 위치의 아이템 타입 리턴.
        if (mChat.get(position).getEmail().equals(stEmail)){ //아이디가 내이메일과 같다면 1을 리턴 아니면 2를리턴
            return MY_MESSAGE;
        } else {
            return YOUR_MESSAGE;
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v;
        if (viewType == MY_MESSAGE) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_your_chat, parent, false);
        }
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mChat.get(position).getText());
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mChat.size();
    }
}

