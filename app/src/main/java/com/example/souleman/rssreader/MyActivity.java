package com.example.souleman.rssreader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;

public class MyActivity  extends Activity  implements OnTaskCompleted{

    private Context mContext;
    private PostDataDAO mPostDataBase;
    private final String URL = "http://feeds.feedburner.com/elise/simplyrecipes";
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<PostData> mListData = new ArrayList<PostData>();

    public MyActivity() {
        mContext = this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerViewInterface mRVI = new RecyclerViewInterface() {
            @Override
            public int GetRecyclerViewPosition(View v) {
                return mRecyclerView.getChildAdapterPosition(v);
            }

            @Override
            public PostData GetSelectedPostData(int position) {
                return mListData.get(position);
            }
        };
        mAdapter = new RecyclerViewAdapter(this, mListData,mRVI);
        mRecyclerView.setAdapter(mAdapter);

        mPostDataBase = new PostDataDAO(this);
        mPostDataBase.open();

        ArrayList<PostData> oldPostDatas = new ArrayList<PostData>();
        oldPostDatas = mPostDataBase.GetAllPostData();


        if (oldPostDatas.size() != 0) {
            for (int i = 0; i < oldPostDatas.size(); i++) {
                mListData.add(oldPostDatas.get(i));
            }
            mAdapter.notifyDataSetChanged();
        } else {
           MyRefreshingFunction();
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyRefreshingFunction();
            }
        });
    }


    private void MyRefreshingFunction() {
        if (checkInternet()) {
             ExecuteMyTask();
        } else {
            Toast.makeText(this, R.string.NetWork_Missing, Toast.LENGTH_SHORT).show();
        }
    }

    // RSS Reader Function
    public void ExecuteMyTask() {
        Toast.makeText(this, R.string.Execute_MyTask, Toast.LENGTH_SHORT).show();

        OnTaskCompleted mCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<PostData> result) {
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                Toast.makeText(mContext,R.string.TASK_COMPLETED+ result.size(),Toast.LENGTH_SHORT).show();

                if (result.size() == 0){
                    Toast.makeText(mContext,R.string.Loading_Error,Toast.LENGTH_SHORT).show();
                }
                else{
                    mListData.clear();
                    for (int i = 0 ; i < result.size();i++){
                        mListData.add(result.get(i));
                    }
                }
                mAdapter.notifyDataSetChanged();
                SavePostData(mListData);

            }
       };
        RssDataController geRss = new RssDataController(mCompleted);
            geRss.execute(URL);
    }

    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected){
            Toast.makeText(mContext, R.string.Make_Connexion_ON, Toast.LENGTH_LONG).show();
        }
            return isConnected;
    }


    // SAVE DATA SHARED PREFERENCE FUNCTION
    public void SavePostData(ArrayList<PostData> mPostdata) {
        mPostDataBase.open();
        mPostDataBase.add(mPostdata);
        mPostDataBase.close();
    }

    @Override
    public void onTaskCompleted(ArrayList<PostData> result) {

    }
}