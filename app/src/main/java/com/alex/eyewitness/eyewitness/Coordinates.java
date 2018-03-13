package com.alex.eyewitness.eyewitness;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 10.03.2018.
 */

public class Coordinates {
    private Double lng;
    private Double lat;
    private Double accuracy;
    private String type;
    private Date inserted;

    public Coordinates(Double lng, Double lat, Double accuracy, String type, Date inserted) {
        this.lng = lng;
        this.lat = lat;
        this.accuracy = accuracy;
        this.type = type;
        this.inserted = inserted;
    }
    public Coordinates() {

    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setInserted(Date inserted) {
        this.inserted = inserted;
    }

    public Double getLng() {
        return lng;
    }

    public Double getLat() {
        return lat;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public String getType() {
        return type;
    }

    public Date getInserted() {
        return inserted;
    }
}
