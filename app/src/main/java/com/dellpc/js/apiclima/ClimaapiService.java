package com.dellpc.js.apiclima;

import com.dellpc.js.models.ClimaResposta;

import java.util.List;

import retrofit2.http.GET;

/**
 * Created by Dell-PC on 02/12/2017.
 */

public interface ClimaapiService{
    @GET("?format=json&woeid=455824")
    retrofit2.Call<ClimaResposta> obterListaClimas();
}
