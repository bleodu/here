package com.kenfestoche.smartcoder.kenfestoche.webservices;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.kenfestoche.smartcoder.kenfestoche.FragmentsSliderActivity;
import com.kenfestoche.smartcoder.kenfestoche.MainActivity;
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
    Context ctx;
    Boolean Mess=true;
    public static final String URL_ZENAPP = "http://www.smartcoder-dev.fr/ZENAPP/webservice/";

    public WebService(Context context){
        ctx= context;
    }

    public WebService(Context context,boolean Message){
        ctx= context;
        Mess=Mess;
    }

    public Utilisateur Connect(String Login, String Pass) {
        if(isOnline()){
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
                        user.positiontous=Uti.getInt("localiser");
                        user.positionamis=Uti.getInt("localiseramis");
                        user.activnotif=Uti.getInt("activnotif");
                        user.inclusfb=Uti.getInt("inclusfb");
                        user.profilsaufkiffs=Uti.getInt("profilsaufkiffs");
                        user.popupmessage=Uti.getInt("popupmessage");
                        user.popupmap=Uti.getInt("popupmap");
                        user.popupprofils=Uti.getInt("popuprofils");

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
        }else{
            return new Utilisateur();
        }

    }



    public void UploadImage(String pathImg,Utilisateur user){
        if(isOnline()){
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

    }

    public Utilisateur SaveUser(Utilisateur User) {
        if(isOnline()){
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

                    String urlParameters =  "popupmessage="+User.popupmessage+"&popupmap="+User.popupmap+"&popupprofil="+User.popupprofils+"&profilsaufkiffs="+User.profilsaufkiffs+"&inclusfb="+User.inclusfb+"&activnotif="+User.activnotif+"&localiser="+User.positiontous+"&localiseramis="+User.positionamis+"&tokenFirebase="+User.tokenFirebase+"&id="+User.id_user+"&calme="+User.calme+"&affinity="+User.affinity+"&qqmots="+User.description+"&statut="+User.statut+"&email="+User.email+"&idfacebook="+User.id_facebook+"&login="+User.login+"&phone="+User.phone+"&password="+User.password+"&age="+User.age+"&sexe="+User.sexe+"&tendancesexe="+User.tendancesexe+"&latitude="+User.latitude+"&longitude="+User.longitude;

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
                            User.positiontous=Uti.getInt("localiser");
                            User.positionamis=Uti.getInt("localiseramis");
                            User.activnotif=Uti.getInt("activnotif");
                            User.inclusfb=Uti.getInt("inclusfb");
                            User.profilsaufkiffs=Uti.getInt("profilsaufkiffs");
                            User.popupmessage=Uti.getInt("popupmessage");
                            User.popupmap=Uti.getInt("popupmap");
                            User.popupprofils=Uti.getInt("popuprofils");

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
        }else{
            return new Utilisateur();
        }

    }

    public JSONArray SetKiffOpen(Utilisateur User,String idKiffs) {
        if(isOnline()){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            StringBuilder builder = new StringBuilder();
            JSONArray Messages = null;
            //Utilisateur user=null;
            try{
                URL url = new URL(URL_ZENAPP+"WS_SetKiffOpen.php");
                HttpURLConnection client = null;
                try {
                    client = (HttpURLConnection) url.openConnection();
                    client.setRequestMethod("POST");

                    String urlParameters =  "id_user="+User.id_user+"&id_kiff="+idKiffs;


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
        }else{
            return new JSONArray();
        }

    }

    public Utilisateur SaveUserPopUp(Utilisateur User) {
        if(isOnline()){
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

                    String urlParameters =  "popupmessage="+User.popupmessage+"&popupmap="+User.popupmap+"&popupprofil="+User.popupprofils+"&profilsaufkiffs="+User.profilsaufkiffs+"&inclusfb="+User.inclusfb+"&activnotif="+User.activnotif+"&localiser="+User.positiontous+"&localiseramis="+User.positionamis+"&tokenFirebase="+User.tokenFirebase+"&id="+User.id_user+"&calme="+User.calme+"&affinity="+User.affinity+"&qqmots="+User.description+"&statut="+User.statut+"&email="+User.email+"&idfacebook="+User.id_facebook+"&login="+User.login+"&phone="+User.phone+"&password="+User.password+"&age="+User.age+"&sexe="+User.sexe+"&tendancesexe="+User.tendancesexe+"&latitude="+User.latitude+"&longitude="+User.longitude;

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
                            User.positiontous=Uti.getInt("localiser");
                            User.positionamis=Uti.getInt("localiseramis");
                            User.activnotif=Uti.getInt("activnotif");
                            User.inclusfb=Uti.getInt("inclusfb");
                            User.profilsaufkiffs=Uti.getInt("profilsaufkiffs");

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
        }else{
            return new Utilisateur();
        }

    }

    public Utilisateur CreateUser(Utilisateur User) {
        if(isOnline()){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            StringBuilder builder = new StringBuilder();
            JSONArray Messages = null;
            //Utilisateur user=null;
            try{
                URL url = new URL(URL_ZENAPP+"WS_CreateUser.php");
                HttpURLConnection client = null;
                try {
                    client = (HttpURLConnection) url.openConnection();
                    client.setRequestMethod("POST");

                    String urlParameters =  "popupmessage="+User.popupmessage+"&popupmap="+User.popupmap+"&popupprofil="+User.popupprofils+"&profilsaufkiffs="+User.profilsaufkiffs+"&inclusfb="+User.inclusfb+"&activnotif="+User.activnotif+"&localiser="+User.positiontous+"&localiseramis="+User.positionamis+"&tokenFirebase="+User.tokenFirebase+"&id="+User.id_user+"&calme="+User.calme+"&affinity="+User.affinity+"&qqmots="+User.description+"&statut="+User.statut+"&email="+User.email+"&idfacebook="+User.id_facebook+"&login="+User.login+"&phone="+User.phone+"&password="+User.password+"&age="+User.age+"&sexe="+User.sexe+"&tendancesexe="+User.tendancesexe+"&latitude="+User.latitude+"&longitude="+User.longitude;

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
                            User.positiontous=Uti.getInt("localiser");
                            User.positionamis=Uti.getInt("localiseramis");
                            User.activnotif=Uti.getInt("activnotif");
                            User.inclusfb=Uti.getInt("inclusfb");
                            User.profilsaufkiffs=Uti.getInt("profilsaufkiffs");

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
        }else{
            return new Utilisateur();
        }

    }

    public void DeleteUser(Utilisateur User) {
        if(isOnline()){
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
                    Utilisateur.deleteAll(Utilisateur.class);


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


    }



    public Utilisateur GetUserFacebook(String IdFacebook) {
        if(isOnline()){
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
        }else{
            return new Utilisateur();
        }

    }

    public JSONArray GetSmsCode(Utilisateur User,Boolean newSms,Boolean smsPass) {
        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray DeletePhotoProfil(int IdPhoto) {
        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }


    public JSONArray SetProfilUser(Utilisateur User) {
        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetListPhotos(Utilisateur User) {
        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetinfoKiff(String idUser,int idmoi) {
        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetinfoUser(String idUser) {
        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetMessageNonLu(int idUser, String idkiffs,String prive) {
        if(isOnline()){
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
                    String urlParameters = "id_user="+idUser+"&id_kiff="+idkiffs+"&prive="+prive;


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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray KiffUser(String idUser,String id_kiff,String statut) {
        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetLastPositionUserInconnu(Utilisateur User) {
        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetSoirees(Utilisateur User) {
        if(isOnline()){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            int soiree=0;
            int debit=0;
            int bar=0;
            int barnuit=0;
            int concert=0;
            int boite=0;

            if(User.bfetes){
                soiree=1;
            }

            if(User.bdebit){
                debit=1;
            }

            if(User.bboitenuit){
                boite=1;
            }

            if(User.bbarnuit){
                barnuit=1;
            }

            if(User.bconcert){
                concert=1;
            }

            if(User.bbar){
                bar=1;
            }



            StringBuilder builder = new StringBuilder();
            JSONArray Messages = null;
            try{
                URL url = new URL(URL_ZENAPP+"WS_GetSoirees.php");
                HttpURLConnection client = null;
                try {
                    client = (HttpURLConnection) url.openConnection();
                    client.setRequestMethod("POST");
                    String urlParameters = "id_user="+User.id_user+"&soiree="+soiree+"&debit="+debit+"&bar="+bar+"&barnuit="+barnuit+"&concert="+concert+"&boite="+boite+"&date="+ User.date+"&datedebit="+User.datedebit+"&horaire="+User.horaire+"&horairedebit="+User.horairedebit;


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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetInfoSoiree(String id) {
        if(isOnline()){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            StringBuilder builder = new StringBuilder();
            JSONArray Messages = null;
            try{
                URL url = new URL(URL_ZENAPP+"WS_GetInfoSoiree.php");
                HttpURLConnection client = null;
                try {
                    client = (HttpURLConnection) url.openConnection();
                    client.setRequestMethod("POST");
                    String urlParameters = "id="+id;


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
        }else{
            return new JSONArray();

        }


    }

    public boolean isOnline() {

        try{
            ConnectivityManager cm =
                    (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if(netInfo==null && Mess){
                Toast.makeText(ctx,"Pas de connexion internet disponible",Toast.LENGTH_LONG).show();
            }

            return netInfo != null && netInfo.isConnectedOrConnecting();

        }catch (Exception ex){
            return false;
        }


    }

    public JSONArray GetLastPositionUserMatch(Utilisateur User) {
        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetLastPositionUserAmis(Utilisateur User) {
        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetListKiffs(Utilisateur User,String KiffsStringId) {

        if(isOnline()){
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
                    if(KiffsStringId.length()>0){
                        KiffsStringId=KiffsStringId.substring(0,KiffsStringId.length()-1);
                    }

                    String urlParameters = "id_user="+User.id_user+"&listkiffs="+KiffsStringId;


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
        }else{
            return new JSONArray();
        }

    }



    public JSONArray SetLastPosition(Utilisateur User) {

        if(isOnline()){
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

        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetListUserByPhone(String Phone,Utilisateur User) {

        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetListUserByPhones(Utilisateur User) {

        if(isOnline()){

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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetDetailConversation(String  idconv,Utilisateur User) {
        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetUsersConversation(String  idconv) {
        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetStatutConversation(String  idconv,int id_user) {
        if(isOnline()){
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
        }else{
            return new JSONArray();
        }


    }

    public JSONArray GetNewDetailConversation(String  idconv,Utilisateur User) {

        if(isOnline()){

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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray SetStatutConversation(String  idconv,int Statut) {

        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetListProfils(Utilisateur User) {

        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetListPhotosProfil(String id_user) {

        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetListAmis(Utilisateur User,String IdAmis) {

        if(isOnline()){
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
                    if(IdAmis.length()>0){
                        IdAmis=IdAmis.substring(0,IdAmis.length()-1);
                    }

                    String urlParameters = "id_user="+User.id_user+"&listamis="+IdAmis;


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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetProfilPhoto(Utilisateur User) {

        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray GetListConversations(Utilisateur User) {

        if(isOnline()){

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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray AddChatMessage(Utilisateur User,String Message,String idConv) {

        if(isOnline()){
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

        }else{
            return new JSONArray();
        }

    }

    public JSONArray AddConversation(String NomConversation, String idusers,Utilisateur User) {

        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray AddConversationPrive(String iduser,String id_kiffs) {

        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray SetNewFriend(Utilisateur User,String id_ami) {

        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray DeleteKiffs(Utilisateur User,String id_kiff) {

        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray SignalerKiffs(Utilisateur User,String id_kiff) {

        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }

    public JSONArray AcceptRefuseFriend(Utilisateur User,String id_ami,String statut) {

        if(isOnline()){
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
        }else{
            return new JSONArray();
        }

    }
}
