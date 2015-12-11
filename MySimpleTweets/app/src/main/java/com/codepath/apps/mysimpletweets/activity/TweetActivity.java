package com.codepath.apps.mysimpletweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.rest.TwitterRestApplication;
import com.codepath.apps.mysimpletweets.rest.TwitterRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class TweetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void onClickCancel(View v) {
        this.finish();
    }


    public void onTweetClicked(View v) {
        // extract tweet body from UI
        EditText etBody = (EditText) findViewById(R.id.etBody);
        String tweetBody = etBody.getText().toString();
        Toast.makeText(this, tweetBody, Toast.LENGTH_SHORT).show();

        // call Twitter API to create tweet
        TwitterRestClient client = TwitterRestApplication.getRestClient();
        client.createTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                // trigger timeline activity a refresh to show latest tweet
                Intent data = new Intent();
                // maybe we can pass back the id of just created tweet
                setResult(RESULT_OK, data);
                // close this Activity
                TweetActivity.this.finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                throwable.printStackTrace();
            }
        }, tweetBody);

    }
}
