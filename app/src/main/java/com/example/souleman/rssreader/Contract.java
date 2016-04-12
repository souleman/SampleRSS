package com.example.souleman.rssreader;

/**
 * Created by Souleman on 07/04/2016.
 */

public class Contract {
    public static final String BASE_AUTHORITY = "com.example.souleman.rssreader.";
    //Normalement les gens n'ont pas de problème avec le copié collé :)
    //public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + BASE_AUTHORITY);
    public static final String BASE_CONTENT_URI = "content://";

    public static final String FIRST_VENDOR = "vnd.android.cursor.";
    public static final String TYPE_DIR = "dir";
    public static final String TYPE_ITEM = "item";
    public static final String SUB_VENDOR = "/vnd.";

    public static final String CONTENT_PROVIDER_MIME = FIRST_VENDOR + TYPE_DIR;
    public static final String CONTENT_PROVIDER_MIME_ITEM = FIRST_VENDOR + TYPE_ITEM;
}


