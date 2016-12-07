package com.onewaree.sispu.POJO;

/**
 * Created by jordao on 12/11/16.
 */

public class Comentario {
    private String autor;
    private String mensagem;
    private String data;
    private int codDemanda;

    public String getAutor(){
        return autor;
    }
    public void setAutor(String autor){
        this.autor = autor;
    }

    public String getMensagem(){
        return mensagem;
    }
    public void setMensagem(String mensagem){
        this.mensagem = mensagem;
    }

    public String getData(){
        return data;
    }
    public void setData(String data){
        this.data = data;
    }

    public int getCodDemanda(){
        return codDemanda;
    }
    public void setCodDemanda(int codDemanda){
        this.codDemanda = codDemanda;
    }
}
