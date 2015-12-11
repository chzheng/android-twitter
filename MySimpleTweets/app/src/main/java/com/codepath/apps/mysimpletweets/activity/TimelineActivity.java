package com.codepath.apps.mysimpletweets.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapter.TweetArrayAdapter;
import com.codepath.apps.mysimpletweets.listener.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.rest.TwitterRestApplication;
import com.codepath.apps.mysimpletweets.rest.TwitterRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {

    private TwitterRestClient client;
    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter tweetAdapter;
    private ListView lvTweets;
    private long oldestLoadedTweetId; // record last tweet received for infinit scroll

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tweets = new ArrayList<Tweet>();
        // create adaptor linking it to data source
        tweetAdapter = new TweetArrayAdapter(this, tweets);
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        lvTweets.setAdapter(tweetAdapter);

        client = TwitterRestApplication.getRestClient();
        populateTimeline(null); // initial load does not have a last loaded tweetId.

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        populateTimeline(oldestLoadedTweetId);
    }

    private void populateTimeline(final Long oldestLoadedTweetId) {
        // send API request to get timeline json
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                try {
                    // fill the listview
                    for (int i=0; i< response.length(); i++) {
                        JSONObject tweetJSON = response.getJSONObject(i);
                        Tweet tweet = Tweet.fromJSON(tweetJSON);
                        if (tweet.getId() != TimelineActivity.this.oldestLoadedTweetId) { // skip the record with the old max_id
                            tweets.add(tweet);
                            TimelineActivity.this.oldestLoadedTweetId = tweet.getId(); // record last tweet received for infinit scroll
                        }
                    }
                    tweetAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                throwable.printStackTrace();
            }
        }, oldestLoadedTweetId);
    }

}
