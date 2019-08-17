package com.example.haeseong.projectline1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.activity.PostActivity;
import com.example.haeseong.projectline1.market.MarketActivity;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder>  {
    ArrayList<String> boardList;
    private ItemClick itemClick;
    Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvBoardName;
        public ImageView ivBoardImage;
        public ViewHolder(View itemView) {
            super(itemView);
            tvBoardName = itemView.findViewById(R.id.item_board_name);
            ivBoardImage = itemView.findViewById(R.id.item_board_icon);
        }
    }
    public BoardAdapter(Context context){
        this.context = context;
        boardList = new ArrayList<>();
        boardList.add("자유게시판");
        boardList.add("과외게시판");
        boardList.add("입시게시판");
        boardList.add("중고 장터");

    }
    @Override
    public BoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_board_post, parent, false) ;
        BoardAdapter.ViewHolder vh = new BoardAdapter.ViewHolder(view) ;
        return vh;
    }

    @Override
    public void onBindViewHolder(BoardAdapter.ViewHolder holder, final int position) {
        holder.tvBoardName.setText(boardList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, String.valueOf(position),Toast.LENGTH_SHORT).show();
                if(position == 3){
                    Intent intent = new Intent(context, MarketActivity.class);
                    intent.putExtra("position",position);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("position",position);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }
    //아이템 클릭시 실행 함수

    public interface ItemClick {
        public void onClick(View view,int position);
    }

    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }
}
