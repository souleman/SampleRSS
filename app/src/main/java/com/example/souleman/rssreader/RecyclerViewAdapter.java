package com.example.souleman.rssreader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Souleman on 02/03/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Activity myContext;
    private ArrayList<PostData> datas;


    public static class ViewHolder extends RecyclerView.ViewHolder{
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

    public RecyclerViewAdapter(Context context, ArrayList<PostData> objects){
        this.datas = objects;
        this.myContext = (Activity) context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem,null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                int position = MyActivity.mRecyclerView.getChildPosition(v);
                PostData postData = MyActivity.listData.get(position);

                Intent postViewdetails =  new Intent(context, PostDetails.class);
                postViewdetails.putExtra("Titre",postData.getTitre());
                postViewdetails.putExtra("Date",postData.getDate());
                postViewdetails.putExtra("Description",postData.getDescription());
                postViewdetails.putExtra("Image",postData.getImage());
                context.startActivity(postViewdetails);
            }
        });

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
    PostData mPostdata = datas.get(i);

        //Download image using picasso library
        Picasso.with(myContext).load(String.valueOf((mPostdata.getImage())))
                .error(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher)
                .into(viewHolder.postImageView);

        viewHolder.postTitleView.setText(mPostdata.getTitre());
        viewHolder.postDateView.setText(mPostdata.getDate());
    }

    @Override
    public int getItemCount() {
        return (null != datas ? datas.size() : 0);
    }
}
