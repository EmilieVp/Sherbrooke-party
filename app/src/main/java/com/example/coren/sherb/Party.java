package com.example.coren.sherb;

import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

public class Party extends AppCompatActivity implements Serializable {

    private String idHost;
    private String type;
    private String title;
    private String description;
    private int price;
    private int max_guest;
    private String schedule;
    private double latitude;
    private double longitude;

    public Party(String idHost, String type, String title, String description, int price, int max_guest, String schedule, double latitude, double longitude){
        this.idHost=idHost;
        this.type = type;
        this.title = title;
        this.description = description;
        this.price = price;
        this.max_guest = max_guest;
        this.schedule = schedule;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getIdHost() {
        return idHost;
    }

    public String getType(){
        return type;
    }

    public String getTitre() { return title; }

    public String getDescription(){
        return description;
    }

    public int getPrice() { return price; }

    public int getMax_guest(){
        return max_guest;
    }

    public String getSchedule() {return schedule;}

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

}
