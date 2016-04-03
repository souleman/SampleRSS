package com.example.souleman.rssreader;

import android.database.Cursor;
import android.view.View;

/**
 * Created by Souleman on 21/03/2016.
 */
public interface RecyclerViewInterface {
    int GetRecyclerViewPosition(View v);
    PostData GetSelectedPostData(int position);
    Cursor getMyCursor();

}
