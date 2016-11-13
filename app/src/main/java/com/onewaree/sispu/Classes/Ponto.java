package com.onewaree.sispu.Classes;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by jordao on 12/11/16.
 */

public class Ponto {
    private String titulo;
    private String autor;
    private String descricao;
    private LatLng latlng;
    private String data;
    private List<Comentario> comentarios;

    public void addComentario(String autor, String mensagem, String data){
        comentarios.add(new Comentario(autor, mensagem, data));
    }

    public Ponto(String titulo, String autor, String descricao, LatLng latlng, String data){
        this.titulo = titulo;
        this.autor = autor;
        this.descricao = descricao;
        this.latlng = latlng;
        this.data = data;
    }

    public LatLng getLatlng(){
        return this.latlng;
    }

    public String getTitulo(){
        return this.titulo;
    }
}
