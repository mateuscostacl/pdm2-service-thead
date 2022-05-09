package com.example.pdm2_service_thead;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.pdm2_service_thead.services.HttpHandler;
import com.example.pdm2_service_thead.services.HttpService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String pokeUrl = "https://pokeapi.co/api/v2/pokemon/";

    ImageView ivEsquerda;
    ImageView ivCentro;
    ImageView ivDireita;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conectarComViewport();
    }

    public void conectarComViewport(){
        ivEsquerda = findViewById(R.id.iv_esq);
        ivCentro = findViewById(R.id.iv_centro);
        ivDireita = findViewById(R.id.iv_dir);
    }

    public void clicar(View v) {
//        Intent i = new Intent(this, Service.class);
//        startService(i);
        Log.i("Main", String.valueOf(Thread.currentThread()));

        handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Post", String.valueOf(Thread.currentThread()));

                        String[] pokes = capturarPokemons();
                        Log.e("POKES", pokes[0]);
                        String[] pokeUrls = pegarUrlDasImagens(pokes);
                        Log.e("POKEURLS", pokeUrls[0]);

                        Picasso.get().load(pokeUrls[0]).resize(250, 250).into(ivEsquerda);
                        Picasso.get().load(pokeUrls[1]).resize(250, 250).into(ivCentro);
                        Picasso.get().load(pokeUrls[2]).resize(250, 250).into(ivDireita);
                    }
                });
            }
        }).start();
    }

    private String[] capturarPokemons() {
        String[] pokemonUrl = new String[]{"","",""};

        for (int i = 0; i < 3; i++) {
            pokemonUrl[i] = pokeUrl + Util.random(898);
        }

        return pokemonUrl;
    }

    private String[] pegarUrlDasImagens(String[] pokemonUrl){
        HttpService httpService = new HttpService();
        String pokemon;
        String[] pokeImages = new String[]{"","",""};
        for (int i = 0; i < 3; i++) {
            pokemon = httpService.getResposta(pokemonUrl[i]);

            try {
                JSONObject joPokemon = new JSONObject(pokemon);
                pokeImages[i] = joPokemon.getJSONObject("sprites").getString("front_default");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return pokeImages;
    }

//    private class MyHandler extends Handler {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//
//
//        }
//    }
}