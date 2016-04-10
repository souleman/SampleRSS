package com.example.souleman.rssreader;

import android.net.Uri;

/**
 * Created by Souleman on 08/04/2016.
 */

public class PostDataTable {
    public static final String TABLE_PATH_PICTURE = Database.POST_TABLE_NAME;
    public static final String AUTHORITY = Contract.BASE_AUTHORITY + "MyContentProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse(Contract.BASE_CONTENT_URI + AUTHORITY + "/" + TABLE_PATH_PICTURE);
    public static final Uri CONTENT_URI_ITEM = Uri.parse(Contract.BASE_CONTENT_URI + AUTHORITY + "/" + TABLE_PATH_PICTURE + "/");

    private static final String SUB_VENDOR = Contract.SUB_VENDOR + AUTHORITY;

    public static final String CONTENT_PROVIDER_MIME = Contract.FIRST_VENDOR + Contract.TYPE_DIR + SUB_VENDOR + ".PostData";;
    public static final String CONTENT_PROVIDER_MIME_ITEM = Contract.FIRST_VENDOR + Contract.TYPE_ITEM + SUB_VENDOR + ".PostData";;
}
