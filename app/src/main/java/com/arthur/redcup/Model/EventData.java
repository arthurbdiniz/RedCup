package com.arthur.redcup.Model;


public class EventData {

    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String second;

    public EventData(){

    }

    public EventData(String year, String month, String day, String hour, String minute, String second){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;

    }

    public void setDate(String year, String month, String day){

        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setTime(String hour, String minute, String second){

        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

}
