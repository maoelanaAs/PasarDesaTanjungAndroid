package com.learning.pasardesatanjunguas.activity;

/**
 * Dibuat oleh Maulana As'an, 10/01/23
 */

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.learning.pasardesatanjunguas.R;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    TextView detailNama, detailDeks, detailHrga;
    ImageView detailFoto;

    String produkKode, produkNama, produkDeks, produkHrga, produkFoto;
    Double harga;

    NumberFormat rpFormat = NumberFormat.getCurrencyInstance
            (new Locale("in", "ID"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailNama = findViewById(R.id.detailNama);
        detailDeks = findViewById(R.id.detailDeks);
        detailHrga = findViewById(R.id.detailHrga);
        detailFoto = findViewById(R.id.detailFoto);

        produkKode = getIntent().getStringExtra("kode");
        produkNama = getIntent().getStringExtra("nama");
        produkDeks = getIntent().getStringExtra("deks");
        produkHrga = getIntent().getStringExtra("hrga");
        produkFoto = getIntent().getStringExtra("foto");
        harga = Double.parseDouble(produkHrga);

        detailNama.setText(produkNama);
        detailDeks.setText(produkDeks);
        detailHrga.setText(rpFormat.format(harga));

        Glide
                .with(this)
                .load(produkFoto)
                .placeholder(R.drawable.loading)
                .error(R.drawable.foto_kosong)
                .into(detailFoto);
    }
}