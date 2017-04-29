package com.example.arthur.redcup.Model;


import java.io.Serializable;

public class Ticket implements Serializable {



    public String ticketId;
    public String userId;
    public String title;
    public String description;
    public String price;
    public String CEP;
    public String userTelephone;
    public String userEmail;
    public String dateCreation;
    public String dateExpiration;



    public String uf;
    public String location;
    public String neighborhood;

    // Default constructor required for calls to
    // DataSnapshot.getValue(Ticket.class)
    public Ticket() {
    }

    public Ticket(String title, String description, String price, String CEP,
                  String userId, String userEmail, String userTelephone, String dateCreation,
                  String dateExpiration, String uf, String location, String neighborhood) {

        this.title = title;
        this.description = description;
        this.price = price;
        this.CEP = CEP;
        this.userTelephone = userTelephone;
        this.dateCreation = dateCreation ;
        this.dateExpiration = dateExpiration;
        this.userId = userId;
        this.userEmail = userEmail;

        this.uf = uf;
        this.location = location;
        this.neighborhood = neighborhood;


    }


    @Override
    public String toString() {
        return "\n"+"Title: " + title +"\n" + "Price: " + price +"\n" + "Email: " + userEmail+"\n";
    }

    public void setTicketId(String ticketId){
        this.ticketId = ticketId;
    }

    public String getTitle(){
        return title;
    }
    public String getPrice(){
        return price;
    }

    public String getUf() {
        return uf;
    }

    public String getLocation() {
        return location;
    }

    public String getNeighborhood() {
        return neighborhood;
    }






}
