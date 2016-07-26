package com.kenfestoche.smartcoder.kenfestoche.webservices;


import android.os.StrictMode;
import android.util.Log;

import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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



    public void UploadImage(String pathImg){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        String pathToOurFile = pathImg;
        String urlServer = "http://www.smartcoder-dev.fr/ZENAPP/webservice/WS_UploadFile.php";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;

        try
        {
            FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs &amp; Outputs.
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Set HTTP method to POST.
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            //outputStream = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(twoHyphens + boundary + lineEnd);
            wr.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
            wr.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                wr.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            wr.writeBytes(lineEnd);
            wr.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

            fileInputStream.close();
            wr.flush();
            wr.close();
        }
        catch (Exception ex)
        {
            //Exception handling
            //Log.i("erreur upload",ex.getMessage());

        }
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

                String urlParameters = "login="+User.login+"&phone="+User.phone+"&=&password="+User.password+"&age="+User.age+"&sexe="+User.sexe+"&tendancesexe="+User.tendancesexe+"&latitude="+User.latitude+"&longitude="+User.longitude;

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
