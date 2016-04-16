package com.example.souleman.rssreader;

import android.net.Uri;

/**
 * Created by Souleman on 08/04/2016.
 */

public class PostDataTable {
    public static final String TABLE_NAME = "PostData";
    public static final String TABLE_PATH = Database.POST_TABLE_NAME;

    public static final String CONTENT_PROVIDER_MIME = Contract.CONTENT_PROVIDER_MIME + TABLE_NAME;
    public static final String CONTENT_PROVIDER_MIME_ITEM = Contract.CONTENT_PROVIDER_MIME_ITEM + TABLE_NAME;

    public static final Uri CONTENT_URI = Contract.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_PATH).build();

    public static Uri buildUri() {
        return CONTENT_URI;
    }

    public static Uri buildItemUri(int id) {
        return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
    }
}
