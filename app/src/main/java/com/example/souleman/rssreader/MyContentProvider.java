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
//Le nom MyContentProvider n'est pas top
public class MyContentProvider extends ContentProvider {
    //Prend exemple sur le gitlab de mon pote qui a fait un fichier Contract c'est pas mal au cas ou il a besoin de plusieurs provider
    private static final String AUTHORITY = "content://com.example.souleman.rssreader.MyContentProvider";
    private static final String Path = "com.example.souleman.rssreader.MyContentProvider";
    private static final String TABLE_PATH_PICTURE = PostDataDAO.TABLE_NAME;
    public static final Uri CONTENT_URI = Uri.parse(AUTHORITY + "/" + TABLE_PATH_PICTURE);
    public static final Uri CONTENT_URI_ITEM = Uri.parse(AUTHORITY + "/" + TABLE_PATH_PICTURE + "/");
    //Normalement c'est PostDataDAO.TABLE_NAME et non PostData si je suis bien ta logique.
    private static final String CONTENT_PROVIDER_MIME = "vnd.android.cursor.dir/vnd." + AUTHORITY + ".PostData";
    private static final String CONTENT_PROVIDER_MIME_ITEM = "vnd.android.cursor.item/vnd." + AUTHORITY + ".PostData";


    private final static int CONTENT_PROVIDER_VERSION = 1;

    private Databasehandler dbHelper;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        //Regarde la donc c'est l'authority qui est demandé, pour le code peut etre faire une variable static pour que
        // cela devient parlant et pour pas que tu te trompe dans les switchs case
        sURIMatcher.addURI(Path, PostDataDAO.TABLE_NAME + "/", 1);
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
        //Regarde le gitlab de mon pote c'est très bien fait, tu n'es pas obligé de faire la découpe en delegate mais pour ca tu as juste
        //a regrouper les fichiers.
        switch (match) {
            case 1:
                return db.query(PostDataDAO.TABLE_NAME,
                        projection, selection, selectionArgs, null, null,
                        sortOrder);

            case 2:
                long id = getId(uri);
                //Problème avec la selection si je fais une selection dans ma requete en plus de l'uri precis.
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
        switch (match) {
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
        //Tu fais un try sans catch et pour faire une exception très très bizarre ton fonctionnement
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
        //Regarde le gitlab de mon pote, pour pas faire un return a chaque fois car ici tu as un problème si je fais une selection sur une requete d'identifiant.
        switch (match) {
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
                //Je ne suis pas du default car tu fais quelque chose alors que ton uri n'est pas gérer encore une fois regarde le gitlab de mon pote.
                return db.delete(
                        PostDataDAO.TABLE_NAME,
                        selection, selectionArgs);
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //Problème si je fais un update de la table
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Encore un fois un try sans catch et un problème de selection. si je fais un update sur un identifiant.
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