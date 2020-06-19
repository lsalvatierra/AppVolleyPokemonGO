package idat.edu.pe.appvolleypokemongo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import idat.edu.pe.appvolleypokemongo.adapters.PokemonAdapter;
import idat.edu.pe.appvolleypokemongo.model.Pokemon;

public class MainActivity extends AppCompatActivity {

    private String urlPokemons = "https://pokeapi.co/api/v2/pokemon/";
    private RecyclerView recyclerViewPokemons;
    private PokemonAdapter adapter;
    private boolean puedeCargar = false;
    private String nextUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**Referencia al Control RecyclerView*/
        recyclerViewPokemons = (RecyclerView) findViewById(R.id.rvpokemon);
        /**Se instancia el Adaptador RecyclerView*/
        adapter = new PokemonAdapter(this);
        /**Se asigna el Adaptador al RecyclerView*/
        recyclerViewPokemons.setAdapter(adapter);
        /**Se asigna el comportamiento del RecyclerView (Contexto, # de Columnas*/
        recyclerViewPokemons.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        recyclerViewPokemons.setHasFixedSize(true);
        /**En **/
        recyclerViewPokemons.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int itemsVisibles = recyclerView.getLayoutManager().getChildCount();
                    int itemsTotales = recyclerView.getLayoutManager().getItemCount();
                    int primerItemVisible = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    //Log.i("TAG2","CARGA: "+ itemsVisibles + " - " + primerItemVisible + " - " + itemsTotales);
                    if (puedeCargar) {
                        if (itemsVisibles + primerItemVisible >= itemsTotales) {
                            Log.i("TAG", "CARGA: " + itemsVisibles + " - " + primerItemVisible + " - " + itemsTotales);
                            puedeCargar = false;
                            getObtenerPokemons(nextUrl);
                        }
                    }
                }
            }
        });

        getObtenerPokemons(urlPokemons);
    }

    public void getObtenerPokemons(String urlActual) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, /**Que Metodo: GET, POST, PUT, etc*/
                urlActual, /**URL del servicio*/
                null, /**Envio de parámetro a traves de un JSON*/
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            nextUrl = response.getString("next");
                            /**Primero buscamos la etiqueta "results" que es de tipo JSONArray*/
                            JSONArray jsonArrayPokemon = response.getJSONArray("results");
                            /**Verificamos si existen valores*/
                            if (jsonArrayPokemon.length() > 0) {
                                ArrayList<Pokemon> miListaPokemon = new ArrayList<>();
                                puedeCargar = true;
                                /**Recorremos cada uno de los elemetos del JSONArray*/
                                for (int i = 0; i < jsonArrayPokemon.length(); i++) {
                                    /**Obtenemos uno a uno cada JSONObject*/
                                    JSONObject jsonPokemon = jsonArrayPokemon.getJSONObject(i);
                                    /**Buscamos las etiquetas "url" y "name"*/
                                    final String url = jsonPokemon.getString("url");
                                    final String nombre = jsonPokemon.getString("name");
                                    /**instanciamos nuestro objeto nuevoPokemon*/
                                    final Pokemon nuevoPokemon = new Pokemon(nombre, url);
                                    /**Llenamos nuestra lista de Pokemons*/
                                    miListaPokemon.add(nuevoPokemon);
                                }
                                adapter.agregarLista(miListaPokemon);
                            }
                        } catch (JSONException je) {
                            puedeCargar = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /**Si ocurre un error*/
                    }
                }
        );
        /**Agregamos la solicitud a la cola.*/
        requestQueue.add(jsonObjectRequest);
    }

}
