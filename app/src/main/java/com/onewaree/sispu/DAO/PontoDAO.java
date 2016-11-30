package com.onewaree.sispu.DAO;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.onewaree.sispu.POJO.Ponto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jordao on 28/11/16.
 */

public class PontoDAO {
    private JSONObject listaPontos_JSON;
    private ManipuleJSON manipuleJSON;

    public PontoDAO(){
        manipuleJSON = new ManipuleJSON();
        listaPontos_JSON = manipuleJSON.getJSONFromAPI();
    }

    public ArrayList<Ponto> getListPontos(){
        ArrayList<Ponto> listaPontos = new ArrayList<Ponto>();

        try{
            JSONArray array = listaPontos_JSON.getJSONArray("pontos");
            for (int i = 0; i < array.length(); i++){
                JSONObject objArray = array.getJSONObject(i);

                Ponto p = new Ponto();
                p.setTitulo(objArray.getString("titulo"));
                p.setAutor(objArray.getString("autor"));
                p.setDescricao(objArray.getString("descricao"));
                p.setData(objArray.getString("data"));
                double lat = Double.parseDouble(objArray.getString("lat"));
                double lng = Double.parseDouble(objArray.getString("lng"));
                LatLng latlng = new LatLng(lat, lng);
                p.setLatLng(latlng);

                listaPontos.add(p);
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }

        return listaPontos;
    }

    public void addPonto(Ponto p){
        JSONObject JSON = parseJSON(p);
        Log.d("SISPUDBG", "passou do json");
        manipuleJSON.sendJSON(JSON);
    }

    private JSONObject parseJSON(Ponto p){
        String data = "{";
            data += "\"ponto\": {";
                data += "\"autor\" : " + p.getAutor() + ", ";
                data += "\"titulo\" : " + p.getTitulo() + ", ";
                data += "\"descricao\" : " + p.getDescricao() + ", ";
                data += "\"lat\" : " + p.getLatlng().latitude + ", ";
                data += "\"lng\" : " + p.getLatlng().longitude + ", ";
                data += "\"data\" : " + p.getData();
            data += "}";
        data += "}";

        JSONObject JSON = null;
        try {
            JSON = new JSONObject(data);
        } catch (JSONException e) {
            Log.d("SISPU", e.toString());
        }
        Log.d("SISPUDBG", data);
        return JSON;
    }
}
