package com.example.haeseong.projectline1.market;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.firebase_helper.FireBaseApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MarketActivity extends AppCompatActivity {
    private String TAG = "Market activity";
    private RecyclerView mRecyclerView;
    MarketAdapter marketAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MarketData> marketDataSet;
    ImageView ivBackButton;
    ImageView ivWritePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        mRecyclerView = findViewById(R.id.market_recycler_view);
        ivBackButton = findViewById(R.id.market_back_button);
        ivWritePage = findViewById(R.id.market_write_page);

        marketDataSet = new ArrayList<>();
        marketAdapter = new MarketAdapter(marketDataSet);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        readMarketData();


        ivBackButton.setOnClickListener(v -> finish());
        ivWritePage.setOnClickListener(v -> {
            Intent intent = new Intent(MarketActivity.this, MarketWriteActivity.class);
            startActivityForResult(intent,3000);
        });
        // specify an adapter (see also next example)
//        mAdapter = new MarketAdapter(marketDataSet);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2){
//            @Override
//            public boolean canScrollVertically() { // 세로스크롤 막기
//                return false;
//            }
//
//            @Override
//            public boolean canScrollHorizontally() { //가로 스크롤막기
//                return false;
//            }
//        });



//        marketDataSet.add(new MarketData("#InsideOut", R.mipmap.ic_app));
//        marketDataSet.add(new MarketData("#Mini", R.mipmap.ic_app));
//        marketDataSet.add(new MarketData("#ToyStroy", R.mipmap.ic_app));
//        marketDataSet.add(new MarketData("#InsideOut", R.mipmap.ic_app));
//        marketDataSet.add(new MarketData("#Mini", R.mipmap.ic_app));
//        marketDataSet.add(new MarketData("#ToyStroy", R.mipmap.ic_app));
//        marketDataSet.add(new MarketData("#InsideOut", R.mipmap.ic_app));
//        marketDataSet.add(new MarketData("#Mini", R.mipmap.ic_app));
//        marketDataSet.add(new MarketData("#ToyStroy", R.mipmap.ic_app));
    }
    protected void readMarketData(){ //갯수한정해야함. 페이징?기법 찾아보기
        FireBaseApi.firestore.collection("market_board")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                MarketData marketData = document.toObject(MarketData.class);
                                if(marketData != null){
                                    marketDataSet.add(marketData);
                                    Log.d("post", String.valueOf(marketDataSet.size()));
                                    mRecyclerView.setAdapter(marketAdapter);
                                }
                            }
                        } else {

                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
// MainActivity 에서 요청할 때 보낸 요청 코드 (3000)
                case 3000:
                    readMarketData();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        marketAdapter.notifyDataSetChanged();
    }
}
