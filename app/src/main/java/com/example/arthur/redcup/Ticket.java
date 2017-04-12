package com.example.arthur.redcup;


public class Ticket {


    public String title;
    public String description;
    public String price;
    public String CEP;

    // Default constructor required for calls to
    // DataSnapshot.getValue(Ticket.class)
    public Ticket() {
    }

    public Ticket(String title, String description, String price, String CEP) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.CEP = CEP;

    }


    @Override
    public String toString() {
        return "Title: " + title + "\n"+" Description: " +
                description + " Price: " + price + "CEP:" + CEP;
    }




}
