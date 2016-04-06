package com.example.souleman.rssreader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Souleman on 04/03/2016.
 */
//inutile tu confond table et DAO
public class PostDataDAO {
    // URI de notre content provider, elle sera utilisé pour accéder au ContentProvider
    //Non utilisié
    public static final Uri CONTENT_URI = Uri.parse("content://com.example.souleman.rssreader.postDatabase");

    // Version de notre base de données
    private final static int VERSION = 1;
    // Nom de notre base de données
    public final static String NOM_FICHIER = "postDatabase.db";
    // Nom de la table de notre base
    public static final String TABLE_NAME = "postDatabase";

    public static final String POST_KEY = "id";
    public static final String POST_TITLE = "titre";
    public static final String POST_DESCRIPTION = "description";
    public static final String POST_DATE = "data";
    public static final String POST_IMG = "image";

    private SQLiteDatabase bdd = null;
    private Databasehandler handler = null;

    public PostDataDAO(Context context) {
        //Si je sais bien compter tu as fais 2 accès a ta base.
        handler = new Databasehandler(context, NOM_FICHIER, null, VERSION);
    }

    //Jamais utilisé
    public SQLiteDatabase open() {
        bdd = handler.getWritableDatabase();
        return bdd;
    }
}