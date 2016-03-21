package com.example.souleman.rssreader;

/**
 * Created by Souleman on 09/02/2016.
 */
public class PostData {
    private int postId;

    private String postTitle;
    private String postDate;
    private String postDescription;
    private String postImage;

    public PostData() {
    }

    public int getId() {
        return postId;
    }

    public void setId(int id) {
        this.postId = id;
    }

    public String getImage() {
        return postImage;
    }

    public void setImage(String image) {
        this.postImage = image;
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
        return postDescription;
    }

    public void setDescription(String description) {
        this.postDescription = description;
    }

}
