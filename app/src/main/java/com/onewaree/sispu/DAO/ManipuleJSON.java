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
import java.net.URLEncoder;

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
            url = new URL("http://onewaree.com/trab_redes/JSONdemandas.php");

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
                    "&titulo=" + URLEncoder.encode(JSON.getString("titulo"), "UTF-8") +
                    "&descricao=" + URLEncoder.encode(JSON.getString("descricao"), "UTF-8") +
                    "&lat=" + URLEncoder.encode(JSON.getString("lat"), "UTF-8") +
                    "&lng=" + URLEncoder.encode(JSON.getString("lng"), "UTF-8") +
                    "&data=" + URLEncoder.encode(JSON.getString("data"), "UTF-8");

            //Create connection
            url = new URL("http://onewaree.com/trab_redes/InsertDemanda.php");
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
            Log.d("Sispu", response.toString());
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
