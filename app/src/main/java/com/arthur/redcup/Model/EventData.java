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

    public void setYear(String year){
        this.year = year;
    }

    public String getYear(){
        return year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonth() {
        return month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getHour() {
        return hour;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getMinute() {
        return minute;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getSecond() {
        return second;
    }
}