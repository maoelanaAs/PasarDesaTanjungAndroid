package com.learning.pasardesatanjunguas.activity;

/**
 * Dibuat oleh Maulana As'an, 10/01/23
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.learning.pasardesatanjunguas.MainActivity;
import com.learning.pasardesatanjunguas.R;
import com.learning.pasardesatanjunguas.util.AppController;
import com.learning.pasardesatanjunguas.util.ServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameuserET, usernameET, passwordET;
    private String newName, newUser, newPass;
    private Button signUpBtn, cancelBtn;
    private TextView signInLink;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        nameuserET = findViewById(R.id.nameuserET);
        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        signUpBtn = findViewById(R.id.signUpBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        signInLink = findViewById(R.id.signInLink);

        pd = new ProgressDialog(SignUpActivity.this);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newName = nameuserET.getText().toString();
                newUser = usernameET.getText().toString();
                newPass = passwordET.getText().toString();

                pd.setMessage("Proses Daftar...");
                pd.setCancelable(false);
                pd.show();
                StringRequest signUpReq = new StringRequest(Request.Method.POST,
                        ServerAPI.URL_SIGNUP, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString("code").equals("1")) {
                                Toast.makeText(SignUpActivity.this,
                                        res.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                            } else {
                                Toast.makeText(SignUpActivity.this,
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
                        Toast.makeText(SignUpActivity.this,
                                "Gagal menghubungkan ke server!!",
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("userName", newUser);
                        map.put("namaUser", newName);
                        map.put("passUser", newPass);
                        map.put("roleUser", "Konsumen");
                        return map;
                    }
                };
                AppController.getInstance().addToRequestQueue(signUpReq);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameuserET.setText("");
                usernameET.setText("");
                passwordET.setText("");
            }
        });


        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}