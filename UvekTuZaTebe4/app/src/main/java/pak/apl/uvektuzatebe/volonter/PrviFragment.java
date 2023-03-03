package pak.apl.uvektuzatebe.volonter;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pak.apl.uvektuzatebe.R;


public class PrviFragment extends Fragment {

    List<Product> products = new ArrayList<>();
    private SupportMapFragment mMapFragment;
    private String latitudevolontera , longitudevolontera;
    private BroadcastReceiver mBroadcastReceiver;
    private GoogleMap mMap;

    public PrviFragment() {
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prvi, container, false);

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if(getActivity() != null)
            ((Vactivity)getActivity()).getLocation();
        mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        getProducts();
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null) {

                        String latpomoci = String.valueOf(location.getLatitude());
                        String lngpomoci = String.valueOf(location.getLongitude());
                        LatLng coordinate = new LatLng(Float.parseFloat(latpomoci), Float.parseFloat(lngpomoci)); //Store these lat lng values somewhere. These should be constant.
                        CameraUpdate l = CameraUpdateFactory.newLatLngZoom(
                                coordinate, 16);
                        if(mMap != null) mMap.animateCamera(l);
                    }

                }
            });
        }
    }

    public static final String BASE_URL = "http://uvektuzatebe.geasoft.net/androidphpmysql/getProducts.php";
    private void getProducts() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
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
                                if (status.equals("0")) {
                                    Product product = new Product(idpomoci, ime, naslov, slika, opis, telefon, adresa, latitude, longitude, idkorisnika, idvolontera, status);
                                    products.add(product);
                                }
                            }
                            mMapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
                                    boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                                            .getString(R.string.style_json)));
                                    mMap = googleMap;

                                    for (int i = 0; i < products.size(); i++) {
                                        float latitude = Float.parseFloat(products.get(i).getLatitude());
                                        float longitude = Float.parseFloat(products.get(i).getLongitude());
                                        LatLng mark = new LatLng(latitude, longitude);
                                        Marker m = googleMap.addMarker(new MarkerOptions()
                                                .position(mark)
                                                .title("Kliknut marker"));
                                        m.setTag(products.get(i).getIdpomoci());
                                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(mark));
                                    }
                                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker marker) {
                                            kliknutMarker((Integer) marker.getTag());
                                            return false;
                                        }
                                    });
                                    getLocation();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
    void kliknutMarker(int productId) {
        Intent intent = new Intent(getActivity(), Detaljipomoci.class);
        intent.putExtra("pozvanIzMape", true);
        String ajdi=String.valueOf(productId);
        intent.putExtra("id", ajdi);
        startActivity(intent);
    }
}