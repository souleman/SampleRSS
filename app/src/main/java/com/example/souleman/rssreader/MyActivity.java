package com.example.souleman.rssreader;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MyActivity  extends Activity  implements LoaderManager.LoaderCallbacks<Cursor> {
    private MyListCursorAdapter mCursorAdapter;
    private static final int LOADER_SEARCH_RESULTS = 0;
    private final Context mContext;
    private final String URL = "http://feeds.feedburner.com/elise/simplyrecipes";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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
        };
        getLoaderManager().initLoader(LOADER_SEARCH_RESULTS, null, this);

        mCursorAdapter = new MyListCursorAdapter(this, mRVI);
        mRecyclerView.setAdapter(mCursorAdapter);

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
        OnTaskCompleted mCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<PostData> result) {
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (result.size() == 0){
                    Toast.makeText(mContext,R.string.Loading_Error,Toast.LENGTH_SHORT).show();
                }
                else{
                    SavePostData(result);
                }
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
    public void SavePostData(ArrayList<PostData> result) {
       getContentResolver().delete(MyContentProvider.CONTENT_URI, null, null);

        for (int i = 0; i < result.size(); i++) {
            ContentValues content = new ContentValues();
            content.put(PostDataDAO.POST_TITLE, result.get(i).getTitre());
            content.put(PostDataDAO.POST_DATE, result.get(i).getDate());
            content.put(PostDataDAO.POST_DESCRIPTION, result.get(i).getDescription());
            content.put(PostDataDAO.POST_IMG, result.get(i).getImage());
            getContentResolver().insert(MyContentProvider.CONTENT_URI,content);
        }
        getLoaderManager().restartLoader(LOADER_SEARCH_RESULTS,null,this);
    }

    // These are the Contacts rows that we will retrieve.
    static final String[] POSTDATA_SUMMARY_PROJECTION = new String[] {
            PostDataDAO.POST_TITLE,
            PostDataDAO.POST_DESCRIPTION,
            PostDataDAO.POST_DATE,
            PostDataDAO.POST_IMG,
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                mContext,                           // Parent activity context
                MyContentProvider.CONTENT_URI,      // Table to query
                POSTDATA_SUMMARY_PROJECTION,        // Projection to return
                null,                               // No selection clause
                null,                               // No selection arguments
                null                                // Default sort order
            );
        }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}