package com.kenfestoche.smartcoder.kenfestoche;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by smartcoder on 12/12/2017.
 */

public class ActivityAdjustPhoto extends Activity {


    CropImageView cropimageView;
    Button Valider;
    ImageView imRotate;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityphoto);
        cropimageView = findViewById(R.id.cropImageView);
        imRotate = findViewById(R.id.imRotate);
        Valider = findViewById(R.id.btValid);

        uri= (Uri) getIntent().getExtras().get("urlPhoto");

        cropimageView.setImageUriAsync(uri);


        imRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropimageView.setRotatedDegrees(cropimageView.getRotatedDegrees()+90);
            }
        });

        Valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap photo = cropimageView.getCroppedImage();
                uri = getImageUri(getApplicationContext(), photo);
                File finalFile = new File(getRealPathFromURI(uri));
                String picturePath = finalFile.getPath();
                WebService WS = new WebService(getApplicationContext());
                WS.UploadImage(picturePath,FragmentsSliderActivity.User);

                finish();
            }
        });

        cropimageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {

            }
        });

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);

    }
}
