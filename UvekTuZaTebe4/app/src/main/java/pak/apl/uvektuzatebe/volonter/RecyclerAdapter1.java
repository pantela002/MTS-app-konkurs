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

public class RecyclerAdapter1 extends RecyclerView.Adapter<RecyclerAdapter1.MyViewHolder>{
    private Context mContext;
    private List<Volonteri> volonteriList = new ArrayList<>();                                              //SVE ISTO KAO U RECYCLERADAPTER
                                                                                                            //OVAJ KORISTIM ZA LADERBOARD
    public RecyclerAdapter1 (Context context,List<Volonteri> volonteriList){
        this.mContext = context;
        this.volonteriList = volonteriList;
    }

    @NotNull
    public RecyclerAdapter1.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(mContext);
        View view =inflater.inflate(R.layout.my_row1,parent,false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Volonteri volonteri = volonteriList.get(position);
        holder.imenalisti.setText(volonteri.getUsername());
        holder.bodovinalisti.setText(volonteri.getBodovi());

    }

    public int getItemCount() {
        return volonteriList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView imenalisti;
        private final TextView bodovinalisti;

        ConstraintLayout mCont;
        public MyViewHolder (View view){
            super(view);
            imenalisti = view.findViewById(R.id.imenalisti);
            bodovinalisti = view.findViewById(R.id.bodovinalisti);
        }
    }
}
