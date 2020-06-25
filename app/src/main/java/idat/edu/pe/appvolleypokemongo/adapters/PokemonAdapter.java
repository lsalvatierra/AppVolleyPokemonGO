package idat.edu.pe.appvolleypokemongo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import idat.edu.pe.appvolleypokemongo.PokemonActivity;
import idat.edu.pe.appvolleypokemongo.R;
import idat.edu.pe.appvolleypokemongo.model.Pokemon;

public class PokemonAdapter
        extends RecyclerView.Adapter<PokemonAdapter.ViewHolder>
{
    private ArrayList<Pokemon> datapokemon;
    private Context context;

    public PokemonAdapter(Context context) {
        this.context = context;
        datapokemon = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pokemon, parent, false);
        return new ViewHolder(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Pokemon pok = datapokemon.get(position);
        holder.tvnompokemon.setText(pok.getNombre());
        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+pok.getId()+".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgpokemon);
        holder.contenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intAndroidDetalle =
                        new Intent(context, PokemonActivity.class);
                intAndroidDetalle.putExtra("pokemon", pok);
                context.startActivity(intAndroidDetalle);
            }
        });
    }

    //Método que agrega información a la lista.
    public void agregarLista(ArrayList<Pokemon> listaPokemon){
        datapokemon.addAll(listaPokemon);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datapokemon.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgpokemon;
        private TextView tvnompokemon;
        private CardView contenedor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgpokemon = (ImageView)itemView.findViewById(R.id.ivpokemon);
            tvnompokemon = (TextView) itemView.findViewById(R.id.tvnompokemon);
            contenedor = (CardView)itemView.findViewById(R.id.contenedor);
        }
    }
}
