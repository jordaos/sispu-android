package com.onewaree.sispu.Classes;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordao on 12/11/16.
 */

public class ControlePontos {
    private List<Ponto> pontos = new ArrayList<Ponto>();

    public void addPonto(String titulo, String autor, String descricao, LatLng latlng, String data){
        pontos.add(new Ponto(titulo, autor, descricao, latlng, data));
    }

    public Ponto getPonto(LatLng latlng){
        for(Ponto p : pontos){
            if(p.getLatlng() == latlng)
                return p;
        }
        return null;
    }

    public List<Ponto> getPontos(){
        return pontos;
    }

    public ControlePontos(){
        pontos.add(new Ponto("P1", "A1", "D1", new LatLng(-4.8, -39.1), "10/11/2016"));
        pontos.add(new Ponto("P2", "A2", "D2", new LatLng(-4.9, -39.06), "11/11/2016"));
        pontos.add(new Ponto("P3", "A3", "D3", new LatLng(-4.8, -39.03528), "12/11/2016"));
    }
}
