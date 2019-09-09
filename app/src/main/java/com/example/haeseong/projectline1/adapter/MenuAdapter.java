package com.example.haeseong.projectline1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.data.Menu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MenuAdapter extends PagerAdapter {
    ArrayList<Menu> menus;
    private LayoutInflater inflater;
    private Context context;
    MenuAdapter(){

    }
    public MenuAdapter(Context context, ArrayList<Menu> menus){
        this.context = context;
        this.menus = menus;
        Log.d("menus", String.valueOf(menus.size()));
    }

    @Override
    public int getCount() {
        return 3;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_menu, container, false);
        TextView tvBreakfast = v.findViewById(R.id.menu_breakfast);
        TextView tvLunch = v.findViewById(R.id.menu_lunch);
        TextView tvDinner = v.findViewById(R.id.menu_dinner);
        int today=0;
        switch (position){
            case 0:
                today = Integer.parseInt(getCurrentDate())-1;
                break;
            case 1:
                today = Integer.parseInt(getCurrentDate());
                break;
            case 2:
                today = Integer.parseInt(getCurrentDate())+1;
                break;
        }
        String breakfast = menuToString(menus.get(today).getBreakfast());
        String lunch = menuToString(menus.get(today).getLunch());
        String dinner = menuToString(menus.get(today).getDinner());

        if(menus.get(today).getBreakfast().size()>0){
            tvBreakfast.setText(breakfast);
        }else{
            tvBreakfast.setText("메뉴가 없습니다.");
        }
        if(menus.get(today).getLunch().size()>0){
            tvLunch.setText(lunch);
        }else{
            tvLunch.setText("메뉴가 없습니다.");
        }
        if(menus.get(today).getDinner().size()>0){
            tvDinner.setText(dinner);
        }else{
            tvDinner.setText("메뉴가 없습니다.");
        }

        container.addView(v) ;

        return v;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
//        container.invalidate();
    }

    protected String menuToString(ArrayList<String> menus){
        String result="";
        for(int i=0; i<menus.size(); i++){
            result += menus.get(i) + ", ";
        }
        return result;
    }
    protected String getCurrentDate(){
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        String[] todayDate = mFormat.format(date).split("-");
        Log.d("todayDate", todayDate[2]);
        return todayDate[2];
    }
}
