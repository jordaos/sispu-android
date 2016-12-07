package com.onewaree.sispu.DAO;

import android.os.StrictMode;
import android.util.Log;


import com.onewaree.sispu.POJO.Comentario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by jordao on 28/11/16.
 */

public class ComentarioDAO {
    private JSONObject listaComentarios_JSON;

    public ComentarioDAO(){
        listaComentarios_JSON = getJSONFromAPI();
    }

    public ArrayList<Comentario> getListComentarios(){
        ArrayList<Comentario> listaComentarios = new ArrayList<Comentario>();

        try{
            JSONArray array = listaComentarios_JSON.getJSONArray("comentarios");
            for (int i = 0; i < array.length(); i++){
                JSONObject objArray = array.getJSONObject(i);

                Comentario c = new Comentario();
                c.setMensagem(objArray.getString("mensagem"));
                c.setAutor(objArray.getString("autor"));
                c.setData(objArray.getString("data"));
                c.setCodDemanda(objArray.getInt("codDemanda"));

                listaComentarios.add(c);
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }

        return listaComentarios;
    }

    public void addComentario(Comentario c){
        JSONObject JSON = parseJSON(c);
        sendJSON(JSON);
    }

    private JSONObject parseJSON(Comentario c){
        String data = "{";
        data += "\"autor\" : \"" + c.getAutor() + "\", ";
        data += "\"mensagem\" : \"" + c.getMensagem() + "\", ";
        data += "\"data\" : \"" + c.getData() + "\", ";
        data += "\"codDemanda\" : \"" + c.getCodDemanda() + "\" ";
        data += "}";
        Log.d("SISPU_COM", data);

        JSONObject JSON = null;
        try {
            JSON = new JSONObject(data);
        } catch (JSONException e) {
            Log.d("SISPU", e.toString());
        }
        return JSON;
    }


    public JSONObject getJSONFromAPI() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://onewaree.com/trab_redes/JSONcomentarios.php");

            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            //InputStreamReader isw = new InputStreamReader(in);
            String data = converterInputStreamToString(in);
            JSONObject obj = new JSONObject(data);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable t) {
            Log.e("SISPU", "Não foi possível baixar o JSON");
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }
    private String converterInputStreamToString(InputStream is){
        StringBuffer buffer = new StringBuffer();
        try{
            BufferedReader br;
            String linha;

            br = new BufferedReader(new InputStreamReader(is));
            while((linha = br.readLine())!=null){
                buffer.append(linha);
            }

            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return buffer.toString();
    }
    public void sendJSON(JSONObject JSON){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url;
        HttpURLConnection connection = null;
        try {
            String urlParameters = "autor=" + URLEncoder.encode(JSON.getString("autor"), "UTF-8") +
                    "&codDemanda=" + URLEncoder.encode(JSON.getString("codDemanda"), "UTF-8") +
                    "&mensagem=" + URLEncoder.encode(JSON.getString("mensagem"), "UTF-8") +
                    "&data=" + URLEncoder.encode(JSON.getString("data"), "UTF-8");
            Log.d("SISPU_COM", urlParameters);
            //Create connection
            url = new URL("http://onewaree.com/trab_redes/InsertComentario.php");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes(urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

        } catch (Exception e) {
            Log.d("SISPU_EXPT", e.toString());
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
