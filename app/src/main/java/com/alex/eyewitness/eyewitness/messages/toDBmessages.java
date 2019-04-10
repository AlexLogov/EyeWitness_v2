package com.alex.eyewitness.eyewitness.messages;

public class toDBmessages {


    String fMode;
    String fMiddleLat;
    String fMiddleLng;


    public void setfMode(String fMode) {
        this.fMode = fMode;
    }

    public void setfMiddleLat(String fMiddleLat) {
        this.fMiddleLat = fMiddleLat;
    }

    public void setfMiddleLng(String fMiddleLng) {
        this.fMiddleLng = fMiddleLng;
    }


    @Override
    public String toString() {
        return "{" +
                "fMode='" + fMode + '\'' +
                " fMiddleLat='" + fMiddleLat + '\'' +
                " fMiddleLng='" + fMiddleLng + '\'' +
                '}';
    }
}
