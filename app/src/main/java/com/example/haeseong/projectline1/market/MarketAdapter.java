package com.example.haeseong.projectline1.market;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.haeseong.projectline1.R;

import java.util.ArrayList;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> {
    private ArrayList<MarketData> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView ivPhoto;
        public TextView tvWriter;
        public TextView tvTitle;
        public TextView tvPrice;

        public ViewHolder(View view) {
            super(view);
            ivPhoto = (ImageView)view.findViewById(R.id.market_item_image);
            tvWriter = (TextView)view.findViewById(R.id.market_item_writer);
            tvTitle = view.findViewById(R.id.market_item_title);
            tvPrice = view.findViewById(R.id.market_item_price);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MarketAdapter(ArrayList<MarketData> marketDataset) {
        mDataset = marketDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MarketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_market, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tvWriter.setText(mDataset.get(position).writer);
        holder.tvPrice.setText(mDataset.get(position).price);
        holder.tvTitle.setText(mDataset.get(position).title);

//        holder.ivPhoto.setImageResource(mDataset.get(position).photo.get(0));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}



