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

    private RecyclerViewInterface listener;


    private Activity myContext;
    //attention nommage
    private ArrayList<PostData> datas;

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

    public RecyclerViewAdapter(Context context, ArrayList<PostData> objects,RecyclerViewInterface listener) {
        this.datas = objects;
        this.listener = listener;
        this.myContext = (Activity) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem, null);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //pense callback ca t'aidera a mieux structure ton code et enlever les static que je suppose tu n'as pas compris les risques
                Context context = v.getContext();

                int position = listener.GetRecyclerViewPosition(v);
                PostData postData = listener.GetSelectedPostData(position);

                Intent postViewdetails = new Intent(context, PostDetails.class);
                postViewdetails.putExtra(PostDetails.EXTRA_TITRE, postData.getTitre());
                postViewdetails.putExtra(PostDetails.EXTRA_DATE, postData.getDate());
                postViewdetails.putExtra(PostDetails.EXTRA_DESCRIPTION, postData.getDescription());
                postViewdetails.putExtra(PostDetails.EXTRA_IMAGE, postData.getImage());
                context.startActivity(postViewdetails);
            }
        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PostData mPostdata = datas.get(i);

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
