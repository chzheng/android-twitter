package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by chzheng on 12/10/2015.
 *
 * - deserialize JSON
 * - manage display related logic
 * - store into DB
 * - pass btwn Activity via Parcelable: https://guides.codepath.com/android/Using-Parcelable
 */
public class Tweet implements /* Parcelable */ Serializable {

    private static final long serialVersionUID = 5177222050535318633L;  // TODO change to use Parcelable

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

//    // This is where you write the values you want to save to the `Parcel`.
//    // The `Parcel` class has methods defined to help you save all of your values.
//    // Note that there are only methods defined for simple values, lists, and other Parcelable objects.
//    // You may need to make several classes Parcelable to send the data you want.
//    @Override
//    public void writeToParcel(Parcel out, int flags) {
//        out.writeInt(id);
//        out.writeString(mName);
//        out.writeParcelable(mInfo, flags);
//    }
//
//    private long id; // tweet id
//    private String body; // tweet body
//    private User user;
//    public String createdAt;
//
//    // Using the `in` variable, we can retrieve the values that
//    // we originally wrote into the `Parcel`.  This constructor is usually
//    // private so that only the `CREATOR` field can access.
//    private MyParcelable(Parcel in) {
//        mData = in.readInt();
//        mName = in.readString();
//        mInfo = in.readParcelable(MySubParcelable.class.getClassLoader());
//    }

}
