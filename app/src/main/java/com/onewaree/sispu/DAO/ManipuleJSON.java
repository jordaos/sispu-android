package com.onewaree.sispu.DAO;

import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;


/**
 * Created by jordao on 28/11/16.
 */

public class ManipuleJSON {
    //Responsavel por carregar o Objeto JSON
    public JSONObject getJSONFromAPI() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://onewaree.com/JSON/maps.json");

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

        String data = JSON.toString();

        URL url;
        HttpURLConnection urlConnection = null;
        Log.d("SISPUDBG", "SENDJSON");
        try {
            url = new URL("http://onewaree.com/JSON/maps.json");

            urlConnection = (HttpURLConnection) url.openConnection();

            byte[] utf8Bytes = data.getBytes("UTF-8");

            urlConnection.setDoOutput( true );
            urlConnection.setInstanceFollowRedirects( false );
            urlConnection.setRequestMethod( "POST" );
            urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty( "charset", "utf-8");
            urlConnection.setRequestProperty( "Content-Length", Integer.toString(utf8Bytes.length));
            urlConnection.setUseCaches( false );

            Log.d("SISPUDBG", "ola 1");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            Log.d("SISPUDBG", "ola 2");
            wr.write(utf8Bytes);

            Log.d("SISPUDBG", "ola 3");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
