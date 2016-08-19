package com.cabnalize;

import android.support.v4.util.ArrayMap;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Sumanta.Longjam on 18-08-2016.
 */
public class Cabrequest extends StringRequest {

    private ArrayMap<String, String> param;
    private String url;
    public Cabrequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.url = url;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-APP-TOKEN", "30800530b7844cbf93c6f33bb7557c9b");
//        headers.put("Content-Type", "application/json; charset=utf-8");
//        headers.put("Content-Type", "application/json");
        return headers;
    }

    @Override
    public String getUrl() {
        if(getMethod() == Method.GET) {
            StringBuilder stringBuilder = new StringBuilder(url);
            Iterator<Map.Entry<String, String>> iterator = param.entrySet().iterator();
            int i = 1;
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if(i == 1) {
                    stringBuilder.append("?" + entry.getKey() + "=" + entry.getValue());
                } else {
                    stringBuilder.append("&" + entry.getKey() + "=" + entry.getValue());
                }
                iterator.remove(); // avoids a ConcurrentModificationException
                i++;
            }
            url = stringBuilder.toString();
        }
        return url;
    }
    @Override
    protected ArrayMap<String, String> getParams() throws AuthFailureError {
        return param;
    }
    public void setParamMap(ArrayMap<String, String> paramMap) {
        this.param = paramMap;
    }
}