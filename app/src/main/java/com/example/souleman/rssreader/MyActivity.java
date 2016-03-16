package com.example.souleman.rssreader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;

public class MyActivity extends Activity {
    public static Context mContext;

    private static PostDataDAO mPostDataBase;
    private ArrayList<PostData> OldPostDatas;
    private Handler handler = new Handler();
    private final String URL = "http://feeds.feedburner.com/elise/simplyrecipes";

    public static RecyclerView mRecyclerView;
    public static RecyclerViewAdapter adapter;

    public static SwipeRefreshLayout mSwipeRefreshLayout;

    public static ArrayList<PostData> listData = new ArrayList<PostData>();

    public MyActivity() {
        mContext = this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mContext = this;

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerViewAdapter(this, listData);
        mRecyclerView.setAdapter(adapter);

        mPostDataBase = new PostDataDAO(this);
        mPostDataBase.open();

        OldPostDatas = new ArrayList<PostData>();
        OldPostDatas = mPostDataBase.GetAllPostData();

        if (OldPostDatas.size() != 0) {
            Toast.makeText(mContext, "Uploadind data...", Toast.LENGTH_LONG).show();
            for (int i = 0; i < OldPostDatas.size(); i++) {
                listData.add(OldPostDatas.get(i));
            }
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(mContext, "1st Starting... loading data", Toast.LENGTH_SHORT).show();
            if (checkInternet()) {
                ExecuteMyTask();
            } else {
                Toast.makeText(this, "Need network connection missing...", Toast.LENGTH_SHORT).show();
            }
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyRefreshingFunction();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 5 * 1000);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    // Loading New Data
    public void MyRefreshingFunction() {
        if (checkInternet()) {
            Toast.makeText(this, "please wait until a next message...", Toast.LENGTH_SHORT).show();

            ExecuteMyTask();
        } else {
            Toast.makeText(this, "NetWork Connection missing...", Toast.LENGTH_SHORT).show();
        }
    }

    // RSS Reader Function
    public void ExecuteMyTask() {
        RssDataController geRss = new RssDataController();
        try {
            Toast.makeText(this, "TAST EXECUTING...", Toast.LENGTH_SHORT).show();

            geRss.execute(URL);
        } catch (Exception e) {
            Log.e("MyActivity", "Erreur Task not executed ");
        }
        //itemAdapter.notifyDataSetChanged();
    }

    // Check Network connection
    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

//For 3G check
        boolean is3g = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
//For WiFi Check
        boolean isWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();

        if (!is3g && !isWifi) {
            Toast.makeText(getApplicationContext(), "Please make sure your Network Connection is ON ", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    // SAVE DATA SHARED PREFERENCE FUNCTION
    public static void SavePostData(ArrayList<PostData> mPostdata) {
/*
        if(mSwipeRefreshLayout.isRefreshing()){
            Toast.makeText(MyActivity.mContext,"Swipe refreshing",Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(MyActivity.mContext,"FINISH",Toast.LENGTH_LONG).show();
        }
*/
        mPostDataBase.open();
        mPostDataBase.ajouter(mPostdata);
        mPostDataBase.close();
        Toast.makeText(MyActivity.mContext, "DATA SAVED", Toast.LENGTH_LONG).show();

    }
}