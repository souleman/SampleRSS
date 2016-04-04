package com.example.souleman.rssreader;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Souleman on 31/03/2016.
 */
public class MyContentProvider extends ContentProvider {

    private static final String POST_KEY = PostDataDAO.POST_KEY;
    private static final String POST_TITLE = PostDataDAO.POST_TITLE;

    public static final String AUTHORITY = "com.example.souleman.rssreader.MyContentProvider";
    public static final String TABLE_PATH_PICTURE = PostDataDAO.TABLE_NAME;
    public static final Uri CONTENT_URI = Uri.parse("content://" +AUTHORITY + "/" +TABLE_PATH_PICTURE);
    public static final String CONTENT_PROVIDER_MIME = "vnd.android.cursor.dir/vnd.com.example.souleman.rssreader";

    private final static int CONTENT_PROVIDER_VERSION = 1;
    private final static String CONTENT_PROVIDER_NOM_FICHIER = PostDataDAO.NOM_FICHIER;

    private static final String CONTENT_PROVIDER_TABLE_NAME = PostDataDAO.TABLE_NAME;
    Databasehandler dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new Databasehandler(getContext(), CONTENT_PROVIDER_NOM_FICHIER, null,CONTENT_PROVIDER_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (id < 0) {
            return  db.query(CONTENT_PROVIDER_TABLE_NAME,
                    projection, selection, selectionArgs, null, null,
                    sortOrder);
        } else {
            return      db.query(CONTENT_PROVIDER_TABLE_NAME,
                    projection, POST_TITLE + "=" + id, null, null, null,
                    null);
        }    }


    @Override
    public String getType(Uri uri) {
        return CONTENT_PROVIDER_MIME;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            long id = db.insertOrThrow(CONTENT_PROVIDER_TABLE_NAME, null, values);

            if (id == -1) {
                throw new RuntimeException(String.format(
                        "%s : Failed to insert [%s] for unknown reasons.","RSSReader", values, uri));
            } else {
                return ContentUris.withAppendedId(uri, id);
            }

        } finally {
            db.close();
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (id < 0)
                return db.delete(
                        CONTENT_PROVIDER_TABLE_NAME,
                        selection, selectionArgs);
            else
                return db.delete(
                        CONTENT_PROVIDER_TABLE_NAME,
                        POST_KEY + "=" + id, selectionArgs);
        } finally {
            db.close();
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            if (id < 0)
                return db.update(CONTENT_PROVIDER_TABLE_NAME,values, selection, selectionArgs);
            else
                return db.update(CONTENT_PROVIDER_TABLE_NAME,
                        values, POST_KEY + "=" + id, null);
        } finally {
            db.close();
        }
    }

    public long getId(Uri ContentUri){
            String lastPathSegment = ContentUri.getLastPathSegment();
            if (lastPathSegment != null) {
                try {
                    return Long.parseLong(lastPathSegment);
                } catch (NumberFormatException e) {
                    Log.e("RssReader", "Number Format Exception : " + e);
                }
            }
            return -1;
        }

}

