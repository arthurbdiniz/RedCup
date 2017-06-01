package com.arthur.redcup.Model;

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
    public String category;
    public String uf;
    public String location;
    public String neighborhood;
    public String pathImage;

    // Default constructor required for calls to
    // DataSnapshot.getValue(Ticket.class)
    public Ticket() {
    }

    public Ticket(String title, String description, String price, String CEP,
                  String userId, String userEmail, String userTelephone, String dateCreation,
                  String dateExpiration, String uf, String location, String neighborhood, String category, String pathImage) {

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
        this.category = category;
        this.pathImage = pathImage;
    }

    @Override
    public String toString() {
        return "\n"+"Title: " + title +"\n" + "Price: " + price +"\n" + "Email: " + userEmail+"\n";
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getPrice(){
        return price;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getCEP(){
        return CEP;
    }

    public void setPrice(String price){
        this.price = price;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    public String getUserTelephone(){
        return userTelephone;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDateCreation(){
        return dateCreation;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getDateExpiration(){
        return dateExpiration;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getCategory(){
        return category;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getUf() {
        return uf;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setNeighborhood(String neighborhood){
        this.neighborhood = neighborhood;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setPathImage(String pathImage){
        this.pathImage = pathImage;
    }

    public String getPathImage(){
        return pathImage;
    }
}