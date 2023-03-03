package pak.apl.uvektuzatebe.volonter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class DrugiFragment extends Fragment {


    public static final String BASE_URL="http://uvektuzatebe.geasoft.net/androidphpmysql/getProducts.php";
    private List<Product> products;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;

    public DrugiFragment() {
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drugi, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView =(RecyclerView) view.findViewById(R.id.risajklzatrazene3);      //trazi u xmlu risajkl
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        products = new ArrayList<>();       //pravljenje liste u koju cemo smestati dobijene pomoci iz baze
        getProducts();
    }


    private void getProducts(){                                             // get request za dobijanje pomoci zatraznih , iz baze podataka
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,       // i smestanje u recyclerview
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
                                User user = SharedPrefManager.getInstance(getContext()).getUser();
                                int intidzer=Integer.parseInt(idvolontera);
                                if(user.getId() ==intidzer)
                                {
                                    Product product = new Product(idpomoci,ime, naslov,slika,opis,telefon,adresa,latitude,longitude,idkorisnika,idvolontera,status);
                                    products.add(product);
                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        mAdapter = new RecyclerAdapter(getContext(),products);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( getContext(), error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

}