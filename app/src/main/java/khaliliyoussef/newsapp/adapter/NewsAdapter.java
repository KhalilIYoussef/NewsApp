package khaliliyoussef.newsapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import khaliliyoussef.newsapp.R;
import khaliliyoussef.newsapp.activity.MainActivity;
import khaliliyoussef.newsapp.model.News;

import static khaliliyoussef.newsapp.R.id.mDate;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();
    private static OnItemClickListener mListener;
    private ArrayList<News> mNews;
    private MainActivity mContext;

    public NewsAdapter(MainActivity mContext, ArrayList<News> mNews, OnItemClickListener mListener) {
        this.mContext = mContext;
        this.mNews = mNews;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder viewHolder, int position) {

        News news = mNews.get(position);

        viewHolder.title.setText(news.getTitle());
        viewHolder.section.setText(news.getSection());
        viewHolder.date.setText(makeDateFormat(news.getDate()));
        viewHolder.onListClick(mNews.get(position), mListener);
    }

    // make new date format
    private String makeDateFormat(String date) {
        date = date.substring(0, date.length() - 1);
        String defaultDate = "yyyy-MM-dd'T'HH:mm:ss";
        String newDate = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(defaultDate);
        SimpleDateFormat outputFormat = new SimpleDateFormat(newDate);
        Date mDate;
        String output = "";
        try {
            mDate = inputFormat.parse(date);
            output = outputFormat.format(mDate);
        } catch (ParseException e) {
            Log.e(LOG_TAG, String.valueOf(R.string.dateParsing), e);
        }
        return output;
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public void clear() {
        mNews.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<News> book) {
        mNews.addAll(book);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(News book);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView section;
        TextView date;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.mTitle);
            section = (TextView) itemView.findViewById(R.id.mSection);
            date = (TextView) itemView.findViewById(mDate);
        }

        void onListClick(final News book, final OnItemClickListener mListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(book);
                }
            });
        }
    }
}
