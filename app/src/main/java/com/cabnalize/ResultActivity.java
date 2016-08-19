package com.cabnalize;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.neno0o.ubersdk.Endpoints.Models.Prices.Price;
import com.neno0o.ubersdk.Endpoints.Models.Prices.Prices;
import com.neno0o.ubersdk.Uber;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Sumanta.Longjam on 18-08-2016.
 */
public class ResultActivity extends AppCompatActivity implements KeyIds {

    private RecyclerView recyclerView;
    private ResultAdapter adapter;
    private List<Object> entityList = new ArrayList<>();
    private double originLat, originLng, destinationLat, destinationLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        originLat = getIntent().getDoubleExtra("originLat", 0);
        originLng = getIntent().getDoubleExtra("originLng", 0);
        destinationLat = getIntent().getDoubleExtra("destinationLat", 0);
        destinationLng = getIntent().getDoubleExtra("destinationLng", 0);
        getOlaData();
        getUberData();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ResultAdapter(this, entityList);
        recyclerView.setAdapter(adapter);
    }

    private void getUberData() {
        Uber.getInstance().getUberAPIService().getPriceEstimates(originLat,
                originLng,
                destinationLat,
                destinationLng,
                new Callback<Prices>() {
                    @Override
                    public void success(Prices prices, Response response) {
                        List<Price> tempList = prices.getPrices();
                        entityList.add(new HeaderEntity(true));
                        entityList.addAll(tempList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        System.out.println("error: "+error.getMessage());
                    }
                });
    }
    private void getOlaData() {
        ArrayMap<String, String> paramMap = new ArrayMap<>();
        paramMap.put("pickup_lat", ""+originLat);
        paramMap.put("pickup_lng", ""+originLng);
        paramMap.put("drop_lat", ""+destinationLat);
        paramMap.put("drop_lng", ""+destinationLng);
        ServerCommunication.INSTANCE.getServerData(ResultActivity.this, Request.Method.GET, OLA_URL, "ola", paramMap, new ServerCommunication.OkuTaskListener() {
            @Override
            public void onPostExecute(String result) {
                System.out.println("ola result: "+result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    List<OlaEntity> tempList = new Gson().fromJson(jsonObject.getJSONArray("ride_estimate").toString(), new TypeToken<List<OlaEntity>>() {}.getType());
                    entityList.add(new HeaderEntity(false));
                    entityList.addAll(tempList);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {

            }

            @Override
            public void noNetwork() {

            }
        });
    }
}
