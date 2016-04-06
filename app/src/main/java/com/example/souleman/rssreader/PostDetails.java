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

        getLoaderManager().initLoader(postDetailsId,null,this);



//        GetCursorFromDataBase myDBCursor = new GetCursorFromDataBase();
//        myDBCursor.execute(uri);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_ITEM + "" + id);

        return new CursorLoader(
                getBaseContext(),                   // Parent activity context
                uri,      // Table to query
                MyActivity.POSTDATA_SUMMARY_PROJECTION,        // Projection to return
                null,                               // No selection clause
                null,                               // No selection arguments
                null                                // Default sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        c.moveToFirst();
        String postDetailsTitre = c.getString(c.getColumnIndex(PostDataDAO.POST_TITLE));
        String postDetailsDate =  c.getString(c.getColumnIndex(PostDataDAO.POST_DATE));
        String postDetailsDescription =  c.getString(c.getColumnIndex(PostDataDAO.POST_DESCRIPTION));
        String postDetailsImage =  c.getString(c.getColumnIndex(PostDataDAO.POST_IMG));
        c.close();

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


/*
    private class GetCursorFromDataBase extends AsyncTask<Uri,Integer, String []>
    {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String [] result) {
            SetData(result);
        }

        @Override
        protected String [] doInBackground(Uri... params) {
            Uri uri = params[0];
            Cursor c =  getContentResolver().query(uri, MyActivity.POSTDATA_SUMMARY_PROJECTION, null, null, null);
            c.moveToFirst();

            String postDetailsTitre = c.getString(c.getColumnIndex(PostDataDAO.POST_TITLE));
            String postDetailsDate =  c.getString(c.getColumnIndex(PostDataDAO.POST_DATE));
            String postDetailsDescription =  c.getString(c.getColumnIndex(PostDataDAO.POST_DESCRIPTION));
            String postDetailsImage =  c.getString(c.getColumnIndex(PostDataDAO.POST_IMG));
            String [] postDataAllDetails = new String[]{
                    postDetailsTitre,
                    postDetailsDate,
                    postDetailsDescription,
                    postDetailsImage,
            };
            c.close();
            return postDataAllDetails;
        }
    }

    private void SetData(String[] result) {
        titre.setText(result[0]);
        date.setText(result[1]);
        description.setText(result[2]);

        Picasso.with(this).load(result[3])
                .error(R.drawable.error)
                .into(image);
    }

*/

}