package com.kenfestoche.smartcoder.kenfestoche.webservices;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;

import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by smartcoder on 07/07/16.
 */
public class WebService {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String URL_ZENAPP = "http://www.smartcoder-dev.fr/ZENAPP/webservice/";

    public Utilisateur Connect(String Login, String Pass) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        Utilisateur user=null;
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

            if(Messages.length()>0){
                Utilisateur.deleteAll(Utilisateur.class);

                JSONObject Uti=Messages.getJSONObject(0);


                user = new Utilisateur();
                user.login=Uti.getString("pseudo");
                user.password=Uti.getString("password");
                user.email=Uti.getString("email");
                user.sexe=Uti.getInt("sexe");
                user.tendancesexe=Uti.getInt("id_tendance");
                user.age=Uti.getInt("age");
                user.phone=Uti.getString("phone");
                user.description=Uti.getString("description");
                user.calme=Uti.getInt("calme");
                user.affinity=Uti.getInt("affinity");
                user.id_user=Uti.getInt("iduser");
                user.statut=Uti.getInt("statut");
                user.localiser=Uti.getInt("localiser");

                user.save();



            }


        } catch (IOException e){

        } catch (JSONException e) {
            e.printStackTrace();
        }


    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

        return user;
    }



    public void UploadImage(String pathImg,Utilisateur user){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        String pathToOurFile = pathImg;
        String urlServer = "http://www.smartcoder-dev.fr/ZENAPP/webservice/WS_UploadFile.php?id_user="+user.id_user;
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

    public Utilisateur SaveUser(Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        //Utilisateur user=null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_SaveUser.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");

                String urlParameters =  "localiser="+User.positiontous+"&localiseramis="+User.positionamis+"&tokenFirebase="+User.tokenFirebase+"&id="+User.id_user+"&calme="+User.calme+"&affinity="+User.affinity+"&qqmots="+User.description+"&statut="+User.statut+"&email="+User.email+"&idfacebook="+User.id_facebook+"&login="+User.login+"&phone="+User.phone+"&password="+User.password+"&age="+User.age+"&sexe="+User.sexe+"&tendancesexe="+User.tendancesexe+"&latitude="+User.latitude+"&longitude="+User.longitude;

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

                if(Messages.length()>0){
                    //Utilisateur.deleteAll(Utilisateur.class);

                    JSONObject Uti=Messages.getJSONObject(0);


                    //Utilisateur.deleteAll(Utilisateur.class);
                    //user= new Utilisateur();
                    if(Uti.has("pseudo")){
                        User.login=Uti.getString("pseudo");
                        User.password=Uti.getString("password");
                        User.email=Uti.getString("email");
                        User.sexe=Uti.getInt("sexe");
                        User.tendancesexe=Uti.getInt("id_tendance");
                        User.age=Uti.getInt("age");
                        User.phone=Uti.getString("phone");
                        User.description=Uti.getString("description");
                        User.calme=Uti.getInt("calme");
                        User.affinity=Uti.getInt("affinity");
                        User.id_user=Uti.getInt("iduser");
                        User.statut=Uti.getInt("statut");
                        User.id_facebook=Uti.getString("idfacebook");
                        User.errormess=Uti.getString("message");
                        User.localiser=Uti.getInt("localiser");
                        User.connecte=true;
                    }else{

                        User.errormess=new String(Uti.getString("message").getBytes("ISO-8859-1"), "UTF-8");
                        User.id_user=0;
                    }


                    //User.save();



                }

            } catch (IOException e){

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return User;
    }

    public void DeleteUser(Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        //Utilisateur user=null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_DeleteUser.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");

                String urlParameters =  "id="+User.id_user;

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

                User.delete();


            } catch (IOException e){

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public Utilisateur GetUserFacebook(String IdFacebook) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        Utilisateur user=null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetUserFacebook.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");

                String urlParameters =  "idfacebook="+IdFacebook;
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

                if(Messages.length()>0){
                    Utilisateur.deleteAll(Utilisateur.class);

                    JSONObject Uti=Messages.getJSONObject(0);


                    Utilisateur.deleteAll(Utilisateur.class);
                    user= new Utilisateur();
                    user.login=Uti.getString("pseudo");
                    user.password=Uti.getString("password");
                    user.email=Uti.getString("email");
                    user.sexe=Uti.getInt("sexe");
                    user.tendancesexe=Uti.getInt("id_tendance");
                    user.age=Uti.getInt("age");
                    user.phone=Uti.getString("phone");
                    user.description=Uti.getString("description");
                    user.calme=Uti.getInt("calme");
                    user.affinity=Uti.getInt("affinity");
                    user.id_user=Uti.getInt("iduser");
                    user.statut=Uti.getInt("statut");
                    user.connecte=true;
                    user.save();



                }

            } catch (IOException e){

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
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

    public JSONArray DeletePhotoProfil(int IdPhoto) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_DeletePhoto.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_photo="+IdPhoto;


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


    public JSONArray SetProfilUser(Utilisateur User) {
        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_SetProfilUser.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");

                String urlParameters = "login="+User.login+"&phone="+User.phone+"&=&password="+User.password+"calme="+User.calme+"&affinity="+User.affinity+"&=&qqmots="+User.description+"&age="+User.age+"&sexe="+User.sexe+"&tendancesexe="+User.tendancesexe+"&latitude="+User.latitude+"&longitude="+User.longitude;

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

    public JSONArray GetListPhotos(Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetListPhotos.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+User.id_user;


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

    public JSONArray GetinfoKiff(String idUser,int idmoi) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetInfoUser.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+idUser+"&id_moi="+idmoi;


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

    public JSONArray GetinfoUser(String idUser) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetInfoUser.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+idUser;


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

    public JSONArray GetMessageNonLu(int idUser, String idkiffs) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetMessageNonLu.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+idUser+"&id_kiffs="+idkiffs;


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

    public JSONArray KiffUser(String idUser,String id_kiff,String statut) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_SetKiffUser.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+idUser+"&id_kiff="+id_kiff+"&statut="+statut;


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

    public JSONArray GetLastPositionUserInconnu(Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetPositionUserInconnu.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+User.id_user;


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

    public JSONArray GetLastPositionUserMatch(Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetPositionUserMatch.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+User.id_user;


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

    public JSONArray GetLastPositionUserAmis(Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetPositionUserAmis.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+User.id_user;


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

    public JSONArray GetListKiffs(Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetListKiffs.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+User.id_user;


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

    public JSONArray SetLastPosition(Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_SetLastPosition.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+User.id_user+"&latitude="+User.latitude+"&longitude="+User.longitude;


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

    public JSONArray GetListUserByPhone(String Phone,Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetListUsers.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "phone="+Phone+"&id_user="+User.id_user;


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

    public JSONArray GetListUserByPhones(Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetListUsers.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+User.id_user;

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

    public JSONArray GetDetailConversation(String  idconv,Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetDetailConversation.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_conversation="+idconv+"&id_user="+User.id_user;


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

    public JSONArray GetUsersConversation(String  idconv) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetUsersConversation.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_conversation="+idconv;


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

    public JSONArray GetStatutConversation(String  idconv,int id_user) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetStatutConversation.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_conversation="+idconv+"&id_user="+id_user;


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

    public JSONArray GetNewDetailConversation(String  idconv,Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetNewDetailConversation.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_conversation="+idconv+"&id_user="+User.id_user;


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

    public JSONArray SetStatutConversation(String  idconv,int Statut) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_SetStatutConversation.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_conversation="+idconv+"&statut="+Statut;


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

    public JSONArray GetListProfils(Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetListProfils.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+User.id_user;


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

    public JSONArray GetListPhotosProfil(String id_user) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetListPhotosUser.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+id_user;


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

    public JSONArray GetListAmis(Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetListAmis.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+User.id_user;


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

    public JSONArray GetProfilPhoto(Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetProfilPhotoUser.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+User.id_user;


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

    public JSONArray GetListConversations(Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_GetListConversation.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+User.id_user;


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

    public JSONArray AddChatMessage(Utilisateur User,String Message,String idConv) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_AddChatMessage.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+User.id_user+"&message="+Message+"&id_conv="+idConv;


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

    public JSONArray AddConversation(String NomConversation, String idusers,Utilisateur User) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_AddConversation.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "nom_conversation="+NomConversation+"&id_users="+idusers+"&id_user="+User.id_user;



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

    public JSONArray AddConversationPrive(String iduser,String id_kiffs) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_AddConversationPrive.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_kiffs="+id_kiffs+"&id_user="+iduser;



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

    public JSONArray SetNewFriend(Utilisateur User,String id_ami) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_AjoutAmis.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                String urlParameters = "id_user="+User.id_user+"&id_ami="+id_ami;


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

    public JSONArray DeleteKiffs(Utilisateur User,String id_kiff) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_DeleteKiffs.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");

                String urlParameters = "id_user="+User.id_user+"&id_kiff="+id_kiff;


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

    public JSONArray SignalerKiffs(Utilisateur User,String id_kiff) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_SignalerKiffs.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");

                String urlParameters = "id_user="+User.id_user+"&id_kiff="+id_kiff;


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

    public JSONArray AcceptRefuseFriend(Utilisateur User,String id_ami,String statut) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StringBuilder builder = new StringBuilder();
        JSONArray Messages = null;
        try{
            URL url = new URL(URL_ZENAPP+"WS_AcceptRefuseFriend.php");
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");

                String urlParameters = "id_user="+User.id_user+"&id_ami="+id_ami+"&statut="+statut;


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
