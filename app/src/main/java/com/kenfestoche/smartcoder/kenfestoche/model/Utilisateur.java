package com.kenfestoche.smartcoder.kenfestoche.model;

import com.orm.SugarRecord;

/**
 * Created by smartcoder on 12/07/16.
 */
public class Utilisateur extends SugarRecord{

    public String login;
    public String password;
    public String email;
    public String phone;
    public Double latitude;
    public Double longitude;
    public int sexe;
    public int tendancesexe;
    public int age;
    public int calme;
    public int affinity;
    public String description;
    public String id_facebook;
    public int id_user;
    public int statut;
    public boolean connecte;
    public String errormess;
    public String tokenFirebase;
    public int localiser;

    public Utilisateur(){
    }



}
