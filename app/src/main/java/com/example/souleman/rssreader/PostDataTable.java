package com.example.souleman.rssreader;

import android.net.Uri;

/**
 * Created by Souleman on 08/04/2016.
 */

public class PostDataTable {
    public static final String TABLE_PATH_PICTURE = Database.POST_TABLE_NAME;
    //C'est quoi ca ?
    public static final String AUTHORITY = Contract.BASE_AUTHORITY + "MyContentProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse(Contract.BASE_CONTENT_URI + AUTHORITY + "/" + TABLE_PATH_PICTURE);
    public static final Uri CONTENT_URI_ITEM = Uri.parse(Contract.BASE_CONTENT_URI + AUTHORITY + "/" + TABLE_PATH_PICTURE + "/");

    private static final String SUB_VENDOR = Contract.SUB_VENDOR + AUTHORITY;

    public static final String CONTENT_PROVIDER_MIME = Contract.CONTENT_PROVIDER_MIME + SUB_VENDOR + ".PostData";
    ;
    public static final String CONTENT_PROVIDER_MIME_ITEM = Contract.CONTENT_PROVIDER_MIME_ITEM + SUB_VENDOR + ".PostData";
    ;

    //Tu as du mal a t'inspirer des examples que je te donne
//    public static final String TABLE_NAME = "PostData";
//    private static final Uri CONTENT_URI = Contract.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
//    public static final String CONTENT_PROVIDER_MIME = Contract.CONTENT_PROVIDER_MIME + TABLE_NAME;
//    public static final String CONTENT_PROVIDER_MIME_ITEM = Contract.CONTENT_PROVIDER_MIME_ITEM + TABLE_NAME;
//
//    public static Uri buildUri() {
//        return CONTENT_URI;
//    }
//
//    public static Uri buildItemUri(int id) {
//        return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
//    }
}
