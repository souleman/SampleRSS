package com.example.souleman.rssreader;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Souleman on 31/03/2016.
 */
public class MyContentProvider extends ContentProvider {

    private static final String AUTHORITY = "content://com.example.souleman.rssreader.MyContentProvider";
    private static final String Path = "com.example.souleman.rssreader.MyContentProvider";
    private static final String TABLE_PATH_PICTURE = PostDataDAO.TABLE_NAME;
    public static final Uri CONTENT_URI = Uri.parse( AUTHORITY + "/" + TABLE_PATH_PICTURE);
    public static final Uri CONTENT_URI_ITEM = Uri.parse(AUTHORITY + "/" + TABLE_PATH_PICTURE +"/");
    private static final String CONTENT_PROVIDER_MIME = "vnd.android.cursor.dir/vnd."+AUTHORITY +".PostData";
    private static final String CONTENT_PROVIDER_MIME_ITEM = "vnd.android.cursor.item/vnd."+AUTHORITY +".PostData";


    private final static int CONTENT_PROVIDER_VERSION = 1;

    private Databasehandler dbHelper;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Path, PostDataDAO.TABLE_NAME + "/",1);
        sURIMatcher.addURI(Path, PostDataDAO.TABLE_NAME + "/*", 2);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new Databasehandler(getContext(), PostDataDAO.NOM_FICHIER, null, CONTENT_PROVIDER_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = sURIMatcher.match(uri);
       switch (match)
        {
            case 1:
                return db.query(PostDataDAO.TABLE_NAME,
                        projection, selection, selectionArgs, null, null,
                        sortOrder);

            case 2:
                long id = getId(uri);
                    return db.query(PostDataDAO.TABLE_NAME, projection, PostDataDAO.POST_KEY + "=" + id, null, null, null, null);
            default:
                return db.query(PostDataDAO.TABLE_NAME,
                        projection, selection, selectionArgs, null, null,
                        sortOrder);
        }
    }


    @Override
    public String getType(Uri uri) {
        int match = sURIMatcher.match(uri);
        switch (match)
        {
            case 1:
                return CONTENT_PROVIDER_MIME;
            case 2:
                return CONTENT_PROVIDER_MIME_ITEM;
            default:
                return null;
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            long id = db.insertOrThrow(PostDataDAO.TABLE_NAME, null, values);

            if (id == -1) {
                throw new RuntimeException(String.format(
                        "%s : Failed to insert [%s] for unknown reasons.", "RSSReader", values, uri));
            } else {
                return ContentUris.withAppendedId(uri, id);
            }

        } finally {
            db.close();
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sURIMatcher.match(uri);
        switch (match)
        {
            case 1:
                return db.delete(
                        PostDataDAO.TABLE_NAME,
                        selection, selectionArgs);
        case 2:
            long id = getId(uri);
            return db.delete(
                    PostDataDAO.TABLE_NAME,
                    PostDataDAO.POST_KEY + "=" + id, selectionArgs);
            default:
                return db.delete(
                        PostDataDAO.TABLE_NAME,
                        selection, selectionArgs);
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            if (id < 0)
                return db.update(PostDataDAO.TABLE_NAME, values, selection, selectionArgs);
            else
                return db.update(PostDataDAO.TABLE_NAME,
                        values, PostDataDAO.POST_KEY + "=" + id, null);
        } finally {
            db.close();
        }
    }

    private long getId(Uri ContentUri) {
        String lastPathSegment = ContentUri.getLastPathSegment();
        if (lastPathSegment != null) {
            return Long.parseLong(lastPathSegment);
        }
        return -1;
    }

}