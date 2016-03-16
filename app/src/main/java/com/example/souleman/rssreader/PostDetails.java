package com.example.souleman.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Souleman on 11/02/2016.
 */
public class PostDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detailled_layout);

        TextView Titre = (TextView) findViewById(R.id.PostTitre);
        TextView Date = (TextView) findViewById(R.id.PostDate);
        TextView Descritpion = (TextView) findViewById(R.id.PostDescritpion);
        ImageView Image = (ImageView) findViewById(R.id.PostImage);


        Bundle mBundle = this.getIntent().getExtras();

        String pTitre = mBundle.getString(RecyclerViewAdapter.TITRE);
        String pDate = mBundle.getString(RecyclerViewAdapter.DATE);
        String pDescription = mBundle.getString(RecyclerViewAdapter.DESCRIPTION);
        String pImage = mBundle.getString(RecyclerViewAdapter.IMAGE);

        Titre.setText(pTitre);
        Date.setText(pDate);
        Descritpion.setText(pDescription);

        Picasso.with(this).load(pImage)
                .error(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher)
                .into(Image);
    }
}