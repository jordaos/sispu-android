package com.onewaree.sispu.Gerenciador;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.onewaree.sispu.DAO.PontoDAO;
import com.onewaree.sispu.POJO.Ponto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordao on 12/11/16.
 */

public class ControlePontos {
    private List<Ponto> pontos = new ArrayList<Ponto>();
    PontoDAO pDAO;
    String url;

    public void addPonto(String titulo, String autor, String descricao, LatLng latlng, String data){
        Ponto p = new Ponto();
        p.setTitulo(titulo);
        p.setAutor(autor);
        p.setDescricao(descricao);
        p.setData(data);
        p.setLatLng(latlng);
        pontos.add(p);

        pDAO.addPonto(p);
    }

    public Ponto getPonto(LatLng latlng){
        for(Ponto p : pontos){
            if(p.getLatlng().latitude == latlng.latitude && p.getLatlng().longitude == latlng.longitude)
                return p;
        }
        return null;
    }

    public Ponto getPontoByID(int id){
        for(Ponto p : pontos){
            if(p.getCodigo() == id){
                return p;
            }
        }
        return null;
    }

    public List<Ponto> getPontos(){
        return pontos;
    }

    public ControlePontos(){
        pDAO  = new PontoDAO();
        pontos = pDAO.getListPontos();
    }
}
