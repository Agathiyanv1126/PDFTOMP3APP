package com.example.app;

public class ScheduleModelClass {

    public String getTextview1() {
        return textview1;
    }

    public String getTextview2() {
        return textview2;
    }

    public void setTextview(String textview1, String textview2) {
        this.textview1 = textview1;
        this.textview2 = textview2;
    }



    private String textview1;
    private String textview2;

    ScheduleModelClass(String textview1,String textview2){
        this.textview1=textview1;
        this.textview2=textview2;

    }


}
