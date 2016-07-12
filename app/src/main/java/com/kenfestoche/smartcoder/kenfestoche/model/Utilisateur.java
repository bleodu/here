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
    public int sexe;
    public int tendancesexe;
    public int age;


}
