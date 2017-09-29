package khaliliyoussef.newsapp.async;


import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import khaliliyoussef.newsapp.model.News;
import khaliliyoussef.newsapp.utils.QueryUtils;


public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // background thread
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // perform the network request and extract the news.
        return QueryUtils.fetchNews(mUrl);
    }
}