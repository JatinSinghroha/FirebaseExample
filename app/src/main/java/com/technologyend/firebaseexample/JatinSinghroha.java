package com.technologyend.firebaseexample;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URLEncoder;

import androidx.appcompat.app.AppCompatActivity;

public class JatinSinghroha extends AppCompatActivity {

    private ImageView whatsappIMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jatin_singhroha);
        setTitle("Developed by");

        whatsappIMG = findViewById(R.id.imageWP);

        whatsappIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager packageManager = JatinSinghroha.this.getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                try {
                    String url = "https://api.whatsapp.com/send?phone="+ "+918950121519" +"&text=" + URLEncoder.encode("Thanks for developing Awesome Friendly Chat App", "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        JatinSinghroha.this.startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(JatinSinghroha.this, "Whatsapp Not Found. Please install Whatsapp!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    Toast.makeText(JatinSinghroha.this, "Some Error Occured!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}