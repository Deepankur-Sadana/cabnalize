package com.cabnalize;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by sumanta on 24/2/16.
 */
public enum ServerCommunication {
    INSTANCE;
    public void getServerData(Context contex, int method, final String url, String tag, ArrayMap<String, String> param, final OkuTaskListener okuTaskListener) {
        Cabrequest cabrequest = new Cabrequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (!TextUtils.isEmpty(response)) {
                        okuTaskListener.onPostExecute(response);
                    } else {
                        okuTaskListener.onPostExecute("");
                    }
                } catch (Exception e) {
                    okuTaskListener.onPostExecute("");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                okuTaskListener.onError();
            }
        });
        if(param!=null)
            cabrequest.setParamMap(param);
        cabrequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (Utils.isNetworkAvailable(contex))
            addToRequestQueue(cabrequest, tag);
        else
            okuTaskListener.noNetwork();
    }

    private void addToRequestQueue(Cabrequest req, String tag) {
        if (!TextUtils.isEmpty(tag))
            req.setTag(tag);
        Cabnalize.getInstance().getRequestQueue().add(req);
    }

    public void cancelRequests(Object tag) {
        Cabnalize.getInstance().getRequestQueue().cancelAll(tag);
    }

    public interface OkuTaskListener {
        void onPostExecute(String result);
        void onError();
        void noNetwork();
    }
}
