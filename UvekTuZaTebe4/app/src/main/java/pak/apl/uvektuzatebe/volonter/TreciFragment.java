package pak.apl.uvektuzatebe.volonter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import pak.apl.uvektuzatebe.korisnik.Kactivity;


public class TreciFragment extends Fragment {


    public static final String BASE_URL3="http://uvektuzatebe.geasoft.net/androidphpmysql/getVolonteri.php";
    private List<Volonteri> volonteriList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    ImageButton logaut, zainf, zasajt , zafejs , zainsta ;
    TextView zdravotext, imatexbodova;

    public TreciFragment() {
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_treci, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = SharedPrefManager.getInstance(getContext()).getUser();
        zasajt = (ImageButton) view.findViewById(R.id.sajt) ;
        zafejs = (ImageButton) view.findViewById(R.id.fejs) ;
        zainsta = (ImageButton) view.findViewById(R.id.insta) ;
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



        zdravotext = view.findViewById(R.id.zdravotext);
        zdravotext.setText("Zdravo "+user.getName());
        imatexbodova= view.findViewById(R.id.textView4);
        imatexbodova.setText("Imate "+user.getBodovi()+" bodova");
        logaut = view.findViewById(R.id.logaut);
        logaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            SharedPrefManager.getInstance(getContext()).logout();
            }
        });
        recyclerView =(RecyclerView) view.findViewById(R.id.risajklzatrazene3);  //trazi u xmlu risajkl
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        volonteriList = new ArrayList<>();    //pravljenje liste pomoci
        getVolonteri();

    }

    private void getVolonteri() {                                   //dobijanje volontera iz baze koji se smestaju u prvih 5 u trecem fragmentu
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray array = new JSONArray(response);
                            int x=1;
                            for(int i=array.length()-1;i>=array.length()-5;i--){
                                JSONObject object = array.getJSONObject(i);
                                String username=String.valueOf(x)+". "+object.getString("username");
                                x++;
                                String bodovi="Bodovi: "+object.getString("bodovi");
                                Volonteri v1 = new Volonteri(username,bodovi);
                                volonteriList.add(v1);
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        mAdapter = new RecyclerAdapter1(getContext(),volonteriList);
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
