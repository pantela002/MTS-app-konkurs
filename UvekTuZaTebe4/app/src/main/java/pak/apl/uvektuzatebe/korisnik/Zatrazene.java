package pak.apl.uvektuzatebe.korisnik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pak.apl.uvektuzatebe.R;
import pak.apl.uvektuzatebe.klasezaprijavu.SharedPrefManager;
import pak.apl.uvektuzatebe.klasezaprijavu.User;
import pak.apl.uvektuzatebe.volonter.Product;
import pak.apl.uvektuzatebe.volonter.RecyclerAdapter;

public class Zatrazene extends AppCompatActivity {

    public static final String BASE_URL="http://uvektuzatebe.geasoft.net/androidphpmysql/getProducts.php";
    private List<Product> products;                 // lista pomoci
    private RecyclerView.Adapter mAdapter;          //adapter
    private RecyclerView recyclerView;              //recyclerview u kom se prikazuju pomoci
    private int idkor;                              // id korisnika
    private RecyclerView.LayoutManager manager;
    ImageButton logaut12 , nazad;                   // dugmici za logout , i za backpage
    TextView zdravot;                               //textview koji prikazuje poruku " Zdravo korisniku "
    ImageButton zasajt , zafejs , zainsta ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zatrazene);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        zasajt = (ImageButton) findViewById(R.id.sajt1) ;
        zafejs = (ImageButton) findViewById(R.id.fejs1) ;
        zainsta = (ImageButton) findViewById(R.id.insta1) ;
        zainsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://instagram.com/uvektuzavas2021?igshid=1akgegw0vz3s");
                Intent instagram= new Intent(Intent.ACTION_VIEW,uri);
                instagram.setPackage("com.instagram.android");
                try{
                    startActivity(instagram);
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/uvektuzavas2021?igshid=1akgegw0vz3s")));
                }
            }
        });
        zasajt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://uvektuzatebe.geasoft.net/");
                Intent instagram= new Intent(Intent.ACTION_VIEW,uri);
                instagram.setPackage("com.instagram.android");
                try{
                    startActivity(instagram);
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://uvektuzatebe.geasoft.net/")));
                }
            }
        });
        zafejs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/Uvek-Tu-Za-Vas-104867651611929/");
                Intent instagram= new Intent(Intent.ACTION_VIEW,uri);
                instagram.setPackage("com.instagram.android");
                try{
                    startActivity(instagram);
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Uvek-Tu-Za-Vas-104867651611929/")));
                }
            }
        });


        User user = SharedPrefManager.getInstance(this).getUser();
        zdravot = findViewById(R.id.zdravotekst);                                           //Trazenje textviewa i buttona u layoutu gore navedenom
        zdravot.setText("Zdravo "+user.getName());                        //i postavljanje na kliknuto dugme ( ukoliko stisne logout ikonicu)
        nazad = findViewById(R.id.nazadbatn);                                               // izlogovace se , a ako stisne ikonicu za nazad , vraca se
        logaut12 = findViewById(R.id.logaut2);                                              // na prethodan page
        logaut12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(Zatrazene.this).logout();
            }
        });
        nazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Zatrazene.this , Kactivity.class));
            }
        });
        recyclerView =(RecyclerView) findViewById(R.id.risajklzatrazene3);                                               // pravljenje recyclerviewa gde
        LinearLayoutManager layoutManager = new LinearLayoutManager(Zatrazene.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);                                                                    // gde ce se prikazivati zatrazene pomoci
        products = new ArrayList<>();
        getProducts();                                                                                                   // dobijanje pomoci iz baze da bi se
    }                                                                                                                    // smestili u recyclerview

    private void getProducts(){                                                                 //Dobijanje pomoci ( nazvana je Product 
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for(int i=0;i<array.length();i++){
                                JSONObject object = array.getJSONObject(i);
                                int idpomoci=object.getInt("idpomoci");
                                String ime=object.getString("ime");
                                String naslov=object.getString("naslov");
                                String slika=object.getString("slika");
                                String opis=object.getString("opis");
                                String telefon=object.getString("telefon");
                                String adresa=object.getString("adresa");
                                String latitude=object.getString("latitude");
                                String longitude=object.getString("longitude");
                                String idkorisnika=object.getString("idkorisnika");
                                String idvolontera=object.getString("idvolontera");
                                String status=object.getString("status");
                                User user = SharedPrefManager.getInstance(Zatrazene.this).getUser();
                                int intidzer=Integer.parseInt(idkorisnika);
                                idkor = user.getId();
                                if(idkor == intidzer)
                                {
                                    Product product = new Product(idpomoci,ime, naslov,slika,opis,telefon,adresa,latitude,longitude,idkorisnika,idvolontera,status);
                                    products.add(product);                                          //  u listu ubacujemo pomoci
                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        mAdapter = new RecyclerAdapter(Zatrazene.this,products);
                        recyclerView.setAdapter(mAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Zatrazene.this , error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(Zatrazene.this).add(stringRequest);
    }

}