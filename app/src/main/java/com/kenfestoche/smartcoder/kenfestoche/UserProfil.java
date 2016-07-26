package com.kenfestoche.smartcoder.kenfestoche;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserProfil extends AppCompatActivity {

    RadioButton rbcalme1;
    RadioButton rbcalme2;
    RadioButton rbcalme3;
    RadioButton rbcalme4;
    RadioButton rbcalme5;

    RadioButton rbverre1;
    RadioButton rbverre2;
    RadioButton rbverre3;
    RadioButton rbverre4;
    RadioButton rbverre5;

    Button Parametre;

    ImageView imgAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profil);

        rbcalme1 = (RadioButton) findViewById(R.id.rdcalme1);
        rbcalme2 = (RadioButton) findViewById(R.id.rdcalme2);
        rbcalme3 = (RadioButton) findViewById(R.id.rdcalme3);
        rbcalme4 = (RadioButton) findViewById(R.id.rdcalme4);
        rbcalme5 = (RadioButton) findViewById(R.id.rdcalme5);

        rbverre1 = (RadioButton) findViewById(R.id.rdverre1);
        rbverre2 = (RadioButton) findViewById(R.id.rdverre2);
        rbverre3 = (RadioButton) findViewById(R.id.rdverre3);
        rbverre4 = (RadioButton) findViewById(R.id.rdverre4);
        rbverre5 = (RadioButton) findViewById(R.id.rdverre4);

        Parametre = (Button) findViewById(R.id.btParametre);

        imgAdd = (ImageView) findViewById(R.id.imgaddprofil);

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        Parametre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SettingsEASER.class);
                startActivity(i);

            }
        });

        rbcalme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbcalme1.setChecked(true);
                rbcalme2.setChecked(false);
                rbcalme3.setChecked(false);
                rbcalme4.setChecked(false);
                rbcalme5.setChecked(false);
            }
        });

        rbcalme2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbcalme1.setChecked(true);
                rbcalme2.setChecked(true);
                rbcalme3.setChecked(false);
                rbcalme4.setChecked(false);
                rbcalme5.setChecked(false);
            }
        });

        rbcalme3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbcalme1.setChecked(true);
                rbcalme2.setChecked(true);
                rbcalme3.setChecked(true);
                rbcalme4.setChecked(false);
                rbcalme5.setChecked(false);
            }
        });

        rbcalme4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbcalme1.setChecked(true);
                rbcalme2.setChecked(true);
                rbcalme3.setChecked(true);
                rbcalme4.setChecked(true);
                rbcalme5.setChecked(false);
            }
        });

        rbcalme5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbcalme1.setChecked(true);
                rbcalme2.setChecked(true);
                rbcalme3.setChecked(true);
                rbcalme4.setChecked(true);
                rbcalme5.setChecked(true);
            }
        });

        rbverre1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbverre1.setChecked(true);
                rbverre2.setChecked(false);
                rbverre3.setChecked(false);
                rbverre4.setChecked(false);
                rbverre5.setChecked(false);
            }
        });

        rbverre2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbverre1.setChecked(true);
                rbverre2.setChecked(true);
                rbverre3.setChecked(false);
                rbverre4.setChecked(false);
                rbverre5.setChecked(false);
            }
        });

        rbverre3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbverre1.setChecked(true);
                rbverre2.setChecked(true);
                rbverre3.setChecked(true);
                rbverre4.setChecked(false);
                rbverre5.setChecked(false);
            }
        });

        rbverre4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbverre1.setChecked(true);
                rbverre2.setChecked(true);
                rbverre3.setChecked(true);
                rbverre4.setChecked(true);
                rbverre5.setChecked(false);
            }
        });

        rbverre5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbverre1.setChecked(true);
                rbverre2.setChecked(true);
                rbverre3.setChecked(true);
                rbverre4.setChecked(true);
                rbverre5.setChecked(true);
            }
        });


    }
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        /*intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);*/
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            Uri selectedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor c = getContentResolver().query(
                    selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();

            // Bitmap thumbnail =
            // (BitmapFactory.decodeFile(picturePath));
            Bitmap thumbnail = BitmapFactory.decodeFile(
                    picturePath);
            imgAdd.setImageBitmap(thumbnail);
            //Get image
              /*  Bitmap newProfilePic = extras.getParcelable("data");
                imgAdd.setImageBitmap(newProfilePic);
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);*/
            WebService WS = new WebService();
            WS.UploadImage(picturePath);

        }
    }



    private void selectImage() {
        //SyncStateContract.Constants.iscamera = true;
        final CharSequence[] items = { "Prendre une photo", "Choisir dans la bibliothèque", "Se connecter à facebook",
                "Annuler" };

        TextView title = new TextView(getApplicationContext());
        title.setText("Ajouter une photo!");
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);


        AlertDialog.Builder builder = new AlertDialog.Builder(
                UserProfil.this);



        builder.setCustomTitle(title);

        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Prendre une photo")) {
                    // Intent intent = new
                    // Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    Intent intent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                /*
                 * File photo = new
                 * File(Environment.getExternalStorageDirectory(),
                 * "Pic.jpg"); intent.putExtra(MediaStore.EXTRA_OUTPUT,
                 * Uri.fromFile(photo)); imageUri = Uri.fromFile(photo);
                 */
                    // startActivityForResult(intent,TAKE_PICTURE);

                    /*Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                    intents.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);*/

                    // start the image capture Intent
                    startActivityForResult(intent, 0);

                } else if (items[item].equals("Choisir dans la bibliothèque")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                } else if (items[item].equals("Annuler")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    public void uploadFileToServer(String pathImg){

    }
}
