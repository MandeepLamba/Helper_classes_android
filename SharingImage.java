package com.mnnu.volleyapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

public class SharingImage {
    public static final int WRITE_EXTERNAL_PERMISSION_REQUEST_CODE = 34344;
    private Context context;
    private static final String TAG = "SharingImage";


    public void shareImage(View view) {
        if (view != null) {
            if (checkPermission()) {
                Bitmap result = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(result);
                Drawable drawable = view.getBackground();
                if (drawable != null) drawable.draw(canvas);
                else canvas.drawColor(Color.WHITE);
                view.draw(canvas);
                startSharing(result);
            }
        } else Log.d(TAG, "shareImage: View is null sharing Failed");
    }
    public void shareImage(View view, String message) {
        if (view != null) {
            if (checkPermission()) {
                Bitmap result = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(result);
                Drawable drawable = view.getBackground();
                if (drawable != null) drawable.draw(canvas);
                else canvas.drawColor(Color.WHITE);
                view.draw(canvas);
                startSharing(result, message);
            }
        } else Log.d(TAG, "shareImage: View is null sharing Failed");
    }

    public void shareImageToWhatsApp(View view, String message){
        if (view != null) {
            if (checkPermission()) {
                try {
                    context.getPackageManager().getPackageInfo("com.whatsapp",0);
                    Bitmap result = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(result);
                    Drawable drawable = view.getBackground();
                    if (drawable != null) drawable.draw(canvas);
                    else canvas.drawColor(Color.WHITE);
                    view.draw(canvas);
                    startSharing(result, message, "com.whatsapp");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
                }

            }
        } else Log.d(TAG, "shareImage: View is null sharing Failed");
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermission: Permissions are Granted");
            return true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (((Activity) context).shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(context).setTitle("Permission Required")
                        .setMessage("Write Permission is required to create and share profile badges")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((Activity) context).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_PERMISSION_REQUEST_CODE);
                            }
                        })
                        .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "Permission Denial", Toast.LENGTH_SHORT).show();
                            }
                        }).show();

            } else
                ((Activity) context).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_PERMISSION_REQUEST_CODE);
        }
        return false;
    }


    private void startSharing(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Profile Badge", "My Profile Badge");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path)).setType("image/*");
        context.startActivity(intent);
    }

    private void startSharing(Bitmap bitmap,String message) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Profile Badge", "My Profile Badge");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path)).setType("image/*");
        context.startActivity(intent);
    }
    private void startSharing(Bitmap bitmap,String message, String pack) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Profile Badge", "My Profile Badge");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setPackage(pack);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path)).setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    public SharingImage(Context context) {
        this.context = context;
    }

    //paste below code in requesting Activity for better result
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == SharingImage.WRITE_EXTERNAL_PERMISSION_REQUEST_CODE){
//            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(this, "Permission Granted Successfully", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                Toast.makeText(this, "Sharing will we unavailable due to lack of Permissions", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}
