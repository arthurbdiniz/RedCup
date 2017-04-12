package com.example.arthur.redcup;


public class User {

    public String name;
    public String email;
    public String password;
    public String idUser;

        public User() {

        }

        public User( String idUser, String name, String email, String password) {
            this.idUser = idUser;
            this.name = name;
            this.email = email;
            this.password = password;
        }


}
