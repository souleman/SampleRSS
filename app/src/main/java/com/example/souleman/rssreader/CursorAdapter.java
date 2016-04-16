package com.example.souleman.rssreader;

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
public class CursorAdapter extends CursorRecyclerViewAdapter<CursorAdapter.ViewHolder> implements View.OnClickListener {
    private final Context context;
    private final RecyclerViewInterface listener;

    public CursorAdapter(Context context, RecyclerViewInterface mRVI) {
        super(null);
        this.listener = mRVI;
        this.context =  context;
    }

    @Override
    public void onClick(final View v) {
        Context context = v.getContext();
        //si tu fais un listerner c'est pour faire le traitement dans le listener.
        int position = listener.getPosition(v);

        if (position != RecyclerView.NO_POSITION) {
            final Cursor cursor = this.getItem(position);
            int id = cursor.getInt(cursor.getColumnIndex(Database.POST_KEY));
            Intent postViewdetails = new Intent(context, PostDetails.class);
            postViewdetails.putExtra(PostDetails.EXTRA_ID, id);
            context.startActivity(postViewdetails);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView postTitleView;
        final TextView postDateView;
        final ImageView postImageView;

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
        Picasso.with(context).load(String.valueOf((cursor.getString(cursor.getColumnIndex(Database.POST_IMG)))))
                .error(R.drawable.error)
                .into(viewHolder.postImageView);

        viewHolder.postTitleView.setText(cursor.getString(cursor.getColumnIndex(Database.POST_TITLE)));
        viewHolder.postDateView.setText(cursor.getString(cursor.getColumnIndex(Database.POST_DATE)));
    }
}