package com.example.souleman.rssreader;

/**
 * Created by Souleman on 09/02/2016.
 */
public class PostData {
    private int postId;

    private String postTitle;
    private String postDate;
    private String postDescritpion;
    private String postimage;

     public PostData() {
    }

    public int getId() {
        return postId;
    }

    public void setId(int id) {
        this.postId = id;
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
