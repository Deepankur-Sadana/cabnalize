package com.cabnalize;

import android.app.Application;
import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Sumanta.Longjam on 18-08-2016.
 */
public class Cabnalize extends Application {
    private static Context context;
    private RequestQueue requestQueue;
    private static final Cabnalize cabnalize = new Cabnalize();

    public Cabnalize() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Cabnalize getInstance() {
        return cabnalize;
    }

    public Context getContext() {
        return context;
    }

    public RequestQueue getRequestQueue() {
        synchronized (this) {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(context);
            }
        }
        return requestQueue;
    }
}
