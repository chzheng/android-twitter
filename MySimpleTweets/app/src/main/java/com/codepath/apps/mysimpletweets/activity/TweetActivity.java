package com.codepath.apps.mysimpletweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.rest.TwitterRestApplication;
import com.codepath.apps.mysimpletweets.rest.TwitterRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

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

        // TODO set current user's username & profile image
        populateCurrentUser();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void populateCurrentUser() {
        TwitterRestClient client = TwitterRestApplication.getRestClient();
        client.getCurrentUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                User user = User.fromJSON(response);
                if (user != null) {
                    TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
                    tvUsername.setText(user.getName());
                    ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
                    ivProfileImage.setImageDrawable(null);
                    // download image from internet into imageView
                    Picasso.with(TweetActivity.this.getBaseContext()).load(user.getProfileImageUrl()).into(ivProfileImage);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }

    public void onClickCancel(View v) {
        this.finish();
    }


    public void onTweetClicked(View v) {
        // extract tweet body from UI
        EditText etBody = (EditText) findViewById(R.id.etBody);
        String tweetBody = etBody.getText().toString();
//        Toast.makeText(this, tweetBody, Toast.LENGTH_SHORT).show();

        // call Twitter API to create tweet
        TwitterRestClient client = TwitterRestApplication.getRestClient();
        client.createTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                // extract newly created tweet
                Tweet tweet = Tweet.fromJSON(response);

                // pass it back to timeline activity to inject into timeline on client side
                // because twitter can not absorb it into timeline fast enough for client refresh
                Intent data = new Intent();
                data.putExtra("tweet", tweet);
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
