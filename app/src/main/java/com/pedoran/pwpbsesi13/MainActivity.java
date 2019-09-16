package com.pedoran.pwpbsesi13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pedoran.pwpbsesi13.QRCodeLibrary.ZxingLibraryExample;
import com.pedoran.pwpbsesi13.TextToPdf.ITextLibraryExample;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLat1 = findViewById(R.id.btn_lat1);
        btnLat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pindah = new Intent(MainActivity.this, ZxingLibraryExample.class);
                startActivity(pindah);
            }
        });

        Button btnLat2 = findViewById(R.id.btn_lat2);
        btnLat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pindah = new Intent(MainActivity.this, ITextLibraryExample.class);
                startActivity(pindah);
            }
        });
    }
}
