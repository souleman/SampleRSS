package com.example.souleman.rssreader;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Souleman on 09/02/2016.
 */
public class RssDataController extends AsyncTask<Context, Integer, ArrayList<PostData>> {
    public static final String TITLE = "title";
    public static final String PUBDATE = "pubDate";
    public static final String DESCRIPTION = "description";
    public static final String ITEM = "item";


    private final OnTaskCompleted listener;

    public RssDataController(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onPostExecute(ArrayList<PostData> result) {
        listener.onTaskCompleted(result);
    }

    @Override
    protected ArrayList<PostData> doInBackground(Context... contexts) {
        final String URL = "http://feeds.feedburner.com/elise/simplyrecipes";

        ArrayList<PostData> StreamRSS = new ArrayList<>();
        HttpURLConnection urlConnection = null;

        URL url;
        Context mContext = contexts[0];
        try {
            url = new URL(URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
            Document doc = docBuild.parse(in);

            urlConnection.setReadTimeout(30 * 1000); // 10 sec
            urlConnection.setConnectTimeout(30 * 1000); // 10 sec

            //La prochaine étape est de supprimer ca pour le remplacer par retrofit ou autre pour que ce soit fait proprement
            int cleanCounter1 = doc.getElementsByTagName(ITEM).getLength();
            mContext.getContentResolver().delete(PostDataTable.CONTENT_URI, null, null);
            for (int i = 0; i < cleanCounter1; i++) {
                PostData rss = new PostData();
                rss.setId(i + 1);

                int cleanCounter2 = doc.getElementsByTagName(ITEM).item(i).getChildNodes().getLength();
                for (int j = 0; j < cleanCounter2; j++) {
                    if (doc.getElementsByTagName(ITEM).item(i).getChildNodes().item(j).getNodeName() != null) {
                        String s = doc.getElementsByTagName(ITEM).item(i).getChildNodes().item(j).getNodeName();
                        Document document = ((Document) doc);
                        switch (s) {
                            case TITLE:
                                rss.setTitre(document.getElementsByTagName(ITEM).item(i).getChildNodes().item(j).getTextContent());

                                break;
                            case PUBDATE:
                                rss.setDate(document.getElementsByTagName(ITEM).item(i).getChildNodes().item(j).getTextContent());
                                break;

                            // Description et images
                            case DESCRIPTION:
                                int start = document.getElementsByTagName(ITEM).item(i).getChildNodes().item(j).getTextContent().indexOf("<p>");
                                int end = document.getElementsByTagName(ITEM).item(i).getChildNodes().item(j).getTextContent().indexOf("<", start + 3);
                                rss.setDescription(document.getElementsByTagName(ITEM).item(i).getChildNodes().item(j).getTextContent().substring(start + 3, end));

                                //pour l'image on réutilise les variable start et end
                                start = document.getElementsByTagName(ITEM).item(i).getChildNodes().item(j).getTextContent().indexOf("src=\"");
                                end = document.getElementsByTagName(ITEM).item(i).getChildNodes().item(j).getTextContent().indexOf("class=\"");

                                rss.setImage(document.getElementsByTagName(ITEM).item(i).getChildNodes().item(j).getTextContent().substring(start + 5, end - 2));
                                break;
                        }
                    }
                }
                ContentValues content = new ContentValues();
                content.put(Database.POST_TITLE, "TITRE");
                content.put(Database.POST_DATE, rss.getDate());
                content.put(Database.POST_DESCRIPTION, rss.getDescription());
                content.put(Database.POST_IMG, rss.getImage());
                mContext.getContentResolver().insert(PostDataTable.CONTENT_URI, content);

                StreamRSS.add(rss);
            }
            in.close();

        }
        //Tu traite de la meme manière les catchs c'est utile ?
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        return StreamRSS;
    }
}