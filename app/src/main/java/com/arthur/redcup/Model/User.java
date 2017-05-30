package com.arthur.redcup.Model;

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

        public void setEmail(String email){
            this.email = email;
        }

        public String getId(){
            return idUser;
        }

        public void setId(String idUser){
            this.idUser = idUser;
        }
}