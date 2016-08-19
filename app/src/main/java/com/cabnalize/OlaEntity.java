package com.cabnalize;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sumanta.Longjam on 19-08-2016.
 */
public class OlaEntity {
    @SerializedName("category")
    private String displayName;
    @SerializedName("distance")
    private Double distance;
    @SerializedName("travel_time_in_minutes")
    private int duration;
    @SerializedName("amount_min")
    private int lowEstimate;
    @SerializedName("amount_max")
    private int highEstimate;

    public OlaEntity() {}

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getLowEstimate() {
        return lowEstimate;
    }

    public void setLowEstimate(int lowEstimate) {
        this.lowEstimate = lowEstimate;
    }

    public int getHighEstimate() {
        return highEstimate;
    }

    public void setHighEstimate(int highEstimate) {
        this.highEstimate = highEstimate;
    }
}
