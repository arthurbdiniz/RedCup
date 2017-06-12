package com.arthur.redcup.Model;

import java.io.Serializable;



public class Location implements Serializable {

    private final String uf;
    private final String state;

    public Location(String uf, String state) {

        this.uf = uf;
        this.state = state;

    }
    public String getUf() {
        return uf;
    }

    public String getState() {
        return  state;
    }

}
