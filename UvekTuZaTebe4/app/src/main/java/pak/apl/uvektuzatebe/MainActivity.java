package pak.apl.uvektuzatebe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.TextView;
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
import pak.apl.uvektuzatebe.volonter.Vactivity;

public class MainActivity extends AppCompatActivity {

    EditText etName, etPassword;        //box za username i pasword , a ispod za button prijava
    Button prijava;
    ImageButton  zainf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            User user = SharedPrefManager.getInstance(this).getUser();                  //ako je vec prijavljen kad udje u app , prebaci ga na svoj aktiviti
            //  finish();
            if(user.getKor().equals("Volonter")){
                startActivity(new Intent(this, Vactivity.class));}
            else{  startActivity(new Intent(this, Kactivity.class));}
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        zainf= (ImageButton) findViewById(R.id.info);
        zainf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://uvektuzatebe.geasoft.net"));
                startActivity(intent);
            }
        });

        etName = findViewById(R.id.ed_username);                        //trazenje boxeva za username , password , i stavljanje onclick prijava button
        etPassword = findViewById(R.id.ed_password);                        // prijavljivanje u aplikaciju

        prijava=findViewById(R.id.prijava_button);
        prijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        TextView nemar;
        nemar= findViewById(R.id.nema_reg);
        nemar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), Registracija.class));
            }
        });

    }



    private void userLogin() {                  //prijavljivanje korisnika

        final String username = etName.getText().toString();
        final String password = etPassword.getText().toString();
        //provera inputa
        if (TextUtils.isEmpty(username)) {
            etName.setError("Unesite korisnicko ime");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Unesite lozinku");
            etPassword.requestFocus();
            return;
        }

        // ako je ok
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            // konvertovanje u json
                            JSONObject obj = new JSONObject(response);

                            //ako nema erora
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                // dobijanje jednog usera
                                JSONObject userJson = obj.getJSONObject("user");

                                //kreiranje user
                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("username"),
                                        userJson.getString("email"),
                                        userJson.getString("kor"),
                                        userJson.getString("bodovi")
                                );

                                //postavljanje aktivnog usera
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                // Kactivity ili Vactivity vvvv
                                 finish();
                                if(user.getKor().equals("Volonter")){
                                    startActivity(new Intent(MainActivity.this, Vactivity.class));}
                                else{  startActivity(new Intent(MainActivity.this, Kactivity.class));}
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
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError { // slanje parametara kao post request
                Map<String, String> params = new HashMap<>();                   // da bi se korisnik prijavio treba postojati u bazi
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }


}