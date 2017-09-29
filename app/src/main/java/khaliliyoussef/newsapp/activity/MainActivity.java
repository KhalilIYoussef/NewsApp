package khaliliyoussef.newsapp.activity;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import khaliliyoussef.newsapp.R;
import khaliliyoussef.newsapp.adapter.NewsAdapter;
import khaliliyoussef.newsapp.async.NewsLoader;
import khaliliyoussef.newsapp.model.News;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private static final String REQUEST_URL = "http://content.guardianapis.com/search?page-size=20";
    private static final String API_KEY = "ec6829c0-7ea5-40c3-b50d-f607e95587a3";
    private static final int LOADER_ID = 1;
    private NewsAdapter newsAdapter;
    private TextView emptyTextView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    LoaderManager loaderManager;
    String newsNumber;
    String orderBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        emptyTextView = (TextView) findViewById(R.id.empty_view);
        progressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        final ConnectivityManager connectivityManager;
        NetworkInfo networkInfo;

        newsAdapter = new NewsAdapter(MainActivity.this, new ArrayList<News>(), new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(News book) {
                String url = book.getURL();
                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                if (newIntent.resolveActivity(getPackageManager()) != null) {
                    newIntent.setData(Uri.parse(url));
                    startActivity(newIntent);
                }
            }
        });

        recyclerView.setAdapter(newsAdapter);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        // If there is a network connection, then fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager = getLoaderManager();
            loaderManager.restartLoader(LOADER_ID, null, MainActivity.this);
        } else {
            progressBar.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(R.string.no_internet);
        }
    }

    // Create the Loader
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle args) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        newsNumber = sharedPreferences.getString(getString(R.string.news_number_kye),
                getString(R.string.default_news_number));
        orderBy = sharedPreferences.getString(getString(R.string
                .order_by_key), getString(R.string.default_order_by));

        Uri uri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = uri.buildUpon();

        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("page-size", newsNumber);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        // Clear the adapter of previous news data
        newsAdapter.clear();
        if (data != null && !data.isEmpty()) {
            newsAdapter.addAll(data);
            // Hide loading indicator because the data has been loaded
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}