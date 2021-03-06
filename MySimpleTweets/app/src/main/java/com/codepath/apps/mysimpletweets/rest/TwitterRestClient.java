package com.codepath.apps.mysimpletweets.rest;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterRestClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "nXIBgRhUUgUC2ofZ7buBerJcj";       // Change this
	public static final String REST_CONSUMER_SECRET = "pJEpmqh6idqsmPTL3zF0ookGkGCZZ3XFlWF2d1bR2fISGybue1"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public TwitterRestClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler handler, Long oldestLoadedTweetId) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);
		if (oldestLoadedTweetId != null) {
//			Toast.makeText(context, "oldestLoadedTweetId = " + oldestLoadedTweetId, Toast.LENGTH_SHORT).show();
			params.put("max_id", oldestLoadedTweetId);
		}
		client.get(apiUrl, params, handler);
	}

	public void createTweet(AsyncHttpResponseHandler handler, String tweetBody) {
		String apiUrl = getApiUrl("statuses/update.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("status", tweetBody);
		client.post(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

	public void getCurrentUser(AsyncHttpResponseHandler handler) {
		// https://dev.twitter.com/rest/reference/get/account/verify_credentials
		// test with https://dev.twitter.com/rest/tools/console
		String apiUrl = getApiUrl("account/verify_credentials.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		client.get(apiUrl, params, handler);
	}
}