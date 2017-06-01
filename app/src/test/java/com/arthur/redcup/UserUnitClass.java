package com.arthur.redcup;

import com.arthur.redcup.Model.User;

import org.junit.Before;
import org.junit.Test;

import static java.lang.Double.valueOf;
import static org.junit.Assert.*;


public class UserUnitClass {

    @Test
    public void shouldValidateEmail(){
        User user = new User();
        user.setEmail("teste@email.com");
        assertEquals("teste@email.com", user.getEmail());
    }

    @Test
    public void shouldValidateUserId(){
        User user = new User();
        user.setId("userId");
        assertEquals("userId", user.getId());
    }
}
