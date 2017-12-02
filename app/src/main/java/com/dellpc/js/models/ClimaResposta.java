package com.dellpc.js.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell-PC on 02/12/2017.
 */

public class ClimaResposta {
    private Clima results;

    public Clima getResults() {
        return results;
    }

    public void setResults(Clima results) {
        this.results = results;
    }
}
