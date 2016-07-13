package com.kenfestoche.smartcoder.kenfestoche.webservices;


import android.os.StrictMode;

import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by smartcoder on 07/07/16.
 */
public class WebService {

    public static final String URL_ZENAPP = "http://www.smartcoder-dev.fr/ZENAPP/webservice/";

    public JSONArray Connect(String Login, String Pass) {
        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
    try{
        URL url = new URL(URL_ZENAPP+"WS_Login.php");
        HttpURLConnection client = null;
        try {
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            //client.setRequestProperty("login",Login);
            //client.setRequestProperty("password",Pass);
            //client.setDoOutput(true);
            //client.connect();

            String urlParameters = "login="+Login+"&cn=&password="+Pass;

            // Send post request
            client.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(client.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int status = client.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    //StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        builder.append(line+"\n");
                    }
                    //builder.close();

            }
            //OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
            Messages = new JSONArray(builder.toString());

        } catch (IOException e){

        } catch (JSONException e) {
            e.printStackTrace();
        }


    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

        return Messages;
    }

    public JSONArray CreateUser(Utilisateur User) {
        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_CreateUser.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");

                String urlParameters = "login="+User.login+"&phone="+User.phone+"&=&password="+User.password+"&age="+User.age+"&sexe="+User.sexe+"&tendancesexe="+User.tendancesexe;

                // Send post request
                client.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(client.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();
                int status = client.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        //StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            builder.append(line+"\n");
                        }
                        //builder.close();

                }
                //OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
                Messages = new JSONArray(builder.toString());

            } catch (IOException e){

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Messages;
    }

    public JSONArray GetSmsCode(Utilisateur User,Boolean newSms,Boolean smsPass) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetSmsCode.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "phone="+User.phone;
                if(newSms){
                    urlParameters = urlParameters+"&newSMS=OK";
                }

                if(smsPass){
                    urlParameters = urlParameters+"&sendPass=OK";
                }


                // Send post request
                client.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(client.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();
                int status = client.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        //StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            builder.append(line+"\n");
                        }
                        //builder.close();

                }
                //OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
                Messages = new JSONArray(builder.toString());

            } catch (IOException e){

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Messages;
    }

}
