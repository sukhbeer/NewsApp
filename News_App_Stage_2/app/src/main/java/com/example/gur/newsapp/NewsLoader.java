package com.example.gur.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String mUrl;

    NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<News> loadInBackground() {
        List<News> newsItems = new ArrayList<>();
        if (mUrl == null) {
            return null;
        }
        try {
            newsItems = Utility.fetchNewsItems(mUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsItems;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
