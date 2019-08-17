package com.example.haeseong.projectline1.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.data.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    ArrayList<Comment> commentList;
    String name; //db의 텍스트가 내꺼인지 상대거인지 확인하기 위해 로그인된 아이디와 텍스트의 아이디 일치여부를 체크.
    Context context;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvName;
        public TextView tvComment;
        public Button btReply;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.comment_name);
            tvComment = itemView.findViewById(R.id.comment_content);
            btReply = itemView.findViewById(R.id.comment_reply_button);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommentAdapter(ArrayList<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
//        Log.d("ChatAdapter", email+"/"+String.valueOf(mChat.size()));
    }

    @Override
    public int getItemViewType(int position) {
        // position 위치의 아이템 타입 리턴.
//        if (mChat.get(position).getEmail().equals(stEmail)){ //아이디가 내이메일과 같다면 1을 리턴 아니면 2를리턴
//            return MY_MESSAGE;
//        } else {
//            return YOUR_MESSAGE;
//        }
        return position;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tvName.setText(commentList.get(position).getName());
        holder.tvComment.setText(commentList.get(position).getContent());
        holder.btReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDialog();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return commentList.size();
    }

    void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("AlertDialog Title");
        builder.setMessage("AlertDialog Content");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }
}

