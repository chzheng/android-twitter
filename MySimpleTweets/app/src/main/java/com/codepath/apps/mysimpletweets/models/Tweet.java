package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chzheng on 12/10/2015.
 *
 * - deserialize JSON
 * - manage display related logic
 * - store into DB
 */
public class Tweet {
    /*
    User should be displayed the username, name, and body for each tweet
    User should be displayed the relative timestamp for each tweet "8m", "7h"
     */
    private long id; // tweet id
    private String body; // tweet body
    private User user;
    public String createdAt;

    public long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    // Tweet.fromJSON(JSONObject) => Tweet
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.id = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }
}
