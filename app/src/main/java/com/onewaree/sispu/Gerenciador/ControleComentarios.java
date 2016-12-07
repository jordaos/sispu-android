package com.onewaree.sispu.Gerenciador;

import android.util.Log;

import com.onewaree.sispu.DAO.ComentarioDAO;
import com.onewaree.sispu.POJO.Comentario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordao on 12/11/16.
 */

public class ControleComentarios {
    private List<Comentario> comenatarios = new ArrayList<Comentario>();
    ComentarioDAO cDAO;
    String url;

    public void addComentario(Comentario c){
        cDAO.addComentario(c);
    }


    public List<Comentario> getComentarios(){
        return comenatarios;
    }

    public ControleComentarios(){
        cDAO = new ComentarioDAO();
        comenatarios = cDAO.getListComentarios();
    }
}
