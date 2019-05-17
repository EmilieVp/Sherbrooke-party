package com.example.coren.sherb;

/**
 * Created by oumai on 08/12/2018.
 */

public class User {
    public String idUser;
    public String pseudo;
    public String password;

    public User(String idUser, String pseudo, String password){
        this.idUser = idUser;
        this.pseudo = pseudo;
        this.password = password;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getPseudo(){
        return pseudo;
    }

    public String getPassword(){
        return password;
    }

}
