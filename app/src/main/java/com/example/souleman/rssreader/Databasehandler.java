package com.example.souleman.rssreader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Souleman on 04/03/2016.
 */
public class Databasehandler extends SQLiteOpenHelper {

    public static final String POST_KEY = "id";
    public static final String POST_TITLE = "titre";
    public static final String POST_DESCRIPTION = "description";
    public static final String POST_DATE = "data";
    public static final String POST_IMG = "image";

    public static final String POST_TABLE_NAME ="Postdatabase";

    public static final String POST_TABLE_CREATE =
            "CREATE TABLE " + POST_TABLE_NAME + " ("+
                    POST_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    POST_TITLE + " TEXT, " +
                    POST_DATE + " TEXT, " +
                    POST_DESCRIPTION + " TEXT, " +
                    POST_IMG + " TEXT);" ;

    public static final String POST_TABLE_DROP = "DROP TABLE IF EXISTS" + POST_TABLE_NAME +";";

    public Databasehandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(POST_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(POST_TABLE_DROP);
        onCreate(db);
    }
}
