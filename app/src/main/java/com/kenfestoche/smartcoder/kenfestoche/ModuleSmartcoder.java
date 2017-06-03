package com.kenfestoche.smartcoder.kenfestoche;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by smartcoder on 22/01/2017.
 */

public class ModuleSmartcoder {

    public static File savbitmap(Bitmap bmp, String nameFile) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if(nameFile.toLowerCase().contains("png")) {
            bmp.compress(Bitmap.CompressFormat.PNG, 60, bytes);
        }else{
            bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        }

        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + nameFile);
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }

    public static Bitmap gebitmap(String nameFile) throws IOException {

        Bitmap fichierbmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                + File.separator + nameFile);

        return fichierbmp;
    }

}
