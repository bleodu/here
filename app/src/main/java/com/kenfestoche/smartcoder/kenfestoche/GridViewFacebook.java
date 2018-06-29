package com.kenfestoche.smartcoder.kenfestoche;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.kenfestoche.smartcoder.kenfestoche.model.ImagesFacebook;
import com.kenfestoche.smartcoder.kenfestoche.model.ImagesProfil;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;
import com.yalantis.ucrop.UCrop;

public class GridViewFacebook extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Activity mActivity;
    Utilisateur User;
    String picturePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_facebook);

        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();
        User = FragmentsSliderActivity.User;
//        mActivity = (Activity) getIntent().getExtras().get("UserActivity");
        Utilisateur User = FragmentsSliderActivity.User;

        GridView gridview = (GridView) findViewById(R.id.gdfacebook);
        gridview.setAdapter(new ImagesFacebook(this,1,User,getContentResolver()));



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == UCrop.REQUEST_CROP || requestCode == -1) { //CROP IMAGE

            final Uri resultUri = UCrop.getOutput(data);
            /*File finalFile = new File(getRealPathFromURI(resultUri));
            //mImageCaptureUri= (Uri) data.getExtras().get("URI");
            picturePath = finalFile.getPath();*/

            SharedPreferences pref = this.getSharedPreferences("EASER", this.MODE_PRIVATE);

            SharedPreferences.Editor edt = pref.edit();

            this.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    WebService WS = new WebService(getApplicationContext());
                    WS.UploadImage(resultUri.getPath(),User);

                    finish();


                }
            });

            /*Uri selectedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor c = this.getContentResolver().query(
                    selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            picturePath = c.getString(columnIndex);
            c.close();


            SharedPreferences pref = this.getSharedPreferences("EASER", this.MODE_PRIVATE);

            SharedPreferences.Editor edt = pref.edit();

            this.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    WebService WS = new WebService(getApplicationContext());
                    WS.UploadImage(picturePath,User);



                }
            });*/

        }else{
            finish();
        }
    }
}
