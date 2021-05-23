package com.topuz.qrcode;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class createC extends AppCompatActivity {
    CardView backButton;
    EditText editText;
    Button createCode,download,shareButton;
    ImageView codeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
         idMatch();

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sharePhoto();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(createC.this,MainActivity.class);
                startActivity(intent);
                    finish();
                }
        });
        createCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String metin=editText.getText().toString().trim();
                if(metin.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter Text",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    MultiFormatWriter multiFormatWriter =new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix=multiFormatWriter.encode(metin,
                                BarcodeFormat.QR_CODE,500,500);
                        BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                        Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                        codeImage.setImageBitmap(bitmap);
                        download.setVisibility(View.VISIBLE);
                        shareButton.setVisibility(View.VISIBLE);
                    }catch (Exception e){
                    }
                }
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                }
                else {
                    final View Layout = getLayoutInflater().inflate(R.layout.imagename, null);
                    final EditText TextIn = Layout.findViewById(R.id.edits);
                    AlertDialog.Builder builder = new AlertDialog.Builder(createC.this,R.style.MyDialogTheme);
                    builder.setTitle("File name:");
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String fileNAME=TextIn.getText().toString().trim();
                            if(fileNAME.equals("")){
                                Toast.makeText(getApplicationContext(),"Give a name \n not saved ",
                                        Toast.LENGTH_LONG).show();
                            }else{
                                BitmapDrawable drawable= (BitmapDrawable) codeImage.getDrawable();
                                Bitmap bitmap=drawable.getBitmap();
                                FileOutputStream outputStream=null;
                                File filePath= Environment.getExternalStorageDirectory();
                                File dir =new File(filePath.getAbsolutePath()+"/topuz");
                                dir.mkdirs();
                                File file=new File(dir,String.format(fileNAME+".jpg"));
                                try {
                                    outputStream=new FileOutputStream(file);
                                }
                                catch (Exception e){
                                    Toast.makeText(getApplicationContext(),"HATA",Toast.LENGTH_LONG).show();
                                }
                                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                                Toast.makeText(getApplicationContext(),"Image Saved\n/topuz/"
                                                +fileNAME+".jpg",
                                        Toast.LENGTH_LONG).show();
                                try {
                                    outputStream.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    outputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        });
                        final AlertDialog alertDialog = builder.create();
                    alertDialog.setView(Layout);
                    alertDialog.show();
            }}
        });



    }
    private void sharePhoto(){
        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        BitmapDrawable drawable=(BitmapDrawable) codeImage.getDrawable();
        Bitmap bitmap=drawable.getBitmap();
        File f=new File(getExternalCacheDir()+"/"+"QR Code"+".jpg");
        Intent shareint = null;
        try {
            FileOutputStream outputStream=new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();
            outputStream.close();
            shareint=new Intent (Intent.ACTION_SEND);
            shareint.setType("image/*");
            shareint.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(f));
            shareint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        catch (Exception e){

        }
        startActivity(Intent.createChooser(shareint,"Share"));
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent=new Intent (createC.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void idMatch(){
        editText=findViewById(R.id.editTextCreateActivity);
        createCode=findViewById(R.id.createCode);
        codeImage=findViewById(R.id.imageView2);
        download=findViewById(R.id.downloadButton);
        backButton=findViewById(R.id.cardView2);
        shareButton=findViewById(R.id.shareButton);
    }
}