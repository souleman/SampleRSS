package com.example.souleman.rssreader;

import android.app.Activity;
import android.app.LoaderManager;
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
// CHANGER LE NOM MyActivity pas top
public class MyActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private MyListCursorAdapter mCursorAdapter;
    private static final int LOADER_SEARCH_RESULTS = 0;
    private final Context mContext;
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

        MyRefreshingFunction();
   }

    private void MyRefreshingFunction() {
        if (checkInternet()) {
            ExecuteMyTask();
        } else {
            Toast.makeText(this, R.string.NetWork_Missing, Toast.LENGTH_SHORT).show();
        }
    }

    // RSS Reader Function
    private void ExecuteMyTask() {
        OnTaskCompleted mCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<PostData> result) {
                SetRefreshing();
               if (result.size() == 0) {
                    Toast.makeText(mContext, R.string.Loading_Error, Toast.LENGTH_SHORT).show();
                } else {
                    getLoaderManager().restartLoader(LOADER_SEARCH_RESULTS, null, MyActivity.this);
                }
            }
        };
        RssDataController geRss = new RssDataController(mCompleted);
        geRss.execute(mContext);
    }

    private void SetRefreshing() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        SetRefreshing();
        return isConnected;
    }

    static final String[] POSTDATA_SUMMARY_PROJECTION = new String[]{
            Database.POST_KEY,
            Database.POST_TITLE,
            Database.POST_DESCRIPTION,
            Database.POST_DATE,
            Database.POST_IMG,
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                mContext,                           // Parent activity context
                PostDataTable.BASE_CONTENT_URI,               // Table to query
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