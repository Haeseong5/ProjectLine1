package com.example.haeseong.projectline1.helper;

import android.util.Log;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class SchoolApiConnection {
    private static String TAG = SchoolApiConnection.class.getSimpleName(); //현재 클래스명 읽어오기.
//https://github.com/5d-jh/school-menu-api
    OkHttpClient client;
    String URL = "https://schoolmenukr.ml/api/high/";
    private static SchoolApiConnection apiConnection = new SchoolApiConnection();

    public static SchoolApiConnection getInstance() {
        return apiConnection;
    }//SeverConnection 객체 리턴

    public SchoolApiConnection() {
        client = new OkHttpClient();
    }
    public void dataRequest(String schoolCode, Callback callback) {
        Log.d(TAG, "followerListRequest()");
        Request request = new Request.Builder() //토큰값을 넘겨주면서
                .url(URL +schoolCode)
                .get()
                .build();
        client.newCall(request).enqueue(callback); // 팔로워리스트 받기
    }
}
