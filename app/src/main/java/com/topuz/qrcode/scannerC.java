package com.topuz.qrcode;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class scannerC extends AppCompatActivity {
    private  Button again;
    private Button run;
    private TextView text;
    CardView backHome;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        idMatch();


        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receivedText=run.getText().toString().trim();
                String QRText=text.getText().toString().trim();
                if(receivedText.contains("link")){
                    Uri url=Uri.parse(QRText);
                    Intent intent =new Intent(Intent.ACTION_VIEW,url);
                    startActivity(intent);
                }
                else{
                    ClipboardManager clipboardManager = (ClipboardManager)
                            getSystemService(CLIPBOARD_SERVICE);
                    clipboardManager.setText(QRText);
                    Toast.makeText(getApplicationContext(),"Copied",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gg=new Intent(scannerC.this,
                        scannerC.class);
                startActivity(gg);
                finish();
            }
        });
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l=new Intent(scannerC.this,MainActivity.class);
                startActivity(l);
                finish();
            }
        });
            IntentIntegrator integrator=new IntentIntegrator(scannerC.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scanner");
            integrator.setBeepEnabled(true);
            integrator.setOrientationLocked(true);
            integrator.setCaptureActivity(Capture.class);
            integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                Intent intent=new Intent(scannerC.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                String receivedText=result.getContents().toString().trim();
                String lastfour=receivedText.substring(receivedText.length()-4);
                text.setText(receivedText);
                if(lastfour.contains(".co")||lastfour.contains(".xyz")
                        ||lastfour.contains(".com")
                ||lastfour.contains(".org")||lastfour.contains(".net")){
                    run.setText("Go to the link ");
                }
                else{
                    run.setText("Copy  ");
                }
                run.setVisibility(View.VISIBLE);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent k=new Intent (scannerC.this,MainActivity.class);
        startActivity(k);
        finish();
    }
    public void idMatch(){
        again=findViewById(R.id.button3);
        run=findViewById(R.id.run);
        text=findViewById(R.id.textView2);
        backHome=findViewById(R.id.cardView3);
    }
}