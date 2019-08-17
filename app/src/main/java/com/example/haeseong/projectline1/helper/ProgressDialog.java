package com.example.haeseong.projectline1.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.haeseong.projectline1.R;

public class ProgressDialog extends Dialog {
    Context context;
    static ProgressDialog progressDialog;
    public ProgressDialog(@NonNull Context context) {
        super(context);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.progress_dialog);
        ImageView progress_image = findViewById(R.id.progress_gif);
        Glide.with(context).asGif().load(R.raw.progress).into(progress_image);
    }

}
