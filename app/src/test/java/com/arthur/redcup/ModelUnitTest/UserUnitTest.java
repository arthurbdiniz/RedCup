package com.arthur.redcup.ModelUnitTest;

import com.arthur.redcup.Model.User;

import org.junit.Before;
import org.junit.Test;

import static java.lang.Double.valueOf;
import static org.junit.Assert.*;

public class UserUnitTest {

    @Test
    public void shouldValidateUf(){
        User user = new User();
        user.setId("IdUser");
        assertEquals("IdUser", user.getId());
    }

    @Test
    public void shouldValidateEmail(){
        User user = new User();
        user.setEmail("email");
        assertEquals("email", user.getEmail());
    }
}
