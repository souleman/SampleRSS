package com.example.souleman.rssreader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Souleman on 04/03/2016.
 */
public class Database extends SQLiteOpenHelper {
    // Version de notre base de données
    //Tu ne l'utilise pas
    public final static int VERSION = 1;
    // Nom de notre base de données
    public final static String NOM_FICHIER = "postDatabase.db";
    // Nom de la table de notre base
    public static final String POST_TABLE_NAME = "postDatabase";

    //Rien a faire ici, Comment tu fais si tu as 100 tables ?
    public static final String POST_KEY = "id";
    public static final String POST_TITLE = "titre";
    public static final String POST_DESCRIPTION = "description";
    public static final String POST_DATE = "data";
    public static final String POST_IMG = "image";

    private static final String POST_TABLE_CREATE =
            "CREATE TABLE " + POST_TABLE_NAME + " (" +
                    POST_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    POST_TITLE + " TEXT, " +
                    POST_DATE + " TEXT, " +
                    POST_DESCRIPTION + " TEXT, " +
                    POST_IMG + " TEXT);";

    private static final String POST_TABLE_DROP = "DROP TABLE IF EXISTS" + POST_TABLE_NAME + ";";


    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
