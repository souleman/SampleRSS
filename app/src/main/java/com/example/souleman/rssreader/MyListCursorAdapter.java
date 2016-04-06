package com.example.souleman.rssreader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Souleman on 24/03/2016.
 */
//Attention au nom
public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder> implements View.OnClickListener {
    //attention au nom
    // Doit etre final je suposse
    private Activity myContext;
    // Doit etre final je suposse
    private RecyclerViewInterface listener;

    public MyListCursorAdapter(Context context, RecyclerViewInterface mRVI) {
        super(context, null);
        this.listener = mRVI;
        this.myContext = (Activity) context;
    }

    @Override
    public void onClick(final View v) {
        Context context = v.getContext();
        int position = listener.GetRecyclerViewPosition(v);

        if (position != RecyclerView.NO_POSITION) {
            final Cursor cursor = this.getItem(position);
            int id = cursor.getInt(cursor.getColumnIndex(PostDataDAO.POST_KEY));
            Intent postViewdetails = new Intent(context, PostDetails.class);
            postViewdetails.putExtra(PostDetails.EXTRA_ID, id);
            context.startActivity(postViewdetails);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Doit etre final je suposse
        TextView postTitleView;
        // Doit etre final je suposse
        TextView postDateView;
        // Doit etre final je suposse
        ImageView postImageView;

        public ViewHolder(View v) {
            super(v);
            this.postImageView = (ImageView) v.findViewById(R.id.postThumb);
            this.postTitleView = (TextView) v.findViewById(R.id.postTitleLabel);
            this.postDateView = (TextView) v.findViewById(R.id.postDateLabel);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, null);
        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        //Ca sert a rien de faire un objet ici.
        PostData mPostdata = new PostData();
        //C'est très couteux de récupérer l'index pense a le faire qu'une fois par exemple dans le swapCursor
        mPostdata.setTitre(cursor.getString(cursor.getColumnIndex(PostDataDAO.POST_TITLE)));
        mPostdata.setDate(cursor.getString(cursor.getColumnIndex(PostDataDAO.POST_DATE)));
        mPostdata.setDescription(cursor.getString(cursor.getColumnIndex(PostDataDAO.POST_DESCRIPTION)));
        mPostdata.setImage(cursor.getString(cursor.getColumnIndex(PostDataDAO.POST_IMG)));

        //Attention avec ton placeholder et ton error
        Picasso.with(myContext).load(String.valueOf((mPostdata.getImage())))
                .error(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher)
                .into(viewHolder.postImageView);

        viewHolder.postTitleView.setText(mPostdata.getTitre());
        viewHolder.postDateView.setText(mPostdata.getDate());
    }
}