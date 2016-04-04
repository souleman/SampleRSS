package com.example.souleman.rssreader;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Souleman on 11/02/2016.
 */
public class PostDetails extends Activity {
    public static final String EXTRA_TITRE = "Titre";
    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_IMAGE = "imgae";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detailled_layout);

        TextView titre = (TextView) findViewById(R.id.PostTitre);
        TextView date = (TextView) findViewById(R.id.PostDate);
        TextView description = (TextView) findViewById(R.id.PostDescription);
        ImageView image = (ImageView) findViewById(R.id.PostImage);

        Bundle postDetailsBundle = this.getIntent().getExtras();

        String postDetailsTitre = postDetailsBundle.getString(EXTRA_TITRE);
        String postDetailsDate = postDetailsBundle.getString(EXTRA_DATE);
        String postDetailsDescription = postDetailsBundle.getString(EXTRA_DESCRIPTION);
        String postDetailsImage = postDetailsBundle.getString(EXTRA_IMAGE);

        titre.setText(postDetailsTitre);
        date.setText(postDetailsDate);
        description.setText(postDetailsDescription);

        Picasso.with(this).load(postDetailsImage)
                .error(R.drawable.error)
                .placeholder(Color.WHITE)
                .into(image);
    }
}