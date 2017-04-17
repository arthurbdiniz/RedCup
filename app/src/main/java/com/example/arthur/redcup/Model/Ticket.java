package com.example.arthur.redcup.Model;


import java.io.Serializable;

public class Ticket implements Serializable {


    public String ticketId;
    public String userId;
    public String title;
    public String description;
    public String price;
    public String CEP;
    public String userEmail;

    // Default constructor required for calls to
    // DataSnapshot.getValue(Ticket.class)
    public Ticket() {
    }

    public Ticket(String title, String description, String price, String CEP, String userId, String userEmail) {

        this.title = title;
        this.description = description;
        this.price = price;
        this.CEP = CEP;
        this.userId = userId;
        this.userEmail = userEmail;


    }


    @Override
    public String toString() {
        return "\n"+"Title: " + title + "\n\n"+"Description: " +
                description +"\n" + "Price: " + price +"\n" + "Email: " + userEmail;
    }

    public void setTicketId(String ticketId){
        this.ticketId = ticketId;
    }

    public String getTitle(){
        return title;
    }




}
