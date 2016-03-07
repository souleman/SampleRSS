package com.example.souleman.rssreader;

/**
 * Created by Souleman on 09/02/2016.
 */
public class PostData {
    public int postId;

    public String postTitle;
    public String postDate;
    public String postDescritpion;
    public String postimage;
    public byte[] postImageByte;

 PostData(){}

 PostData(int mpostId,String mpostTitle, String mpostDate, String mpostDescritpion,String mpostimage,byte[] postImageByte)
 {
     setId(mpostId);
     setTitre(mpostTitle);
     setDate(mpostDate);
     setDescription(mpostDescritpion);
     setImage(mpostimage);
     setImageByte(postImageByte);
 }


    public int getId() {
        return postId;
    }

    public void setId(int id) {
        this.postId = id;
    }


    public byte[] getImageByte() {
        return postImageByte;
    }

    public void setImageByte(byte[] imageUrl) {
        this.postImageByte = imageUrl;
    }

    public String getImage() {
        return postimage;
    }

    public void setImage(String image) {
        this.postimage = image;
    }

    public String getTitre() {
        return postTitle;
    }

    public void setTitre(String titre) {
        this.postTitle = titre;
    }

    public String getDate() {
        return postDate;
    }

    public void setDate(String date) {
        this.postDate = date;
    }

    public String getDescription() {
        return postDescritpion;
    }

    public void setDescription(String description) {
        this.postDescritpion = description;
    }

}
