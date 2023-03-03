package pak.apl.uvektuzatebe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pak.apl.uvektuzatebe.klasezaprijavu.SharedPrefManager;
import pak.apl.uvektuzatebe.klasezaprijavu.URLs;
import pak.apl.uvektuzatebe.klasezaprijavu.User;
import pak.apl.uvektuzatebe.klasezaprijavu.VolleySingleton;
import pak.apl.uvektuzatebe.korisnik.Kactivity;

public class Registracija extends AppCompatActivity {

    EditText editTextUsername, editTextEmail, editTextPassword , editTextPassword2 ;
    RadioGroup radioGroupGender;                            //sve boxevi za registraciju , email , username ....
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);
        progressBar = findViewById(R.id.progressBar);


        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        editTextUsername = findViewById(R.id.ed_username1);             //trazenje boxeva u layoutu
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.ed_password1);
        editTextPassword2 = findViewById(R.id.ed_password2);
        radioGroupGender = findViewById(R.id.radioOpcije);


        Button zareg= findViewById(R.id.Registracija_button);
      zareg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();                                         //ako klikne registrujte se , zapocinje proces registracije
            }
        });

        findViewById(R.id.ima_log).setOnClickListener(new View.OnClickListener() {                      //ako vec ima , ide na mainativity
            @Override
            public void onClick(View view) {
                //Ako stisne da vec ima acc
                finish();
                startActivity(new Intent(Registracija.this, MainActivity.class));
            }
        });

    }




    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String password2 = editTextPassword2.getText().toString().trim();
        final String bod="0";

        final String kor = "Korisnik";

        if (TextUtils.isEmpty(email)) {                                         //provere unosa podataka
            editTextEmail.setError("Unesite email");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Unesite ispravnu email adresu");
            editTextEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Unesite korisnicko ime");
            editTextUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Unesite lozinku");
            editTextPassword.requestFocus();
            return;
        }
        if(!password.equals(password2))
        {
            editTextPassword.setError("Lozinke se ne poklapaju");
            editTextPassword.requestFocus();
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {

                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                JSONObject userJson = obj.getJSONObject("user");


                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("username"),
                                        userJson.getString("email"),
                                        userJson.getString("kor"),
                                        userJson.getString("bodovi")
                                );


                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);


                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();               //slanje podataka i smestanje u bazu podataka ukoliko nije prijavljen
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("kor", kor);
                params.put("bodovi", bod);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }




}