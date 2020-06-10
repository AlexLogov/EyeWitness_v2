package com.alex.eyewitness.eyewitness.messages;

import java.io.Serializable;
import java.util.Date;

public class NotificationInfo implements Serializable {
    public String message;

    public Double latitude;
    public Double longetude;
    public double presition;
    public Date beginDateOfAction;
    public Date endDateOfAction;
    public String description;
    public String userID;
    public Double minDistanle;

    public NotificationInfo() {
    }

    public NotificationInfo(Double latitude,
                            Double longetude,
                            double presition,
                            Date beginDateOfAction,
                            Date endDateOfAction,
                            String description,
                            String userID,
                            double minDistanle) {
        this.latitude = latitude;
        this.longetude = longetude;
        this.presition = presition;
        this.beginDateOfAction = beginDateOfAction;
        this.endDateOfAction = endDateOfAction;
        this.description = description;
        this.userID = userID;
        this.minDistanle = minDistanle;
    }

}
