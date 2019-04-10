package com.alex.eyewitness.eyewitness;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ServerWorker {


    private static final String BASE_URL = "http://192.168.1.6:8080/eyeserver-war/webresources/generic";

    private static AsyncHttpClient client = new AsyncHttpClient();

    static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static void post(final Context pContext, final String url, final HttpEntity entity, final AsyncHttpResponseHandler responseHandler) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                client.post(pContext, getAbsoluteUrl(url), entity, "application/json", responseHandler);
            }
        };
        mainHandler.post(myRunnable);

    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


    public static void SaveUser(String pNotifId, Context pContext) {
        RequestParams rp = new RequestParams();
        rp.add("pNotifId", pNotifId);

        JsonStreamerEntity entity;
        try {
            // entity = new StringEntity("{pNotifId:"+pNotifId+"}", "UTF-8");
            entity = new JsonStreamerEntity(
                    null,
                    false,
                    "");

            // Add string params

            entity.addPart("mode", "saveID");
            entity.addPart("pNotifId", pNotifId);

        } catch (IllegalArgumentException e) {
            Log.d("HTTP", "StringEntity: IllegalArgumentException");
            return;
        }
        String contentType = "string/xml;UTF-8";
        ServerWorker.post(pContext, "", entity, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                String v = new String();
            }


            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                JSONObject firstEvent = null;
                try {
                    firstEvent = (JSONObject) timeline.get(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String tweetText = null;
                try {
                    tweetText = firstEvent.getString("text");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Do something with the response
                System.out.println(tweetText);
            }
        });
    }

    public static void SaveMiddleCoords(Coordinates pCoord, Context pContext) {
        //RequestParams rp = new RequestParams();
        //rp.add("lng", Double.toString( pCoord.getLng()));

        JsonStreamerEntity entity;
        try {
            entity = new JsonStreamerEntity(
                    null,
                    false,
                    "");
            entity.addPart("mode", "saveMiddleCoords");
            entity.addPart("pNotifId", FirebaseInstanceId.getInstance().getToken());
            entity.addPart("lng", Double.toString(pCoord.getLng()));
            entity.addPart("lnat", Double.toString(pCoord.getLat()));

        } catch (IllegalArgumentException e) {
            Log.d("HTTP", "StringEntity: IllegalArgumentException");
            return;
        }
        String contentType = "string/xml;UTF-8";
        ServerWorker.post(pContext, "", entity, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                String v = new String();
            }


            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                JSONObject firstEvent = null;
                try {
                    firstEvent = (JSONObject) timeline.get(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String tweetText = null;
                try {
                    tweetText = firstEvent.getString("text");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Do something with the response
                System.out.println(tweetText);
            }
        });

    }


}
