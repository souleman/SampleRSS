package com.example.souleman.rssreader;

/**
 * Created by Souleman on 07/04/2016.
 */

public class Contract {
    public static final String BASE_AUTHORITY = "com.example.souleman.rssreader.";
    public static final String BASE_CONTENT_URI = "content://";

    public static final String FIRST_VENDOR = "vnd.android.cursor.";
    public static final String TYPE_DIR = "dir";
    public static final String TYPE_ITEM = "item";
    public static final String SUB_VENDOR = "/vnd.";

    public static final String CONTENT_PROVIDER_MIME = FIRST_VENDOR + TYPE_DIR + SUB_VENDOR;
    public static final String CONTENT_PROVIDER_MIME_ITEM = FIRST_VENDOR + TYPE_ITEM + SUB_VENDOR;
/*
    public static final String BASE_AUTHORITY = "com.example.souleman.rssreader.MyContentProvider";
    private static final String CONTENT = "content://";
    public static final String TABLE_PATH_PICTURE = Database.POST_TABLE_NAME;

    public static final Uri CONTENT_URI =      Uri.parse(CONTENT +BASE_AUTHORITY + "/" + TABLE_PATH_PICTURE);
    public static final Uri CONTENT_URI_ITEM = Uri.parse(CONTENT + BASE_AUTHORITY + "/" + TABLE_PATH_PICTURE + "/");

    private static final String FIRST_VENDOR = "vnd.android.cursor.";
    private static final String TYPE_DIR = "dir";
    private static final String TYPE_ITEM = "item";
    private static final String SUB_VENDOR = "/vnd.";

    public static final String CONTENT_PROVIDER_MIME = FIRST_VENDOR + TYPE_DIR + SUB_VENDOR + BASE_AUTHORITY + ".PostData";
    public static final String CONTENT_PROVIDER_MIME_ITEM = FIRST_VENDOR + TYPE_ITEM + SUB_VENDOR + BASE_AUTHORITY + ".PostData";
*/
}


