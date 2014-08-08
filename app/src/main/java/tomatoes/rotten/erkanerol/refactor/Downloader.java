package tomatoes.rotten.erkanerol.refactor;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;


public class Downloader extends AsyncTask<String, String, JSONObject> {

    public AsyncResponse delegate=null;
    public int successFlag=MyConstants.DOWNLOAD_FLAG_CORRECT;
    public int total=0;

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject jsonResponse=new JSONObject();
        int responseCode = -1;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        String request=params[0];
        Log.v("request", request);
        HttpGet httpget = new HttpGet(request);

        try {
            HttpResponse response = client.execute(httpget);
            StatusLine statusLine = response.getStatusLine();
            responseCode = statusLine.getStatusCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }

                jsonResponse = new JSONObject(builder.toString());
            }
            else {
                Log.i("error", String.format("Unsuccessful HTTP response code: %d", responseCode));
                successFlag=MyConstants.DOWNLOAD_FLAG_CONNECTION_ERROR;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            successFlag=MyConstants.DOWNLOAD_FLAG_CONNECTION_ERROR;
        }
        catch (Exception e) {
            e.printStackTrace();
            successFlag=MyConstants.DOWNLOAD_FLAG_CONNECTION_ERROR;
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
}
