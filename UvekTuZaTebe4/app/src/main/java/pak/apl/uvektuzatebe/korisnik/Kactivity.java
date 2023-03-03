package pak.apl.uvektuzatebe.korisnik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pak.apl.uvektuzatebe.R;
import pak.apl.uvektuzatebe.klasezaprijavu.SharedPrefManager;
import pak.apl.uvektuzatebe.klasezaprijavu.User;
import pak.apl.uvektuzatebe.klasezaprijavu.VolleySingleton;
import pak.apl.uvektuzatebe.volonter.Detaljipomoci;
import pak.apl.uvektuzatebe.volonter.Product;
import pak.apl.uvektuzatebe.volonter.RecyclerAdapter;

public class Kactivity extends AppCompatActivity {

    public static final String BASE_URL="http://uvektuzatebe.geasoft.net/androidphpmysql/potrazipomoc.php";
    EditText t1, t2, t3;                                            // textviewi za ime i prezime , broj telefona i kratak opis
    FusedLocationProviderClient fusedLocationProviderClient;
    ImageButton button, logaut, zatrazenapomocb;            //dugmici za slanje zahteva za pomoc , za logout i za drugi activity (zatrazene)
    RadioGroup radioGroupGender;                        // da li je korisnik odabrao pomoc kod kuce ili van kuce


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kactivity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Kactivity.this);

        radioGroupGender = findViewById(R.id.radioOpcije);          // Dobijanje podataka iz layouta
        t1 = findViewById(R.id.imeprezime);                         // Dobijanje podataka iz layouta
        t2 = findViewById(R.id.brojtelefona);                       // Dobijanje podataka iz layouta
        t3 = findViewById(R.id.kratakopis);                         // Dobijanje podataka iz layouta

        zatrazenapomocb = findViewById(R.id.zatrazenapomoc);                                    // Ukoliko klikne ikonicu u donjem desnom uglu
        zatrazenapomocb.setOnClickListener(new View.OnClickListener() {                         // Ulazi u svoje zatrazene pomoci , i one koje nije prijavio
            @Override                                                                           // da su zavrsene
            public void onClick(View view) {

               Intent intent= new Intent(Kactivity.this , Zatrazene.class);       // Prebacivanje na taj drugi aktiviti
               startActivity(intent);

            }
        });

        logaut = findViewById(R.id.logaut);                                                     // Ukoliko stisne logout
        logaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(Kactivity.this).logout();                         //Odjavice se
            }
        });

        button = findViewById(R.id.posaljizahtev);                                          //Ako zatrazi pomoc , prvo izlazi prozorcic koji trazi dozvolu lokacije
        button.setOnClickListener(new View.OnClickListener() {                              //Ukoliko ne prihvati nece moci da zatrazi pomoc , a ako prihvati
            @Override                                                                       //lokacija korisnika ce biti postavljena na mapu
            public void onClick(View view) {                                                //Trazenje lokacije korisnika
                if (ActivityCompat.checkSelfPermission(Kactivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Kactivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(Kactivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
            }
        });
    }



    private void dodaj(String a , String l , String lg){                        //funkcija koja ce informacije o zatrazenoj pomoci staviti u bazu podataka
        final String naslov = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();
        final String ime = t1.getText().toString().trim();
        final String opis = t3.getText().toString().trim();
        final String telefon = t2.getText().toString().trim();
        final String slika;
        User user = SharedPrefManager.getInstance(Kactivity.this).getUser();                //pravimo objekat user u koga smestamo podatke prijavljenog korisnika
        final int idkorisnika = user.getId();
        if(naslov.equals("Pomoc kod kuce")){ slika ="http://uvektuzatebe.geasoft.net/androidphpmysql/images/pomockodkuce.jpg"; }
        else{ slika ="http://uvektuzatebe.geasoft.net/androidphpmysql/images/pomocvankuce.jpg";}

        if (TextUtils.isEmpty(ime)) {
            t1.setError("Unesite korisnicko ime");
            t1.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(telefon)) {
            t2.setError("Unesite lozinku");
            t2.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(opis)) {
            t3.setError("Unesite lozinku");
            t3.requestFocus();
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,                    // slanje requesta
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finish();
                        startActivity(new Intent(Kactivity.this,Zatrazene.class));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Kactivity.this, error.toString(),Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override                                                                           //Prosledjujemo podatke koje zelimo da smestimo u bazu podataka
            protected Map<String, String> getParams() throws AuthFailureError {                 //Podaci se prijavju preko POST requesta , i sa njima se dalje radi
                Map<String, String> params = new HashMap<>();                                   // phpu
                params.put("ime",ime);
                params.put("naslov",naslov);
                params.put("opis", opis);
                params.put("slika", slika);
                params.put("telefon", telefon);
                params.put("adresa", a);
                params.put("latitude", l);
                params.put("longitude", lg);
                params.put("idkorisnika", String.valueOf(idkorisnika));
                params.put("status", "0");
                params.put("idvolontera","0");
                return params;
            }
        };
        VolleySingleton.getInstance(Kactivity.this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==100 && grantResults.length>0 && (grantResults[0]+grantResults[1]
        ==PackageManager.PERMISSION_GRANTED)){                                      //Ukoliko korisnik dozvoli koriscenje lokacije , startuje se funkcija
            getLocation();                                                          // getLocation() koja trazi lokaciju
        }else{
            Toast.makeText(getApplicationContext(), "Odbijen pristup lokaciji", Toast.LENGTH_SHORT).show();         //ukoliko odbije nece moci poslati zahtev
        }                                                                                                                //za pomoc
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {                                                                                                //Trazi lokaciju korisnika
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ){
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    private String latpomoci,lngpomoci,adresapomoci;
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if(location!=null){
                            try {
                                Geocoder geocoder;
                                latpomoci=String.valueOf(location.getLatitude());
                                lngpomoci=String.valueOf(location.getLongitude());
                                List<Address> addresses;
                                geocoder = new Geocoder(Kactivity.this, Locale.getDefault());
                                addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                                adresapomoci= addresses.get(0).getAddressLine(0);
                                User user = SharedPrefManager.getInstance(Kactivity.this).getUser();
                                    dodaj(adresapomoci, latpomoci, lngpomoci);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                      else{
                            LocationRequest locationRequest = new LocationRequest()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(10000)
                                    .setFastestInterval(1000)
                                    .setNumUpdates(1);
                                LocationCallback locationCallback = new LocationCallback(){


                                  public void OnLocationResult(LocationResult locationResult) {
                                      try {
                                          Location location1= locationResult.getLastLocation();
                                          Geocoder geocoder;
                                          latpomoci=String.valueOf(location1.getLatitude());
                                          lngpomoci=String.valueOf(location1.getLongitude());
                                          List<Address> addresses;
                                          geocoder = new Geocoder(Kactivity.this, Locale.getDefault());
                                          addresses = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                                          adresapomoci = addresses.get(0).getAddressLine(0);
                                          User user = SharedPrefManager.getInstance(Kactivity.this).getUser();
                                              dodaj(adresapomoci, latpomoci, lngpomoci);
                                            }
                                        catch (IOException e) {
                                                  e.printStackTrace();
                                             }
                                          }
                            };
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest
                            ,locationCallback, Looper.myLooper());
                        }
                    }
                });
        }
        else{
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


}