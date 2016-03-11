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
    // Merci de revoir les règles de nommage http://www.oracle.com/technetwork/java/codeconventions-135099.html
    // Et je suppose fortement qu'elles peuvent etre local
    private TextView Titre;
    private TextView Descritpion;
    private TextView Date;
    private ImageView Image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detailled_layout);

        Titre = (TextView) findViewById(R.id.PostTitre);
        Date = (TextView) findViewById(R.id.PostDate);
        Descritpion = (TextView) findViewById(R.id.PostDescritpion);
        Image = (ImageView) findViewById(R.id.PostImage);


        Bundle mBundle = this.getIntent().getExtras();
        //merci de mettre ceci en tant qu'attribut
        String pTitre = mBundle.getString("Titre");
        String pDate = mBundle.getString("Date");
        String pDescription = mBundle.getString("Description");
        String pImage = mBundle.getString("Image");

        //tu utilise du versionning alors supprime
//        byte [] bytes = mBundle.getByteArray("BMP");
//        Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        Titre.setText(pTitre);
        Date.setText(pDate);
        Descritpion.setText(pDescription);
//        Image.setImageBitmap(mBitmap);


        Picasso.with(this).load(pImage)
                .error(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher)
                .into(Image);
    }
}