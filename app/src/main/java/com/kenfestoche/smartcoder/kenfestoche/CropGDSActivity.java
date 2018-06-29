package com.kenfestoche.smartcoder.kenfestoche;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class CropGDSActivity extends Activity {

    CropImageView mCropView;
    Uri sourceUri;
    ImageView imRotate;
    Button btValid;
    String pathURI;
    TextView txHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        //pathURI =
        //File file = new File(pathURI);
        sourceUri = (Uri) getIntent().getExtras().get("PathUri");

        mCropView = findViewById(R.id.cropImageView);
        imRotate = findViewById(R.id.imRotate);
        btValid = findViewById(R.id.btCrop);

        Typeface faceGenerica=Typeface.createFromAsset(this.getAssets(),"Generica.otf");

        txHeader = findViewById(R.id.txHeader);
        txHeader.setTypeface(faceGenerica);


        Typeface face=Typeface.createFromAsset(this.getAssets(),"fonts/weblysleekuil.ttf");

        btValid.setTypeface(face);


        //mCropView.setOutputMaxSize(400, 400);
        mCropView.setCustomRatio(1,1);
        mCropView.load(sourceUri).execute(new LoadCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });


        btValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCropView.crop(sourceUri)
                        .execute(new CropCallback() {
                            @Override public void onSuccess(Bitmap cropped) {
                                 Uri tempUri = getImageUri(CropGDSActivity.this, cropped);

                                File finalFile = new File(getRealPathFromURI(tempUri));
                                //mImageCaptureUri= (Uri) data.getExtras().get("URI");
                                String picturePath = finalFile.getPath();
                                WebService WS = new WebService(CropGDSActivity.this);
                                WS.UploadImage(picturePath,FragmentsSliderActivity.User);

                                finish();
                            }

                            @Override public void onError(Throwable e) {
                            }
                        });
            }
        });

        imRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
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
