package com.example.gur.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class NewsAdapter extends ArrayAdapter<News> {
    NewsAdapter(Context context, ArrayList<News> newsItems) {
        super(context, 0, newsItems);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        News newsItem = getItem(position);
        TextView sectionView = listItemView.findViewById(R.id.section);
        TextView titleView = listItemView.findViewById(R.id.title);
        TextView dateView = listItemView.findViewById(R.id.date);
        TextView authorView = listItemView.findViewById(R.id.author);

        sectionView.setText(newsItem != null ? newsItem.getArticleSection() : null);
        titleView.setText(newsItem != null ? newsItem.getArticleTitle() : null);
        dateView.setText(newsItem != null ? newsItem.getArticlePublicationDate() : null);
        authorView.setText(newsItem != null ? newsItem.getArticleAuthor() : null);

        return listItemView;
    }
}
