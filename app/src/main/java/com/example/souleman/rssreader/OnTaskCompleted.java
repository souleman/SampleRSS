package com.example.souleman.rssreader;

import java.util.ArrayList;

/**
 * Created by Souleman on 21/03/2016.
 */
//Vu que tu l'utilise que dans RssDataController met la en inner class
public interface OnTaskCompleted {
    void onTaskCompleted(ArrayList<PostData> result);
}
