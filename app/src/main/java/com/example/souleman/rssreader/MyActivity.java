package com.example.souleman.rssreader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;

public class MyActivity extends Activity {
    //jamais un context static
    public static Context mContext;
    //depuis quand c'est static ?
    private static PostDataDAO mPostDataBase;
    //a supprimer car tu ne l'utilise pas
    private Handler handler = new Handler();
    private final String URL = "http://feeds.feedburner.com/elise/simplyrecipes";

    //depuis quand c'est static ? as tu compris ce que c'était static ?
    public static RecyclerView mRecyclerView;
    //attention tu as de nommage qui change pourquoi il ne le fait pas commencer par m, je sais que commencer par m était un truc de google il y 3 ans mais depuis il revient
    // un peu dessus car pour faire les getter setter automatiquement c'est chiant.
    //depuis quand c'est static ?
    public static RecyclerViewAdapter adapter;
    //depuis quand c'est static ?
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    //meme remarque attention le nommage
    //depuis quand c'est static ?
    public static ArrayList<PostData> listData = new ArrayList<PostData>();

    public MyActivity() {
        mContext = this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mContext = this;

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //pourquoi ne pas attendre d'avoir des données au lieu de demander de dessionner un truc vide ?
        adapter = new RecyclerViewAdapter(this, listData);
        mRecyclerView.setAdapter(adapter);

        //Attention tu fais des accès base, ceci devrait etre fait en asynchrone. Pense au cursorLoader.
        mPostDataBase = new PostDataDAO(this);
        mPostDataBase.open();

        ArrayList<PostData> oldPostDatas = new ArrayList<PostData>();
        //Tu utilise une base de données alors pense CursorLoader.
        oldPostDatas = mPostDataBase.GetAllPostData();

        if (oldPostDatas.size() != 0) {
            //Je ne suis pas trop Taost normalement avec je swiperefresh tu sais si ton app est entrain de ce mettre a jour
            Toast.makeText(mContext, "Uploadind data...", Toast.LENGTH_LONG).show();
            for (int i = 0; i < oldPostDatas.size(); i++) {
                listData.add(oldPostDatas.get(i));
            }
            adapter.notifyDataSetChanged();
        } else {
            //Toast inutile, si tu veux des infos pour toi pense au Logcat
            Toast.makeText(mContext, "1st Starting... loading data", Toast.LENGTH_SHORT).show();
            if (checkInternet()) {
                ExecuteMyTask();
            } else {
                // Actuelement c'est plus des snackbar qu'on doit utiliser.
                Toast.makeText(this, "Need network connection missing...", Toast.LENGTH_SHORT).show();
            }
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyRefreshingFunction();
            }
        });
    }

    //Tu ne fais rien avec le menu alors supprime le
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    //pourquoi en public ?
    //Sinon regarde ligne 70 tu fais un peu la meme chose
    public void MyRefreshingFunction() {
        if (checkInternet()) {
            //a supprimer
            //  Toast.makeText(this, "please wait until a next message...", Toast.LENGTH_SHORT).show();
            ExecuteMyTask();
        } else {
            Toast.makeText(this, "NetWork Connection missing...", Toast.LENGTH_SHORT).show();
        }
    }

    // RSS Reader Function
    public void ExecuteMyTask() {
        RssDataController geRss = new RssDataController();
        //Pourquoi un try catch ?
        try {
            geRss.execute(URL);
        } catch (Exception e) {
            Log.e("MyActivity", "Erreur Task not executed ");
        }
    }

    // Check Network connection
    // La doc peut t'aider http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            return true;
        } else {
            Toast.makeText(MyActivity.mContext, "Please make sur the Connexion is ON", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    // SAVE DATA SHARED PREFERENCE FUNCTION
    public static void SavePostData(ArrayList<PostData> mPostdata) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        //rien a faire ici.
        mPostDataBase.open();
        mPostDataBase.ajouter(mPostdata);
        mPostDataBase.close();
        //a supprimer l'utilisateur n'a pas besoin de savoir ca
        Toast.makeText(MyActivity.mContext, "DATA SAVED", Toast.LENGTH_LONG).show();

    }
}