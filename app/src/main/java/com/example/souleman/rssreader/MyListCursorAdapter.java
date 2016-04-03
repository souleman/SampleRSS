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
public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder> implements  View.OnClickListener{
    private Activity myContext;
    private Cursor cursor;
    private RecyclerViewInterface listener;
    private OnItemClickListener onItemClickListener;

    public MyListCursorAdapter(Context context, Cursor cursor, RecyclerViewInterface mRVI){
        super(context,cursor);
        this.listener = mRVI;
        this.cursor = cursor;
        this.myContext = (Activity) context;

    }

    @Override
    public void onClick(final View v)
    {
        Context context = v.getContext();

        int position = listener.GetRecyclerViewPosition(v);
        cursor.moveToPosition(position);

        PostData postData = new PostData();
        postData.setTitre(cursor.getString(cursor.getColumnIndexOrThrow("titre")));
        postData.setDate(cursor.getString(cursor.getColumnIndexOrThrow("data")));
        postData.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
        postData.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));

        Intent postViewdetails = new Intent(context, PostDetails.class);
        postViewdetails.putExtra(PostDetails.EXTRA_TITRE, postData.getTitre());
        postViewdetails.putExtra(PostDetails.EXTRA_DATE, postData.getDate());
        postViewdetails.putExtra(PostDetails.EXTRA_DESCRIPTION, postData.getDescription());
        postViewdetails.putExtra(PostDetails.EXTRA_IMAGE, postData.getImage());
        context.startActivity(postViewdetails);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView postTitleView;
        TextView postDateView;
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


        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        PostData mPostdata = new PostData();
        mPostdata.setTitre(cursor.getString(cursor.getColumnIndexOrThrow("titre")));
        mPostdata.setDate(cursor.getString(cursor.getColumnIndexOrThrow("data")));
        mPostdata.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
        mPostdata.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));

        Picasso.with(myContext).load(String.valueOf((mPostdata.getImage())))
                .error(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher)
                .into(viewHolder.postImageView);

        viewHolder.postTitleView.setText(mPostdata.getTitre());
        viewHolder.postDateView.setText(mPostdata.getDate());
    }

    public interface OnItemClickListener
    {
        void onItemClicked(Cursor cursor);
    }
}
