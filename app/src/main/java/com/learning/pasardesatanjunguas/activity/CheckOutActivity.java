package com.learning.pasardesatanjunguas.activity;

/**
 * Dibuat oleh Maulana As'an, 10/01/23
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.learning.pasardesatanjunguas.MainActivity;
import com.learning.pasardesatanjunguas.R;
import com.learning.pasardesatanjunguas.activity.cekongkir.CekOngkirActivity;
import com.learning.pasardesatanjunguas.util.AppController;
import com.learning.pasardesatanjunguas.util.ServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {

    TextView noNota, tglJual, userName, totalJual, ongkir, grandTotal, statusByr;
    ImageView statusImg, buktiByr;
    Button cekOngBtn, galeriBtn, kirimBtn, cetakBtn, batalBtn;
    ProgressDialog pd;
    Bitmap bitmap;
    byte[] bytes;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String nonota, username, tglSkrg, status = "Belum Lunas";

    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
    double totals, ongkis, grands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        pd = new ProgressDialog(this);
        noNota = findViewById(R.id.noNota);
        tglJual = findViewById(R.id.tglJual);
        userName = findViewById(R.id.userName);
        totalJual = findViewById(R.id.totalJual);
        ongkir = findViewById(R.id.ongkir);
        grandTotal = findViewById(R.id.grandTotal);
        statusByr = findViewById(R.id.statusByr);
        statusImg = findViewById(R.id.statusImg);
        buktiByr = findViewById(R.id.buktiByr);

        cekOngBtn = findViewById(R.id.cekOngBtn);
        galeriBtn = findViewById(R.id.galeriBtn);
        kirimBtn = findViewById(R.id.kirimBtn);
        cetakBtn = findViewById(R.id.cetakBtn);
        batalBtn = findViewById(R.id.batalBtn);

        tglSkrg = sdf.format(new Date());
        username = MainActivity.user_name;
        totals = MainActivity.total;
        ongkis = getIntent().getDoubleExtra("ongkir", 0);
        grands = totals + ongkis;

        setAllTextAndImg();

        cekOngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckOutActivity.this, CekOngkirActivity.class));
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                if (result.getResultCode() == Activity.RESULT_OK) {
                                    Intent data = result.getData();
                                    Uri uri = data.getData();
                                    try {
                                        bitmap = MediaStore.Images
                                                .Media.getBitmap(getContentResolver(), uri);
                                        buktiByr.setImageBitmap(bitmap);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        });

        galeriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });

        kirimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    bytes = byteArrayOutputStream.toByteArray();
                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

                    pd.setMessage("Kirim bukti transfer...");
                    pd.setCancelable(false);
                    pd.show();
                    StringRequest jualReq = new StringRequest(Request.Method.POST,
                            ServerAPI.JUAL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.cancel();
                            try {
                                JSONObject res = new JSONObject(response);
                                if (res.getString("code").equals("1")) {
                                    Toast.makeText(CheckOutActivity.this,
                                            res.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                    nonota = res.getString("no_nota");
                                    status = "Lunas";
                                    setAllTextAndImg();
                                    galeriBtn.setVisibility(View.GONE);
                                    kirimBtn.setVisibility(View.GONE);
                                    buktiByr.setVisibility(View.GONE);
                                    cekOngBtn.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(CheckOutActivity.this,
                                            res.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.cancel();
                            Toast.makeText(CheckOutActivity.this,
                                    "Gagal menghubungkan ke server!!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("tglJual", tglSkrg);
                            map.put("namaKons", username);
                            map.put("totalJual", String.valueOf(totals));
                            map.put("ongkir", String.valueOf(ongkis));
                            map.put("grandTotal", String.valueOf(grands));
                            map.put("statusByr", "Lunas");
                            map.put("buktiByr", base64Image);
                            return map;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(jualReq);
                } else {
                    Toast.makeText(CheckOutActivity.this,
                            "Upload bukti pembayaran terlebih dahulu",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        cetakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        batalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.total = 0;
                startActivity(new Intent(CheckOutActivity.this, MainActivity.class));
            }
        });
    }

    private void setAllTextAndImg() {
        noNota.setText(nonota);
        tglJual.setText(tglSkrg);
        userName.setText(username);
        totalJual.setText(rpFormat.format(totals));
        ongkir.setText(rpFormat.format(ongkis));
        grandTotal.setText(rpFormat.format(grands));
        statusByr.setText(status);

        if (status.equals("Lunas")) {
            statusImg.setImageResource(R.drawable.ic_berhasil);
        } else {
            statusImg.setImageResource(R.drawable.ic_gagal);
        }
    }
}