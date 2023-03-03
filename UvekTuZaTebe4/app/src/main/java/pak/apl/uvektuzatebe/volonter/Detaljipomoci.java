package pak.apl.uvektuzatebe.volonter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pak.apl.uvektuzatebe.R;
import pak.apl.uvektuzatebe.klasezaprijavu.SharedPrefManager;
import pak.apl.uvektuzatebe.klasezaprijavu.User;
import pak.apl.uvektuzatebe.klasezaprijavu.VolleySingleton;
import pak.apl.uvektuzatebe.korisnik.Kactivity;
import pak.apl.uvektuzatebe.korisnik.Zatrazene;

import static android.view.View.GONE;

public class Detaljipomoci extends AppCompatActivity {
    public static final String BASE_URL1="http://uvektuzatebe.geasoft.net/androidphpmysql/updateaj.php";
    public static final String BASE_URL2="http://uvektuzatebe.geasoft.net/androidphpmysql/dodajbod.php";
    public static final String BASE_URL3="http://uvektuzatebe.geasoft.net/androidphpmysql/izbrisi.php";
    public static final String BASE_URL4="http://uvektuzatebe.geasoft.net/androidphpmysql/getProducts.php";
    public static final String BASE_URL5="http://uvektuzatebe.geasoft.net/androidphpmysql/smanjibod.php";
    ImageButton nazadbatn;                  // dugme za pageback
    Button prihvati,otkazi,zavrsi;          //dugmici za prihvati(volonter ) , otkazi ( volonter ) i zavrsi(korisnik)
    ImageView slika99;                      //imageview za sliku ( kod kuce ili van kuce )
    TextView  naslov99, ime99 , telefon99, lokacija99 , opis99;
    String idpomoci, idvolontera;
    Product product ;           // trenutno zatrazena pomoc na layoutu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaljipomoci);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        slika99=findViewById(R.id.slika99);                 //trazenje svih boxova u layoutu detajlipomoci
        naslov99=findViewById(R.id.naslov99);
        ime99=findViewById(R.id.ime99);
        telefon99=findViewById(R.id.telefon99);
        lokacija99=findViewById(R.id.lokacija99);
        opis99=findViewById(R.id.opis99);
        otkazi=findViewById(R.id.otkazi99);
        zavrsi=findViewById(R.id.zavrsi99);
        prihvati=findViewById(R.id.prihvati99);
        nazadbatn = findViewById(R.id.nazadbatn99);

