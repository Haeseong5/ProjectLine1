package com.example.haeseong.projectline1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.item.ProfileItem;

import java.util.ArrayList;

public class ProfileAdapter2 extends BaseAdapter {
    Context context;
    private ArrayList<String> items;

    public ProfileAdapter2(Context context, ArrayList<String> items){
        this.context = context;
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
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
            convertView = inflater.inflate(R.layout.item_profile2, parent, false);
        }
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView text1 = convertView.findViewById(R.id.item_profile2_text);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Log.d("adapter2 i: ", String.valueOf(position));
        Log.d("adapter2 items", String.valueOf(items.size()));
        // 아이템 내 각 위젯에 데이터 반영
        text1.setText(items.get(position));
        return convertView;
    }
}
