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
import android.widget.ImageView;
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

public class SignInActivity extends AppCompatActivity {

    public static String userName;
    private EditText usernameET, passwordET;
    private String user, pass;
    private Button signInBtn, cancelBtn;
    private TextView signUpLink;
    private ImageView googleBtn, fcbookBtn;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        signInBtn = findViewById(R.id.signInBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        signUpLink = findViewById(R.id.signUpLink);
        googleBtn = findViewById(R.id.googleBtn);
        fcbookBtn = findViewById(R.id.fcbookBtn);

        pd = new ProgressDialog(SignInActivity.this);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = usernameET.getText().toString();
                pass = passwordET.getText().toString();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(SignInActivity.this,
                            "Username/Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {

                    pd.setMessage("Proses Masuk...");
                    pd.setCancelable(false);
                    pd.show();
                    StringRequest signInReq = new StringRequest(Request.Method.POST,
                            ServerAPI.URL_SIGNIN, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.cancel();
                            try {
                                JSONObject res = new JSONObject(response);
                                if (res.getString("code").equals("1")) {
                                    Toast.makeText(SignInActivity.this,
                                            res.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                    userName = user;
                                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                } else {
                                    Toast.makeText(SignInActivity.this,
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
                            Toast.makeText(SignInActivity.this,
                                    "Gagal menghubungkan ke server!!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("userName", user);
                            map.put("passUser", pass);
                            return map;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(signInReq);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usernameET.setText("");
                passwordET.setText("");
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, GoogleSignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        fcbookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, FacebookAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}