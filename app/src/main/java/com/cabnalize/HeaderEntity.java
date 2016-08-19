package com.cabnalize;

/**
 * Created by Sumanta.Longjam on 19-08-2016.
 */
public class HeaderEntity {
    private boolean isUber;

    public HeaderEntity(boolean isUber) {
        this.isUber = isUber;
    }
    public boolean isUber() {
        return isUber;
    }

    public void setUber(boolean uber) {
        isUber = uber;
    }
}
