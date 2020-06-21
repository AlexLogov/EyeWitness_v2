package com.alex.eyewitness.eyewitness.utils;

import android.content.Context;

import java.util.Date;

class PropertyUtils {

    private static PropertyUtils sPropertyUtils;

    private Date lastCoordSaveTime;

    private PropertyUtils(Context context) {
    }

    public static PropertyUtils getInstance(Context context) {
        if (sPropertyUtils == null) {
            sPropertyUtils = new PropertyUtils(context);
        }
        return sPropertyUtils;
    }

    public Date getLastCoordSaveTime() {
        return lastCoordSaveTime;
    }

    public void setLastCoordSaveTime(Date lastCoordSaveTime) {
        this.lastCoordSaveTime = lastCoordSaveTime;
    }

}
