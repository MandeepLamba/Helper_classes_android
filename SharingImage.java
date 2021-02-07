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
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

public class SharingImage {
    private static final int WRITE_EXTERNAL_PERMISSION_REQUEST_CODE = 3434;
    private Context context;


    public void shareImage(View view){
        Bitmap result = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Drawable drawable = view.getBackground();
        if(drawable != null){
            drawable.draw(canvas);
        }
        else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        if(checkPermission()){
            startSharing(result);
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            return true;
        } else if (((Activity)context).shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            new AlertDialog.Builder(context)
                    .setTitle("Permission Required")
                    .setMessage("Write Permission is required to create and share profile badges")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ((Activity) context).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(context, "Permission Denial", Toast.LENGTH_SHORT).show();
                        }
                    }).show();

        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            ((Activity) context).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_PERMISSION_REQUEST_CODE);
        }
        return false;
    }


    private void startSharing(Bitmap bitmap){
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Profile Badge", "My Profile Badge");
        Uri uri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        context.startActivity(intent);
    }


    public SharingImage(Context context){
        this.context = context;
    }

    //paste below code in requesting Activity for better result
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == 3434){
//            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(this, "Permission Granted Successfully", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                Toast.makeText(this, "Sharing will we unavailable due to lack of Permissions", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}
