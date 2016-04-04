package com.example.souleman.rssreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Souleman on 04/03/2016.
 */
public class PostDataDAO {


    // URI de notre content provider, elle sera utilisé pour accéder au ContentProvider
    public static final Uri CONTENT_URI = Uri.parse("content://com.example.souleman.rssreader.postDatabase");

    // Version de notre base de données
    private final static int VERSION = 1;
    // Nom de notre base de données
    public final static String NOM_FICHIER = "postDatabase.db";
    // Nom de la table de notre base
    public static final String TABLE_NAME = "Postdatabase";

    public static final String POST_KEY = "id";
    public static final String POST_TITLE = "titre";
    public static final String POST_DESCRIPTION = "description";
    public static final String POST_DATE = "data";
    public static final String POST_IMG = "image";

    private static final int NUM_KEY = 0;
    private static final int NUM_TITLE = 1;
    private static final int NUM_DESCRIPTION = 2;
    private static final int NUM_DATE = 3;
    private static final int NUM_IMG = 4;


    private SQLiteDatabase bdd = null;
    private Databasehandler handler = null;


    public PostDataDAO(Context context) {
        handler = new Databasehandler(context, NOM_FICHIER, null, VERSION);
    }

    public  void openForWrite() {
        bdd = handler.getWritableDatabase();
    }

    public  void openForRead() {
        bdd = handler.getReadableDatabase();
    }

    public SQLiteDatabase open() {
        bdd = handler.getWritableDatabase();
        return bdd;
    }

    public void close() {
        bdd.close();
    }

    public SQLiteDatabase getDB() {
        return bdd;
    }


    public void add(ArrayList<PostData> postdata) {
        delete();

        for (int i = 0; i < postdata.size(); i++) {
            ContentValues content = new ContentValues();
            content.put(POST_TITLE, postdata.get(i).getTitre());
            content.put(POST_DATE, postdata.get(i).getDate());
            content.put(POST_DESCRIPTION, postdata.get(i).getDescription());
            content.put(POST_IMG, postdata.get(i).getImage());
            bdd.insert(TABLE_NAME, null, content);
        }
    }

    public PostData cursorToPostData(Cursor c){
        if(c.getCount() == 0){
            c.close();
            return null;
        }
        PostData postData = new PostData();
        postData.setId(c.getInt(NUM_KEY));
        postData.setTitre(c.getString(NUM_TITLE));
        postData.setDate(c.getString(NUM_DATE));
        postData.setDescription(c.getString(NUM_DESCRIPTION));
        postData.setImage(c.getString(NUM_IMG));
        c.close();
        return postData;
    }

    private void delete() {

            bdd.delete(TABLE_NAME, null, null);
    }

    public ArrayList<PostData> GetAllPostData() {
        ArrayList<PostData> postDataList = new ArrayList<PostData>();

        Cursor c = bdd.query(TABLE_NAME, new String[]{POST_KEY, POST_TITLE, POST_DATE, POST_DESCRIPTION, POST_IMG}, null, null, null, null, null);

        while (c.moveToNext()) {
            PostData mPd = new PostData();
            mPd.setId(c.getInt(NUM_KEY));

            mPd.setTitre(c.getString(NUM_TITLE));
            mPd.setDate(c.getString(NUM_DATE));
            mPd.setDescription(c.getString(NUM_DESCRIPTION));
            mPd.setImage(c.getString(NUM_IMG));
            postDataList.add(mPd);
        }
        c.close();
        return postDataList;
    }

    private class addAsyncTask extends AsyncTask<ArrayList<PostData>, Integer,Boolean> {
        @Override
        protected Boolean doInBackground(ArrayList<PostData>... params) {
            ArrayList<PostData> postdata = params[0];
            for (int i = 0; i < postdata.size(); i++) {
                ContentValues content = new ContentValues();
                content.put(POST_TITLE, postdata.get(i).getTitre());
                content.put(POST_DATE, postdata.get(i).getDate());
                content.put(POST_DESCRIPTION, postdata.get(i).getDescription());
                content.put(POST_IMG, postdata.get(i).getImage());
                bdd.insert(TABLE_NAME, null, content);
            }
            return true;
        }
    }

    private long getId(Uri uri) {
        String lastPathSegment = uri.getLastPathSegment();
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