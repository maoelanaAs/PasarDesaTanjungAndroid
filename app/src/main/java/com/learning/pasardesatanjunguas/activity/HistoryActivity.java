package com.learning.pasardesatanjunguas.activity;

/**
 * Dibuat oleh Maulana As'an, 10/01/23
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.learning.pasardesatanjunguas.MainActivity;
import com.learning.pasardesatanjunguas.R;
import com.learning.pasardesatanjunguas.adapter.HistoryAdapter;
import com.learning.pasardesatanjunguas.model.HistoryModel;
import com.learning.pasardesatanjunguas.util.AppController;
import com.learning.pasardesatanjunguas.util.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mManager;
    ArrayList<HistoryModel> mItems;
    ProgressDialog pd;

    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mRecyclerView = findViewById(R.id.recyclerHistory);
        pd = new ProgressDialog(HistoryActivity.this);
        mItems = new ArrayList<>();

        loadJson();

        adapter = new HistoryAdapter(mItems);
        mManager = new GridLayoutManager(this, 1);

        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(adapter);
    }

    private void loadJson() {

        pd.setMessage("Mengambil data history...");
        pd.setCancelable(false);
        pd.show();

        StringRequest historyReq = new StringRequest(Request.Method.POST, ServerAPI.URL_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        JSONArray res = null;
                        try {
                            res = new JSONArray(response);
                            for (int i = 0; i < res.length(); i++) {
                                try {
                                    JSONObject data = res.getJSONObject(i);
                                    HistoryModel mh = new HistoryModel();
                                    mh.setNo_nota(data.getString("no_nota"));
                                    mh.setTgl_jual(data.getString("tgl_jual"));
                                    mh.setNama_user(data.getString("nama_kons"));
                                    mh.setTotal_jual(data.getString("total_jual"));
                                    mh.setOngkir(data.getString("ongkir"));
                                    mh.setGrand_total(data.getString("grand_total"));
                                    mh.setStatus(data.getString("status_byr"));
                                    mItems.add(mh);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("History Data", "Berhasil mengambil data history!!");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Toast.makeText(HistoryActivity.this,
                        "Gagal menghubungkan ke server!!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("namaKons", MainActivity.user_name);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(historyReq);
    }
}