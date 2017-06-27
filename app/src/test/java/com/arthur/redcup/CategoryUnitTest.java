package com.arthur.redcup;

import com.arthur.redcup.Model.Category;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CategoryUnitTest {

    @Test
    public void shouldValidateNome(){
        Category category = new Category();
        category.setNome("2000");
        assertEquals("2000", category.getNome());
    }

    @Test
    public void shouldValidatePhoto(){
        Category category = new Category();
        category.setPhoto(10);
        assertEquals(10, category.getPhoto());
    }
}
