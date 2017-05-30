package com.arthur.redcup.ModelUnitTest;

import com.arthur.redcup.Model.Ticket;

import org.junit.Before;
import org.junit.Test;

import static java.lang.Double.valueOf;
import static org.junit.Assert.*;

public class TicketUnitTest {

    @Test
    public void shouldValidateTicketId(){
        Ticket ticket = new Ticket();
        ticket.setTicketId("TicketId");
        assertEquals("TicketId", ticket.getTicketId());
    }

    @Test
    public void shouldValidateUserId(){
        Ticket ticket = new Ticket();
        ticket.setUserId("Joao123@email.com");
        assertEquals("Joao123@email.com", ticket.getUserId());
    }

    @Test
    public void shouldValidateTitle(){
        Ticket ticket = new Ticket();
        ticket.setTitle("Title");
        assertEquals("Title", ticket.getTitle());
    }

    @Test
    public void shouldValidateDescreption(){
        Ticket ticket =  new Ticket();
        ticket.setDescription("Description");
        assertEquals("Description", ticket.getDescription());
    }

    @Test
    public void shouldValidatePrice(){
        Ticket ticket = new Ticket();
        ticket.setPrice("price");
        assertEquals("price", ticket.getPrice());
    }

    @Test
    public void shouldValidateCEP() {
        Ticket ticket = new Ticket();
        ticket.setCEP("CEP");
        assertEquals("CEP", ticket.getCEP());
    }

    @Test
    public void shouldValidateUserTelephone(){
        Ticket ticket = new Ticket();
        ticket.setUserTelephone("telephone");
        assertEquals("telephone", ticket.getUserTelephone());
    }

    @Test
    public void shouldValidateUserEmail(){
        Ticket ticket = new Ticket();
        ticket.setUserEmail("aluno@email.com");
        assertEquals("aluno@email.com", ticket.getUserEmail());
    }

    @Test
    public void shouldValidateDateCreation(){
        Ticket ticket = new Ticket();
        ticket.setDateCreation("dateCreation");
        assertEquals("dateCreation", ticket.getDateCreation());
    }

    @Test
    public void shouldValidateDateExpiration(){
        Ticket ticket = new Ticket();
        ticket.setDateExpiration("dateExpiration");
        assertEquals("dateExpiration", ticket.getDateExpiration());
    }

    @Test
    public void shouldValidateCategory(){
        Ticket ticket = new Ticket();
        ticket.setCategory("category");
        assertEquals("category", ticket.getCategory());
    }

    @Test
    public void shouldValidateUf(){
        Ticket ticket = new Ticket();
        ticket.setUf("uf");
        assertEquals("uf", ticket.getUf());
    }

    @Test
    public void shouldValidateLocation(){
        Ticket ticket = new Ticket();
        ticket.setLocation("location");
        assertEquals("location", ticket.getLocation());
    }

    @Test
    public void shouldValidateNeighborhood(){
        Ticket ticket = new Ticket();
        ticket.setNeighborhood("neighborhood");
        assertEquals("neighborhood", ticket.getNeighborhood());
    }

    @Test
    public void shouldValidatePathImage(){
        Ticket ticket = new Ticket();
        ticket.setPathImage("pathImage");
        assertEquals("pathImage", ticket.getPathImage());
    }

}
