package com.example.haeseong.projectline1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.data.GridItem;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    ArrayList<GridItem> items;
    Context context;
    int layout;
    public GridAdapter(Context context, int layout, ArrayList<GridItem> items){
        this.context = context;
        this.items = items;
        this.layout = layout;


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
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView==null){
            convertView = layoutInflater.inflate(layout, null);
        }
        ImageView imageView = convertView.findViewById(R.id.item_grid_image);
        TextView textView = convertView.findViewById(R.id.item_grid_text);
        imageView.setImageResource(items.get(position).getImage());
        textView.setText(
                items.get(position).getText());


        return convertView;
    }
}
