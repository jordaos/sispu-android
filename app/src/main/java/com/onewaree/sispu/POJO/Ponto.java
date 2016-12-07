package com.onewaree.sispu.POJO;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordao on 12/11/16.
 */

public class Ponto {
    private int codigo;
    private String titulo;
    private String autor;
    private String descricao;
    private LatLng latlng;
    private String data;
    private ArrayList<Comentario> comentarios;

    public Ponto(){
        comentarios = new ArrayList<Comentario>();
    }

    public void addComentario(Comentario c){
        comentarios.add(c);
    }

    public void setCodigo(int codigo){this.codigo = codigo;}
    public int getCodigo(){return codigo;}

    public LatLng getLatlng(){
        return this.latlng;
    }

    public String getTitulo(){
        return this.titulo;
    }

    public String getAutor(){
        return this.autor;
    }

    public String getDescricao(){
        return this.descricao;
    }

    public String getData(){
        return this.data;
    }

    public List<Comentario> getComentarios(){
        return this.comentarios;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public void setAutor(String autor){
        this.autor = autor;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public void setData(String data){
        this.data = data;
    }

    public void setLatLng(LatLng latlng){
        this.latlng = latlng;
    }

}
