package com.example.haeseong.projectline1.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.haeseong.projectline1.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class AutoScrollAdapter  extends PagerAdapter {

    Context context;
//    ArrayList<String> data;
    String[] banners;
    public AutoScrollAdapter(Context context) {
        this.context = context;
//        this.data = data;
        Resources res = context.getResources();
        banners = res.getStringArray(R.array.ad_banner_link);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // position 값을 받아 주어진 위치에 페이지를 생성합니다.
        //뷰페이지 슬라이딩 할 레이아웃 인플레이션
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_auto_viewpager,null);
        ImageView image_container = v.findViewById(R.id.item_home_image);
        Glide.with(context).load(banners[position]).into(image_container);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //position 값을 받아 주어진 위치에 있는 페이지를 삭제 합니다.
        container.removeView((View)object);

    }

    @Override
    public int getCount() {
        return banners.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        //페이지 뷰가 생성된 페이지의 object key와 같은지 확인합니다. object key는 instantiate
        // object key는 instantiateItem 메소드에서 리턴 시킨 object 입니다. 즉, 페이지의 뷰가 생성된 뷰인지 아닌지를 확인합니다.
        return view == object;
    }
}
