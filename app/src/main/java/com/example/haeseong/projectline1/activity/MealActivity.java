package com.example.haeseong.projectline1.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.helper.GlobalUser;
import com.example.haeseong.projectline1.helper.SchoolApiConnection;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MealActivity extends AppCompatActivity {
    String TAG = "MealActivity";
    TextView textView;
    SchoolApiConnection schoolApi;
    GlobalUser globalUser;
    Handler handler;
    String[] schoolCode ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        textView = findViewById(R.id.meal_text);
        globalUser = GlobalUser.getInstance();
        schoolApi = SchoolApiConnection.getInstance();
        handler = new Handler();
        schoolCode = new String[10];
        schoolCode[0] = "J100005906"; //이현고
        schoolCode[1] = "J100000760"; //이천고
        schoolCode[2] = "J100000763"; //양정고
        schoolCode[3] = "J100000762"; //제일고
        checkSchool(globalUser.getSchool());
        getData();
    }
        public void getData() {
            schoolApi.dataRequest(globalUser.getSchoolCode(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                final String responseResult;
                                try {
                                    responseResult = response.body().string();
                                    Log.d(TAG, "getData result: " + responseResult);
                                    textView.setText(responseResult);
                                    Gson gson = new Gson();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                }).start();
            }
        }); //jwt토큰을 넘겨주면서 요청에 성공하면 callback이 호출될 것.
    }
    private void checkSchool(String schoolName){
        switch (schoolName ){
            case "이현고등학교":
                globalUser.setSchoolCode(schoolCode[0]);
                break;
            case "이천고등학교":
                globalUser.setSchoolCode(schoolCode[1]);
                break;
            case "양정여자고등학교":
                globalUser.setSchoolCode(schoolCode[2]);
                break;
            case "이천제일고등학교":
                globalUser.setSchoolCode(schoolCode[3]);
                break;
        }
    }
}
