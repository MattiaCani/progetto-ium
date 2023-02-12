package com.example.parkify;

import java.io.Serializable;

public class Disp implements Serializable{
    private Float media;
    private Integer numVal;

    public Disp(){}

    public Disp(Float media, Integer numVal) {
        this.media = media;
        this.numVal = numVal;
    }

    public Float getMedia() {
        return media;
    }

    public void setMedia(Float media) {
        this.media = media;
    }

    public Integer getNumVal() {
        return numVal;
    }

    public void setNumVal(Integer numVal) {
        this.numVal = numVal;
    }
}
