package com.example.souleman.rssreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Souleman on 04/03/2016.
 */
public class PostDataDAO {
    //Revois la visibilité des attributs car normalement tout devrais etre private.

    protected final static int VERSION = 1;
    protected final static String NOM_FICHIER = "postDatabase.db";

    public static final String TABLE_NAME ="Postdatabase";

    public static final String POST_KEY = "id";
    public static final String POST_TITLE = "titre";
    public static final String POST_DESCRIPTION = "description";
    public static final String POST_DATE = "data";
    public static final String POST_IMG = "image";


    public static final int Num_KEY = 0;
    public static final int Num_TITLE = 1;
    public static final int Num_DESCRIPTION = 2;
    public static final int Num_DATE = 3;
    public static final int Num_IMG = 4;


    public SQLiteDatabase mDB = null;
    public Databasehandler mHandler = null;


    public PostDataDAO(Context context){
        mHandler = new Databasehandler(context, NOM_FICHIER, null, VERSION);
    }

    public SQLiteDatabase open(){
        mDB = mHandler.getWritableDatabase();
        return mDB;
    }

    public void close(){
        mDB.close();
    }

    public SQLiteDatabase getDB(){
        return mDB;
    }


    public void ajouter(ArrayList<PostData> postdata)
    {

        ArrayList<PostData> mPostDataDelete = new ArrayList<PostData>();
        mPostDataDelete = GetAllPostData();
        supprimer(mPostDataDelete);

        for (int i=0; i<postdata.size();i++){
            ContentValues content = new ContentValues();
            content.put(POST_TITLE,postdata.get(i).getTitre());
            content.put(POST_DATE,postdata.get(i).getDate());
            content.put(POST_DESCRIPTION,postdata.get(i).getDescription());
            content.put(POST_IMG, postdata.get(i).getImage());
            mDB.insert(TABLE_NAME,null,content);
        }
    }


    public void supprimer(ArrayList<PostData> postdata) {
        for (int i = 0; i < postdata.size(); i++) {
            mDB.delete(TABLE_NAME, POST_TITLE + " = ?", new String[]{postdata.get(i).getTitre()});
        }
    }

    public ArrayList<PostData> GetAllPostData()
    {
        ArrayList<PostData> postDataList = new ArrayList<PostData>();

        Cursor c = mDB.query(TABLE_NAME,new String[]{POST_KEY,POST_TITLE,POST_DATE,POST_DESCRIPTION,POST_IMG},null,null,null,null,null);
        if(c.getCount() == 0){
        }

        while (c.moveToNext()){
            PostData mPd = new PostData();
            mPd.setId(c.getInt(Num_KEY));

            mPd.setTitre(c.getString(Num_TITLE));
            mPd.setDate(c.getString(Num_DATE));
            mPd.setDescription(c.getString(Num_DESCRIPTION));
            mPd.setImage(c.getString(Num_IMG));
            postDataList.add(mPd);
        }
        c.close();
        return postDataList;
    }
}