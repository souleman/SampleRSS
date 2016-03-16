package com.example.souleman.rssreader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Souleman on 04/03/2016.
 */
public class Databasehandler extends SQLiteOpenHelper {

    private static final String POST_KEY = "id";
    private static final String POST_TITLE = "titre";
    private static final String POST_DESCRIPTION = "description";
    private static final String POST_DATE = "data";
    private static final String POST_IMG = "image";

    private static final String POST_TABLE_NAME = "Postdatabase";

    private static final String POST_TABLE_CREATE =
            "CREATE TABLE " + POST_TABLE_NAME + " (" +
                    POST_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    POST_TITLE + " TEXT, " +
                    POST_DATE + " TEXT, " +
                    POST_DESCRIPTION + " TEXT, " +
                    POST_IMG + " TEXT);";

    private static final String POST_TABLE_DROP = "DROP TABLE IF EXISTS" + POST_TABLE_NAME + ";";

    public Databasehandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(POST_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //autant rien faire car si tu fais une mise a jour c'est pour ajouter une colonne ou autre.
        db.execSQL(POST_TABLE_DROP);
        onCreate(db);
    }
}
