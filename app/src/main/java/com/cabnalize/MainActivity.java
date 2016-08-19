package com.cabnalize;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.neno0o.ubersdk.Endpoints.Models.Prices.Price;
import com.neno0o.ubersdk.Endpoints.Models.Prices.Prices;
import com.neno0o.ubersdk.Uber;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, KeyIds {

    private static String TAG = "MainActivity";
    private static final int ORIGIN_REQUEST_CODE = 1;
    private static final int DISTINATION_REQUEST_CODE = 2;
    private Button oriBtn, desBtn;
    private LatLng originLatLng, destinationLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uber.getInstance().init(CLIENT_ID,
                CLIENT_SECRET,
                SERVER_TOKEN,
                REDIRECT_URI);

        // Open the autocomplete activity when the button is clicked.
        oriBtn = (Button) findViewById(R.id.oriBtn);
        desBtn = (Button) findViewById(R.id.desBtn);
        oriBtn.setOnClickListener(this);
        desBtn.setOnClickListener(this);
        findViewById(R.id.searchBtn).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchBtn : {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("originLat", originLatLng.latitude);
                intent.putExtra("originLng", originLatLng.longitude);
                intent.putExtra("destinationLat", destinationLatLng.latitude);
                intent.putExtra("destinationLng", destinationLatLng.longitude);
                startActivity(intent);
                break;
            }
            case R.id.oriBtn : {
                openAutocompleteActivity(ORIGIN_REQUEST_CODE);
                break;
            }
            case R.id.desBtn : {
                openAutocompleteActivity(DISTINATION_REQUEST_CODE);
                break;
            }
        }
    }

    private void getUberData() {
        Uber.getInstance().getUberAPIService().getPriceEstimates(originLatLng.latitude,
                originLatLng.longitude,
                destinationLatLng.latitude,
                destinationLatLng.longitude,
                new Callback<Prices>() {
                    @Override
                    public void success(Prices prices, Response response) {
                        List<Price> tempList = prices.getPrices();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        System.out.println("error: "+error.getMessage());
                    }
                });
    }
    private void getOlaData() {
        ArrayMap<String, String> paramMap = new ArrayMap<>();
        paramMap.put("pickup_lat", ""+originLatLng.latitude);
        paramMap.put("pickup_lng", ""+originLatLng.longitude);
        paramMap.put("drop_lat", ""+destinationLatLng.latitude);
        paramMap.put("drop_lng", ""+destinationLatLng.longitude);
        ServerCommunication.INSTANCE.getServerData(MainActivity.this, Request.Method.GET, OLA_URL, "ola", paramMap, new ServerCommunication.OkuTaskListener() {
            @Override
            public void onPostExecute(String result) {
                System.out.println("ola result: "+result);
            }

            @Override
            public void onError() {

            }

            @Override
            public void noNetwork() {

            }
        });
    }

    private void openAutocompleteActivity(int requestCode) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, requestCode);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(), 0).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check that the result was from the autocomplete widget.
        if (requestCode == ORIGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                originLatLng = place.getLatLng();
                Log.i(TAG, "LatLng: "+place.getLatLng());
                Log.i(TAG, "Place Selected: " + place.getName());
                oriBtn.setText(place.getName());
                /*// Format the place's details and display them in the TextView.
                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
                } else {
                    mPlaceAttribution.setText("");
                }*/
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if the user pressed the back button.
            }
        } else if (requestCode == DISTINATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                destinationLatLng = place.getLatLng();
                Log.i(TAG, "LatLng: "+place.getLatLng());
                Log.i(TAG, "Place Selected: " + place.getName());
                desBtn.setText(place.getName());
                /*// Format the place's details and display them in the TextView.
                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
                } else {
                    mPlaceAttribution.setText("");
                }*/
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if the user pressed the back button.
            }
        }
    }

}