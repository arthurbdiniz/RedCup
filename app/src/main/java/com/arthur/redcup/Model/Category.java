package com.arthur.redcup.Model;

import android.support.annotation.StringDef;
import android.widget.ImageView;

import java.io.Serializable;


public class Category implements Serializable {

    private String nome;
    public int photo;

    public Category(String nome, int photo) {

        this.nome = nome;
        this.photo = photo;

    }

    public Category() {

    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public int getPhoto() {
        return  photo;
    }
}