package com.example.progettoIUMcorretto;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class Parcheggi {
    private String parkingName;
    private int imageResource;
    private String security;
    private String gRatings;
    private int colorSecurity;
    private List<Parcheggi> parcheggiList = new ArrayList<Parcheggi>();

    public Parcheggi(String parkingName, int imageResource, String security, String gRatings, int colorSecurity) {
        this.parkingName = parkingName;
        this.imageResource = imageResource;
        this.security = security;
        this.gRatings = gRatings;
        this.colorSecurity = colorSecurity;
    }

    public Parcheggi(String parkingName, int imageResource, String security, String gRatings) {
        this.parkingName = parkingName;
        this.imageResource = imageResource;
        this.security = security;
        this.gRatings = gRatings;
        addToList();
    }

    public void addToList() {
        parcheggiList.add(this);
    }
    public ArrayList<Parcheggi> getList(){
        return (ArrayList<Parcheggi>) parcheggiList;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getgRatings() {
        return gRatings;
    }

    public void setgRatings(String gRatings) {
        this.gRatings = gRatings;
    }

    public int getColorSecurity() {
        return colorSecurity;
    }

    public void setColorSecurity() {
        float avg = Float.parseFloat(this.getSecurity());

        if (avg >3.8){
            colorSecurity = R.drawable.dot_green;
        }else if(avg >2.2 && avg <=3.8){
            colorSecurity= R.drawable.dot_orange;
        }else{
            colorSecurity = R.drawable.dot_red;
        }

    }
}
