package com.example.souleman.rssreader;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Souleman on 11/02/2016.
 */
public class PostDetails extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String EXTRA_ID = "id";
    private TextView titre;
    private TextView date;
    private TextView description;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detailled_layout);

        titre = (TextView) findViewById(R.id.PostTitre);
        date = (TextView) findViewById(R.id.PostDate);
        description = (TextView) findViewById(R.id.PostDescription);
        image = (ImageView) findViewById(R.id.PostImage);

        Bundle postDetailsBundle = this.getIntent().getExtras();
        int postDetailsId = postDetailsBundle.getInt(EXTRA_ID);

        getLoaderManager().initLoader(postDetailsId, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse(Contract.CONTENT_URI_ITEM + "" + id);
        return new CursorLoader(
                getApplicationContext(),                    // Parent activity context
                uri,                                        // Table to query
                POSTDATA_DETAILS_PROJECTION,                // Projection to return
                null,                                       // No selection clause
                null,                                       // No selection arguments
                null                                        // Default sort order
        );
    }

    static final String[] POSTDATA_DETAILS_PROJECTION = new String[]{
            Database.POST_TITLE,
            Database.POST_DESCRIPTION,
            Database.POST_DATE,
            Database.POST_IMG,
    };


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        c.moveToFirst();
        String postDetailsTitre = c.getString(c.getColumnIndex(Database.POST_TITLE));
        String postDetailsDate = c.getString(c.getColumnIndex(Database.POST_DATE));
        String postDetailsDescription = c.getString(c.getColumnIndex(Database.POST_DESCRIPTION));
        String postDetailsImage = c.getString(c.getColumnIndex(Database.POST_IMG));

        titre.setText(postDetailsTitre);
        date.setText(postDetailsDate);
        description.setText(postDetailsDescription);

        Picasso.with(this).load(postDetailsImage)
                .error(R.drawable.error)
                .into(image);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}