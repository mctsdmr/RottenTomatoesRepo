package backend;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import tomatoes.rotten.erkanerol.refactor.MyConstants;


public class Downloader extends AsyncTask<String, String, JSONObject> {

    private AsyncResponse delegate=null;
    public int successFlag= MyConstants.DOWNLOAD_FLAG_CORRECT;
    public int total=0;

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject jsonResponse=new JSONObject();
        String requestUrl=params[0];

        try {
            URL u = new URL(requestUrl);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    jsonResponse=new JSONObject(sb.toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if( !"".equals(jsonResponse.optString("error"))){
            successFlag=MyConstants.DOWNLOAD_FLAG_API_ERROR;
        }

        if( jsonResponse.optInt("total")!=0){
            total=jsonResponse.optInt("total");
        }

        return jsonResponse;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        delegate.downloadFinish(jsonObject,successFlag,total);
    }

    public interface AsyncResponse {
        void downloadFinish(JSONObject jSONResponse,int successFlag,int total);
    }

    public void setDelegate(AsyncResponse object){
        this.delegate=object;
    }

}
