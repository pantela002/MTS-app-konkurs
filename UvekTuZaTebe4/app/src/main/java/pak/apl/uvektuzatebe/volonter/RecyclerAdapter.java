package pak.apl.uvektuzatebe.volonter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import pak.apl.uvektuzatebe.R;
import pak.apl.uvektuzatebe.klasezaprijavu.SharedPrefManager;
import pak.apl.uvektuzatebe.klasezaprijavu.User;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
    private Context mContext;
    private List<Product> products = new ArrayList<>();

    public RecyclerAdapter (Context context,List<Product> products){
        this.mContext = context;
        this.products = products;
    }

    @NotNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {               //trazimo layout kako ce izgledati kolone
        LayoutInflater inflater= LayoutInflater.from(mContext);                                         // i vracamo view
        View view =inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {                  //product je pomoc na poziciji (position)
        Product product = products.get(position);                                               // ostale pobadtke doijamo pomocu holder.___
        holder.naslov.setText(product.getNaslov());
        if(product.getNaslov().equals("Pomoc kod kuce")){

            String slikap ="http://uvektuzatebe.geasoft.net/androidphpmysql/images/pomockodkuce.jpg";
            Glide.with(mContext).load(slikap).into(holder.mImageView);

        }else
        {
            String slikap ="http://uvektuzatebe.geasoft.net/androidphpmysql/images/pomocvankuce.jpg";
            Glide.with(mContext).load(slikap).into(holder.mImageView);
        }
        holder.lokacija.setText(product.getAdresa());
        holder.username.setText(product.getIme());

        holder.mCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,Detaljipomoci.class);
                intent.putExtra("naslov" , product.getNaslov());//
                intent.putExtra("opis", product.getOpis());//
                intent.putExtra("ime", product.getIme());//
                intent.putExtra("idpomoci",String.valueOf(product.getIdpomoci()));//
                intent.putExtra("lokacija",product.getAdresa());//
                intent.putExtra("slika",product.getSlika());//
                intent.putExtra("telefon",product.getTelfon());//
                intent.putExtra("idvolontera", product.getIdvolontera());
                mContext.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return products.size();             // vraca trenutnu vrednost liste te sa pomocima
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView naslov;
        private final TextView lokacija;
        private final TextView username;
        private final ImageView mImageView;
        ConstraintLayout mCont;
        public MyViewHolder (View view){
            super(view);

            naslov = view.findViewById(R.id.naslovprodukta);
            mImageView = view.findViewById(R.id.slikaprodukta);
            username=view.findViewById(R.id.korisnikprodukta);
            lokacija=view.findViewById(R.id.lokacijaprodukta);
            mCont = view.findViewById(R.id.mContainer);
        }
    }
}

