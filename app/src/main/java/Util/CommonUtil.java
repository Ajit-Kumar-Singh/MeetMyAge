package Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CommonUtil {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;  // Declare this integer globally

    public static List<String> checkAndRequestPermissions(Context context)
    {
        int camera = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
        int readStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeStorage = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int fineLoc = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLoc = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writeStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (fineLoc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (coarseLoc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        return listPermissionsNeeded;
    }

    public static String encodeImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;

    }

    public static Bitmap compressBitmap(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                /*
                    public boolean compress (Bitmap.CompressFormat format, int quality, OutputStream stream)
                        Write a compressed version of the bitmap to the specified outputstream.
                        If this returns true, the bitmap can be reconstructed by passing a
                        corresponding inputstream to BitmapFactory.decodeStream().

                        Note: not all Formats support all bitmap configs directly, so it is possible
                        that the returned bitmap from BitmapFactory could be in a different bitdepth,
                        and/or may have lost per-pixel alpha (e.g. JPEG only supports opaque pixels).

                        Parameters
                        format : The format of the compressed image
                        quality : Hint to the compressor, 0-100. 0 meaning compress for small size,
                            100 meaning compress for max quality. Some formats,
                            like PNG which is lossless, will ignore the quality setting
                        stream: The outputstream to write the compressed data.

                        Returns
                            true if successfully compressed to the specified stream.
                */

                /*
                    Bitmap.CompressFormat
                        Specifies the known formats a bitmap can be compressed into.

                            Bitmap.CompressFormat  JPEG
                            Bitmap.CompressFormat  PNG
                            Bitmap.CompressFormat  WEBP
                */
        // Compress the bitmap with JPEG format and quality 50%
        bitmap.compress(Bitmap.CompressFormat.JPEG,40,stream);

        byte[] byteArray = stream.toByteArray();
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        return compressedBitmap;
    }
    public static Bitmap convertStringToBitmap(String imageString)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        //decode base64 string to image
        imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    public static boolean permissions(List<String> listPermissionsNeeded, Activity activity) {

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), CommonUtil.REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
