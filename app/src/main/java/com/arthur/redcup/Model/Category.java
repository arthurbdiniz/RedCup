package com.arthur.redcup.Model;

import android.support.annotation.StringDef;
import android.widget.ImageView;


public class Category {

    private final String nome;
    public final int photo;



    public Category(String nome, int photo) {

        this.nome = nome;
        this.photo = photo;

    }

    public String getNome() {
        return nome;
    }

    public int getPhoto() {return  photo;}

}

