package com.example.haeseong.projectline1.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.data.FriendData;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private ArrayList<FriendData> friendList = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFriendName;
        ImageView ivFriendProfile;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            tvFriendName = itemView.findViewById(R.id.item_friend_name);
            ivFriendProfile = itemView.findViewById(R.id.item_friend_profile);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public FriendAdapter(ArrayList<FriendData> friendList) {
        this.friendList = friendList ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_friend, parent, false) ;
        FriendAdapter.ViewHolder vh = new FriendAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(FriendAdapter.ViewHolder holder, int position) {
        String text = friendList.get(position).getName() ;
        Log.d("friendList",position+"/"+text);
        holder.tvFriendName.setText(text) ;
//        holder.ivFriendProfile.
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        Log.d("friendList size; ", String.valueOf(friendList.size()));
        return friendList.size() ;
    }
}
