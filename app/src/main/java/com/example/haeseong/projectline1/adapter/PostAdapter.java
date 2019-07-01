package com.example.haeseong.projectline1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.data.Post;
import com.example.haeseong.projectline1.item.ProfileItem;

import java.util.ArrayList;

public class PostAdapter extends BaseAdapter{
    ArrayList <Post> posts ;
    Context context;
    int position;

    public PostAdapter(Context context, ArrayList<Post> post, int position){
        this.posts = post;
        this.context = context;
        this.position = position;

    }
    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_post, parent, false);
        }
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView tvTitle = convertView.findViewById(R.id.item_post_title);
        TextView tvContent = convertView.findViewById(R.id.item_post_content);
        TextView tvTime = convertView.findViewById(R.id.item_post_time);
        TextView tvName = convertView.findViewById(R.id.item_post_id);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Post post = posts.get(posts.size()-1-position); //역순으로 접근

        // 아이템 내 각 위젯에 데이터 반영
        tvTitle.setText(post.getTitle());
        tvContent.setText(post.getContent());
        tvTime.setText(post.getTime());
        tvName.setText(post.getName());
        return convertView;
    }
}
