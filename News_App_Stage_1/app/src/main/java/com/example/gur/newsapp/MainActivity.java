package com.example.gur.newsapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>, AdapterView.OnItemClickListener {

    private ListView listView;
    private NewsAdapter adapter;
    private ArrayList<News> mNewsItems;

    @Override
    protected void onResume() {
        fetchSavedPreferences();
        super.onResume();
    }


    private void fetchSavedPreferences() {
        String url;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String BASE_URL = "https://content.guardianapis.com/search?&show-tags=contributor&api-key=";
        String API_KEY = "dcd9ad5e-c852-4c47-bc8f-eab7f3411f07";
        if (preferences.getString(getString(R.string.key), getString(R.string.default_order_value)).equals(getString(R.string.default_order_value))) {
            url = BASE_URL + API_KEY;
        } else {
            String ORDER_BY_ATTR = "&order-by=oldest";
            url = BASE_URL + API_KEY + ORDER_BY_ATTR;
        }
        Log.v("switch_value", preferences.getString("category_list", ""));
        switch (preferences.getString("category_list", "")) {
            case "All":
                Utility.setmURL(url);
                break;
            case "Politics":
                Utility.setmURL(url + "&q=politics");
                break;
            case "Sports":
                Utility.setmURL(url + "&q=sports");
                break;
            case "Finance":
                Utility.setmURL(url + "&q=finance");
                break;
            case "Education":
                Utility.setmURL(url + "&q=education");
                break;
            case "Economics":
                Utility.setmURL(url + "&q=economics");
            default:
                Utility.setmURL(url);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchSavedPreferences();

        listView = findViewById(R.id.list_view);

        mNewsItems = new ArrayList<>();

        if (!isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.network_error_message)
                    .setTitle(R.string.network_error_title);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
            listView.setOnItemClickListener(this);

        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        Log.v("URL Received", Utility.getmURL());

        Uri baseUri = Uri.parse(Utility.getmURL());
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        mNewsItems = new ArrayList<>(data);
        if (mNewsItems.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.json_error)
                    .setTitle(R.string.json_error_title);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        UpdateView(mNewsItems);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }

    private void UpdateView(ArrayList<News> newsItems) {
        adapter = new NewsAdapter(getApplicationContext(), newsItems);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news = adapter.getItem(position);
        String url = news.getArticleUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.choose_topic) {
            Intent in = new Intent(this, ChooseCategory.class);
            startActivity(in);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
