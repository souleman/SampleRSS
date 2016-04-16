package com.example.souleman.rssreader;

import android.net.Uri;

/**
 * Created by Souleman on 07/04/2016.
 */

public class Contract {
    public static final String BASE_AUTHORITY = "com.example.souleman.rssreader";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + BASE_AUTHORITY);

    public static final String FIRST_VENDOR = "vnd.android.cursor.";
    public static final String TYPE_DIR = "dir";
    public static final String TYPE_ITEM = "item";
    public static final String SUB_VENDOR = "/vnd."+BASE_AUTHORITY;

    public static final String CONTENT_PROVIDER_MIME = FIRST_VENDOR + TYPE_DIR + SUB_VENDOR;
    public static final String CONTENT_PROVIDER_MIME_ITEM = FIRST_VENDOR + TYPE_ITEM + SUB_VENDOR;
}


