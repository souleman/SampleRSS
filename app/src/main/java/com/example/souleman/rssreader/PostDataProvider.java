package com.example.souleman.rssreader;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Souleman on 31/03/2016.
 */
public class PostDataProvider extends ContentProvider {

    private final static int CONTENT_PROVIDER_VERSION = 1;
    private final static int All_TABLE = 1;
    private final static int ITEM_TABLE = 2;
    private Database dbHelper;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(Contract.BASE_AUTHORITY, Database.POST_TABLE_NAME + "/", All_TABLE);
        sURIMatcher.addURI(Contract.BASE_AUTHORITY, Database.POST_TABLE_NAME + "/*", ITEM_TABLE);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new Database(getContext(), Database.NOM_FICHIER, null, CONTENT_PROVIDER_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = sURIMatcher.match(uri);

        if (TextUtils.isEmpty(selection)) {
            selection = "";
        }
        switch (match) {
            case ITEM_TABLE:
                long id = getId(uri);
                selection = selection + "" + Database.POST_KEY + "=" + id;
            case All_TABLE:
                return db.query(Database.POST_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public String getType(Uri uri) {
        int match = sURIMatcher.match(uri);
        switch (match) {
            case All_TABLE:
                return PostDataTable.CONTENT_PROVIDER_MIME;
            case ITEM_TABLE:
                return PostDataTable.CONTENT_PROVIDER_MIME_ITEM;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.insertOrThrow(Database.POST_TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int res;
        int match = sURIMatcher.match(uri);
        if (TextUtils.isEmpty(selection)) {
            selection = "";
        }

        switch (match) {
            case ITEM_TABLE:
                long id = getId(uri);
                selection = selection + " " + Database.POST_KEY + "=" + id;
            case All_TABLE:
                res = db.delete(Database.POST_TABLE_NAME, selection, selectionArgs);
                db.close();
                return res;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = sURIMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int res;
        if (TextUtils.isEmpty(selection)) {
            selection = "";
        }
        switch (match) {
            case ITEM_TABLE:
                long id = getId(uri);
                selection = selection + " " + Database.POST_KEY + "=" + id;
            case All_TABLE:
                res = db.update(Database.POST_TABLE_NAME, values, selection, selectionArgs);
                db.close();
                return res;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private long getId(Uri ContentUri) {
        String lastPathSegment = ContentUri.getLastPathSegment();
        if (lastPathSegment != null) {
            return Long.parseLong(lastPathSegment);
        }
        else{
            return -1;
        }
    }
}