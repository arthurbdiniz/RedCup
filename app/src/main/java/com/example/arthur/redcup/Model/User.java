package com.example.arthur.redcup.Model;


import java.io.Serializable;

public class User implements Serializable{


    public String email;
    public String idUser;

        public User() {

        }

        public User( String idUser, String email) {
            this.idUser = idUser;
            this.email = email;

        }

        public String getEmail(){
            return email;
        }
        public String getId(){
            return idUser;
        }


}
