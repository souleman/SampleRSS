package com.example.souleman.rssreader;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
public class RssReader extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter mCursorAdapter;
    private static final int LOADER_SEARCH_RESULTS = 0;
    private final Context mContext;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mLinearLayout;

    public RssReader() {
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mLinearLayout = (LinearLayout) findViewById(R.id.mainLL);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //tu pourrais faire en sorte que c'est ton objet qui soit listener
        RecyclerViewInterface mRVI = new RecyclerViewInterface() {
            @Override
            public int getPosition(View v) {
                return mRecyclerView.getChildAdapterPosition(v);
            }
        };
        getLoaderManager().initLoader(LOADER_SEARCH_RESULTS, null, this);

        mCursorAdapter = new CursorAdapter(this, mRVI);
        mRecyclerView.setAdapter(mCursorAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshingTask();
            }
        });
        refreshingTask();
    }

    private void refreshingTask() {
        if (checkInternet()) {
            executeMyTask();
        } else {
            Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.network_missing, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void executeMyTask() {
        OnTaskCompleted mCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<PostData> result) {
                setRefreshing();
                if (result.size() == 0) {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.loading_error, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    getLoaderManager().restartLoader(LOADER_SEARCH_RESULTS, null, RssReader.this);
                }
            }
        };
        RssDataController geRss = new RssDataController(mCompleted);
        geRss.execute(mContext);
    }

    private void setRefreshing() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        setRefreshing();
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
        Uri uri = PostDataTable.buildUri();
        return new CursorLoader(
                mContext,                           // Parent activity context
                uri,                                // Table to query
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