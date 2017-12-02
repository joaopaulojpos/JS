package com.dellpc.js;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dellpc.js.apiclima.ClimaapiService;
import com.dellpc.js.models.Clima;
import com.dellpc.js.models.ClimaResposta;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CLIMEX";

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.hgbrasil.com/weather/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        obterDados();
    }

    private void obterDados(){
        ClimaapiService service = retrofit.create(ClimaapiService.class);
        Call<ClimaResposta> climaRespostaCall = service.obterListaClimas();

        climaRespostaCall.enqueue(new Callback<ClimaResposta>() {
            @Override
            public void onResponse(Call<ClimaResposta> call, Response<ClimaResposta> response) {
                if (response.isSuccessful()){

                    ClimaResposta climaResposta = response.body();
                    Clima listaClima = climaResposta.getResults();

                    Log.i(TAG, " Clima " + listaClima.getDescription());
                    Log.i(TAG, " Data " + listaClima.getDate());

                    /**
                    for (int i = 0; i < listaClima.size(); i++){
                        Clima c = listaClima.get(i);
                        Log.i(TAG, " Clima: " + c.getDescription());
                    }
                     */

                }else {
                    Log.e(TAG," onResponse " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ClimaResposta> call, Throwable t) {
                Log.e(TAG, " onFailure " + t.getMessage());
            }
        });
    }

}
