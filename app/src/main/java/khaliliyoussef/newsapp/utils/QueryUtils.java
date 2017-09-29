package khaliliyoussef.newsapp.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import khaliliyoussef.newsapp.R;
import khaliliyoussef.newsapp.model.News;


public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    // Keys for parsing JSON
    private static final String Response = "response";
    private static final String Results = "results";
    private static final String Section = "sectionName";
    private static final String Title = "webTitle";
    private static final String Date = "webPublicationDate";
    private static final String Url = "webUrl";


    private QueryUtils() {
    }

    // Query the News and return a list of {@link News} objects.
    public static List<News> fetchNews(String request) {
        // Create URL object
        URL url = createUrl(request);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, String.valueOf(R.string.httpProblem), e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link News}
        // Return the list of {@link News}
        return extractFromJson(jsonResponse);
    }

    // Returns new URL object from the given string URL.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, String.valueOf(R.string.urlProblem), e);
        }
        return url;
    }

    // Make an HTTP request to the given URL and return a String as the response
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // if the response was successful (code 200),
            // then read the input stream and parse the response
            // HttpURLConnection.HTTP_OK = 200
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, String.valueOf(R.string.readProblem) + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, String.valueOf(R.string.jsonProblem), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // Get the binary information in form of InputStreamReader,
    // convert it with BufferedReader and put it toString() with StringBuilder
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    // Get the data from JSON and return as a list of {@link News} objects
    private static List<News> extractFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> mNews = new ArrayList<>();
        try {
            // Build a list of News objects
            JSONObject baseJSONObject = new JSONObject(newsJSON);
            JSONObject responseJSONObject = baseJSONObject.getJSONObject(Response);
            JSONArray newsResults = responseJSONObject.getJSONArray(Results);
            for (int i = 0; i < newsResults.length(); i++) {
                JSONObject info = newsResults.getJSONObject(i);
                String section = info.getString(Section);
                String date = info.getString(Date);
                String title = info.getString(Title);
                String url = info.getString(Url);

                News news = new News(section, title, date, url);
                mNews.add(news);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, String.valueOf(R.string.jsonProblem2), e);
        }
        // Return the list of news
        return mNews;
    }
}