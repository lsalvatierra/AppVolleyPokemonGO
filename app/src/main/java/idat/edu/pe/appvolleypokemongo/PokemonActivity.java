package idat.edu.pe.appvolleypokemongo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import idat.edu.pe.appvolleypokemongo.model.Pokemon;

public class PokemonActivity extends AppCompatActivity {

    private TextView tvPokemon;
    private ImageView ivPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);
        tvPokemon = findViewById(R.id.tvdetallepokemon);
        ivPokemon = findViewById(R.id.ivdetallepokemon);
        if(getIntent().hasExtra("pokemon")){
            Pokemon objPokemon = getIntent()
                    .getParcelableExtra("pokemon");
            tvPokemon.setText(objPokemon.getNombre());
            Glide.with(getApplicationContext())
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+objPokemon.getId()+".png")
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivPokemon);

        }
    }
}
