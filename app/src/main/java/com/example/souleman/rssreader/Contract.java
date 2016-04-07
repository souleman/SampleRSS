package com.example.souleman.rssreader;

import android.net.Uri;

/**
 * Created by Souleman on 07/04/2016.
 */

public class Contract {
    public static final String AUTHORITY = "com.example.souleman.rssreader.MyContentProvider";
    private static final String CONTENT = "content://";
    public static final String TABLE_PATH_PICTURE = Database.POST_TABLE_NAME;

    public static final Uri CONTENT_URI =      Uri.parse(CONTENT +AUTHORITY + "/" + TABLE_PATH_PICTURE);
    public static final Uri CONTENT_URI_ITEM = Uri.parse(CONTENT+ AUTHORITY + "/" + TABLE_PATH_PICTURE + "/");

    private static final String FIRST_VENDOR = "vnd.android.cursor.";
    private static final String TYPE_DIR = "dir";
    private static final String TYPE_ITEM = "item";
    private static final String SUB_VENDOR = "/vnd.";

    public static final String CONTENT_PROVIDER_MIME = FIRST_VENDOR + TYPE_DIR + SUB_VENDOR + AUTHORITY + ".PostData";
    public static final String CONTENT_PROVIDER_MIME_ITEM = FIRST_VENDOR + TYPE_ITEM + SUB_VENDOR + AUTHORITY + ".PostData";
}
