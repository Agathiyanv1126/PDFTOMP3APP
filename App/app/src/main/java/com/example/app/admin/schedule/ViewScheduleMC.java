package com.example.app.admin.schedule;

import com.google.gson.annotations.SerializedName;

public class ViewScheduleMC {
    @SerializedName("_id")
    private String id;

    @SerializedName("bus_name")
    private String busName;

    @SerializedName("bus_number")
    private String busNumber;

    @SerializedName("date")
    private String date;

    public ViewScheduleMC(String id, String busName, String busNumber, String date) {
        this.id = id;
        this.busName = busName;
        this.busNumber = busNumber;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getBusName() {
        return busName;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public String getDate() {
        return date;
    }
}