        nazadbatn.setOnClickListener(new View.OnClickListener() {               //vracanje na prethodni page
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(getIntent().getBooleanExtra("pozvanIzMape", false)) {            //u ovoj if naredbi gledamo
            idpomoci=getIntent().getStringExtra("id");                                  // da li je detaljipomoci pozvana iz mape
            getpomoc(idpomoci);                                                                // i tada je volonteru dostupno dugme prihvati
            User user = SharedPrefManager.getInstance(Detaljipomoci.this).getUser();
            otkazi.setVisibility(GONE);
            zavrsi.setVisibility(GONE);
            prihvati.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateaj(idpomoci , "1", String.valueOf(user.getId()));
                    startActivity(new Intent(Detaljipomoci.this , Vactivity.class));
                }
            });
        }
        else                                                    // da li je pozvan preko recyclerviewa ( korislik ili volonter )
        {                                                       // vidimo preko idusera , i onda postavimo dugme koje treba
            idvolontera=getIntent().getStringExtra("idvolontera");
            idpomoci=getIntent().getStringExtra("idpomoci");
            naslov99.setText(getIntent().getStringExtra("naslov"));
            if(getIntent().getStringExtra("naslov").equals("Pomoc kod kuce"))
            {
                String slika ="http://uvektuzatebe.geasoft.net/androidphpmysql/images/pomockodkuce.jpg";
                Glide.with(Detaljipomoci.this).load(slika).into(slika99);
            }
            else
            {
                String slika ="http://uvektuzatebe.geasoft.net/androidphpmysql/images/pomocvankuce.jpg";
                Glide.with(Detaljipomoci.this).load(slika).into(slika99);
            }

            ime99.setText(getIntent().getStringExtra("ime"));
            telefon99.setText(getIntent().getStringExtra("telefon"));
            lokacija99.setText(getIntent().getStringExtra("lokacija"));
            opis99.setText(getIntent().getStringExtra("opis"));
            User user = SharedPrefManager.getInstance(Detaljipomoci.this).getUser();

            if(user.getKor().equals("Korisnik")){
                    otkazi.setVisibility(GONE);                                                     // ako je korisnik i ako stisne zavrsi
                    prihvati.setVisibility(GONE);                                                   //volonteru se dodaje bod
                    zavrsi.setOnClickListener(new View.OnClickListener() {                          // i pomoc se brize iz baze podataka
                        @Override
                        public void onClick(View v) {
                             dodajbod(idvolontera);
                            izbrisi(idpomoci);
                        }
                    });
            }
            else
            {
                zavrsi.setVisibility(GONE);
                prihvati.setVisibility(GONE);
                otkazi.setOnClickListener(new View.OnClickListener() {
                    @Override                                                           //ako volonter stisne otkazi
                    public void onClick(View v) {                                       //iz nekog razloga je odustao i pomoc se se opet prikazati
                        updateaj(idpomoci,"0","0");                     // na mapi i neko ce moci da prihvati
                        startActivity(new Intent(Detaljipomoci.this , Vactivity.class));
                    }
                });
            }

        }
    }

    private void getpomoc(String ajdpomoci) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int idpomoci = object.getInt("idpomoci");
                                String ime = object.getString("ime");
                                String naslov = object.getString("naslov");
                                String slika = object.getString("slika");
                                String opis = object.getString("opis");
                                String telefon = object.getString("telefon");
                                String adresa = object.getString("adresa");
                                String latitude = object.getString("latitude");
                                String longitude = object.getString("longitude");
                                String idkorisnika = object.getString("idkorisnika");
                                String idvolontera = object.getString("idvolontera");
                                String status = object.getString("status");
                                User user = SharedPrefManager.getInstance(Detaljipomoci.this).getUser();
                                int intidzer = Integer.parseInt(idvolontera);
                                if (ajdpomoci.equals(String.valueOf(idpomoci))) {
                                    product = new Product(idpomoci, ime, naslov, slika, opis, telefon, adresa, latitude, longitude, idkorisnika, idvolontera, status);
                                    Glide.with(Detaljipomoci.this).load(product.getSlika()).into(slika99);
                                    naslov99.setText(product.getNaslov());
                                    ime99.setText(product.getIme());
                                    telefon99.setText(product.getTelfon());
                                    lokacija99.setText(product.getAdresa());
                                     opis99.setText(product.getOpis());
                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( Detaljipomoci.this, error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(Detaljipomoci.this).add(stringRequest);
    }


    private void updateaj(String idpomoci , String status , String idvolontera){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL1,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        finish();
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Detaljipomoci.this, error.toString(),Toast.LENGTH_SHORT).show();
            }}) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idpomoci",idpomoci);
                params.put("status", status);
                params.put("idvolontera",idvolontera);
                return params;
            }};
        VolleySingleton.getInstance(Detaljipomoci.this).addToRequestQueue(stringRequest);
    }


    private void izbrisi(String idpomoci) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL3,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        finish();
                        startActivity(new Intent(Detaljipomoci.this,Kactivity.class));
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Detaljipomoci.this, error.toString(),Toast.LENGTH_SHORT).show();
            }}) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idpomoci",idpomoci);
                return params;
            }};
        VolleySingleton.getInstance(Detaljipomoci.this).addToRequestQueue(stringRequest);
    }


    private void dodajbod(String idvolontera) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL2,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                       // finish();
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Detaljipomoci.this, error.toString(),Toast.LENGTH_SHORT).show();
            }}) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",idvolontera);
                return params;
            }};
        VolleySingleton.getInstance(Detaljipomoci.this).addToRequestQueue(stringRequest);

    }


}