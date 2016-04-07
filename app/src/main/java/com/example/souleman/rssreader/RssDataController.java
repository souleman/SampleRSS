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
            mContext.getContentResolver().delete(Contract.CONTENT_URI, null, null);
            url = new URL(URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
            Document doc = docBuild.parse(in);

            urlConnection.setReadTimeout(30 * 1000); // 10 sec
            urlConnection.setConnectTimeout(30 * 1000); // 10 sec

            int cleanCounter1 = doc.getElementsByTagName("item").getLength();
            for (int i = 0; i < cleanCounter1; i++) {
                PostData rss = new PostData();
                rss.setId(i + 1);

                int cleanCounter2 = doc.getElementsByTagName("item").item(i).getChildNodes().getLength();
                for (int j = 0; j < cleanCounter2; j++) {
                    if (doc.getElementsByTagName("item").item(i).getChildNodes().item(j).getNodeName() != null) {
                        String s = doc.getElementsByTagName("item").item(i).getChildNodes().item(j).getNodeName();
                        Document document = ((Document) doc);
                        switch (s) {
                            case "title":
                                rss.setTitre(document.getElementsByTagName("item").item(i).getChildNodes().item(j).getTextContent());

                                break;
                            case "pubDate":
                                rss.setDate(document.getElementsByTagName("item").item(i).getChildNodes().item(j).getTextContent());
                                break;

                            // Description et images
                            case "description":
                                int start = document.getElementsByTagName("item").item(i).getChildNodes().item(j).getTextContent().indexOf("<p>");
                                int end = document.getElementsByTagName("item").item(i).getChildNodes().item(j).getTextContent().indexOf("<", start + 3);
                                rss.setDescription(document.getElementsByTagName("item").item(i).getChildNodes().item(j).getTextContent().substring(start + 3, end));

                                //pour l'image on réutilise les variable start et end
                                start = document.getElementsByTagName("item").item(i).getChildNodes().item(j).getTextContent().indexOf("src=\"");
                                end = document.getElementsByTagName("item").item(i).getChildNodes().item(j).getTextContent().indexOf("class=\"");

                                rss.setImage(document.getElementsByTagName("item").item(i).getChildNodes().item(j).getTextContent().substring(start + 5, end - 2));
                                break;
                        }
                    }
                }

                ContentValues content = new ContentValues();
                content.put(Database.POST_KEY, rss.getId());
                content.put(Database.POST_TITLE, rss.getTitre());
                content.put(Database.POST_DATE, rss.getDate());
                content.put(Database.POST_DESCRIPTION, rss.getDescription());
                content.put(Database.POST_IMG, rss.getImage());
                mContext.getContentResolver().insert(Contract.CONTENT_URI, content);

                StreamRSS.add(rss);
            }

            //Ca fait longtemps que je ne parce plus les objets moi meme mais ca devrait resembler a ceci
//            int eventType = xpp.getEventType();
//            Article feed = null;
//
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                if (eventType == XmlPullParser.START_TAG) {
//                    if (xpp.getName().equalsIgnoreCase(TAG_ITEM)) {
//                        feed = new Article();
//                    } else if (feed != null ) {
//                        if (xpp.getName().equalsIgnoreCase(TAG_TITLE)) {
//                            feed.setTitle(xpp.nextText());
//                        } else if (xpp.getName().equalsIgnoreCase(TAG_DESCRIPTION)) {
//                            feed.setDescription(xpp.nextText());
//                        } else if (xpp.getName().equalsIgnoreCase(TAG_PUB_DATE)) {
//                            feed.setDate(xpp.nextText());
//                        } else if (xpp.getName().equalsIgnoreCase(TAG_CONTENT_ENCODED)) {
//                            feed.setEncodedContent(xpp.nextText());
//                        }
//                    }
//                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase(TAG_ITEM)) {
//                    ContentValues values = new ContentValues();
//
//                    values.put(ArticleProvider.KEY_TITLE, feed.getTitle());
//                    values.put(ArticleProvider.KEY_DESCRIPTION, feed.getDescription());
//                    values.put(ArticleProvider.KEY_PUB_DATE, Utils.dateToString(feed.getDate()));
//                    values.put(ArticleProvider.KEY_CONTENT, feed.getEncodedContent());
//                    values.put(ArticleProvider.KEY_IMAGE_URL, feed.getImageUrl());
//
//                    mContext.getContentResolver().insert(
//                            ArticleProvider.CONTENT_URI, values);
//                }
//                eventType = xpp.next();
//            }
//            return true;
            in.close();

        }

        //Tu traite de la meme manière les catchs c'est utile ?
        catch (MalformedURLException e) {
            e.printStackTrace();
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