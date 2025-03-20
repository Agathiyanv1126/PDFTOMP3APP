package com.example.app.response;

import com.example.app.admin.schedule.ViewScheduleMC;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ViewScheduleResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("buses")
    private List<ViewScheduleMC> buses;

    public boolean isSuccess() {
        return success;
    }

    public List<ViewScheduleMC> getBuses() {
        return buses;
    }
}