package com.learning.pasardesatanjunguas;

/**
 * Dibuat oleh Maulana As'an, 10/01/23
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.learning.pasardesatanjunguas.activity.CheckOutActivity;
import com.learning.pasardesatanjunguas.activity.DetailActivity;
import com.learning.pasardesatanjunguas.activity.HistoryActivity;
import com.learning.pasardesatanjunguas.activity.SignInActivity;
import com.learning.pasardesatanjunguas.adapter.ProdukAdapter;
import com.learning.pasardesatanjunguas.model.ProdukModel;
import com.learning.pasardesatanjunguas.util.AppController;
import com.learning.pasardesatanjunguas.util.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ProdukAdapter.ItemClickListener {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mManager;
    ArrayList<ProdukModel> mItems;
    ProgressDialog pd;

    private ProdukAdapter adapter;
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    public static String user_name;
    public TextView totalPrice;
    public static double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user_name = user.getDisplayName();
        } else {
            user_name = SignInActivity.userName;
        }
        Toast.makeText(this, "Selamat Datang " + user_name, Toast.LENGTH_SHORT).show();


        mRecyclerView = findViewById(R.id.recyclerView);
        pd = new ProgressDialog(MainActivity.this);
        mItems = new ArrayList<>();

        loadJson();

        adapter = new ProdukAdapter(mItems);
        mManager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        totalPrice = findViewById(R.id.totalPrice);
        totalPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CheckOutActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_call_center:
                Toast.makeText(getApplicationContext(), "Call Center",
                        Toast.LENGTH_SHORT).show();
                Intent iCall = new Intent(Intent.ACTION_DIAL);
                iCall.setData(Uri.parse("tel:087730393303"));
                if (iCall.resolveActivity(getPackageManager()) != null) {
                    startActivity(iCall);
                }
                return true;
            case R.id.action_sms_center:
                Toast.makeText(getApplicationContext(), "SMS Center",
                        Toast.LENGTH_SHORT).show();
                Intent iSMS = new Intent(Intent.ACTION_SENDTO);
                iSMS.setData(Uri.parse("smsto:087730393303"));
                if (iSMS.resolveActivity(getPackageManager()) != null) {
                    startActivity(iSMS);
                }
                return true;
            case R.id.action_lokasi:
                Toast.makeText(getApplicationContext(), "Lokasi",
                        Toast.LENGTH_SHORT).show();
                Intent iLoc = new Intent(Intent.ACTION_VIEW);
                iLoc.setData(Uri.parse("geo:0,0?q=Kantor+Kelurahan+Tanjung+Klego"));
                if (iLoc.resolveActivity(getPackageManager()) != null) {
                    startActivity(iLoc);
                }
                return true;
            case R.id.action_history:
                Toast.makeText(getApplicationContext(), "History",
                        Toast.LENGTH_SHORT).show();
                Intent iHistory = new Intent(this, HistoryActivity.class);
                startActivity(iHistory);
                return true;
            case R.id.action_update_user:
                Toast.makeText(getApplicationContext(), "Update User",
                        Toast.LENGTH_SHORT).show();
//                Intent iUpdUser = new Intent(this, UpdateUserActivity.class);
//                startActivity(iUpdUser);
                return true;
            case R.id.action_logout_user:
                Toast.makeText(getApplicationContext(), "Logout",
                        Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent iOutUser = new Intent(this, SignInActivity.class);
                startActivity(iOutUser);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void postTotal() {
        totalPrice.setText("Total : " + rpFormat.format(total));
    }

    @Override
    public void onClick(View view, int position) {

        final ProdukModel product = mItems.get(position);
        Double price = Double.parseDouble(product.getHrga_prdk());

        switch (view.getId()) {
            case R.id.productNama:
                Toast.makeText(this, product.getNama_prdk(),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra("kode", product.getKode_prdk());
                intent.putExtra("nama", product.getNama_prdk());
                intent.putExtra("deks", product.getDesk_prdk());
                intent.putExtra("hrga", product.getHrga_prdk());
                intent.putExtra("foto", product.getFoto_prdk());
                startActivity(intent);
                return;
            case R.id.productFoto:
                total += price;
                Toast.makeText(this, rpFormat.format(price), Toast.LENGTH_SHORT).show();
                postTotal();
                return;
            default:
                break;
        }
    }

    private void loadJson() {

        pd.setMessage("Mengambil data produk...");
        pd.setCancelable(false);
        pd.show();

        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.POST, ServerAPI.URL_VIEW_PRDK,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pd.cancel();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject data = response.getJSONObject(i);
                        ProdukModel mp = new ProdukModel();
                        mp.setKode_prdk(data.getString("kode_prdk"));
                        mp.setNama_prdk(data.getString("nama_prdk"));
                        mp.setDesk_prdk(data.getString("desk_prdk"));
                        mp.setHrga_prdk(data.getString("hrga_prdk"));
                        mp.setFoto_prdk(data.getString("foto_prdk"));
                        mItems.add(mp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                }
                Log.d("Produk Data", "Berhasil mengambil data produk!!");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Toast.makeText(MainActivity.this,
                        "Gagal menghubungkan ke server!!", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(reqData);
    }
}