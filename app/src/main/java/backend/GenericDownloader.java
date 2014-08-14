package backend;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Patterns;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GenericDownloader extends AsyncTask<Void, Void, JSONObject> {


    public enum HttpType{
        GET,POST
    }

    public enum ResultType{
        SUCCESSFUL,INSUFFICIENT_INPUT_ERROR,HTTP_ERROR,JSON_OBJECT_ERROR
    }

    private String requestUrl;

    private HashMap<String,String> map;

    private HttpType httpType;

    private ResultType resultType;

    private AsyncResponse delegate=null;

    public GenericDownloader(){
        super();
        this.map=new HashMap<String, String>();
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        if(requestUrl!=null && httpType!=null){
            switch (httpType){
                case GET:


                    requestUrl+="?";
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        requestUrl+="&"+key+"="+value;
                    }
                    Log.v("requestUrl", requestUrl);


                    try {
                        URL u = null;
                        u = new URL(requestUrl);
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
                                JSONObject jsonResponse=new JSONObject(sb.toString());
                                this.resultType=ResultType.SUCCESSFUL;
                                return jsonResponse;
                        }

                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                        this.resultType=ResultType.HTTP_ERROR;
                        return null;
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        this.resultType=ResultType.HTTP_ERROR;
                        return null;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        this.resultType=ResultType.JSON_OBJECT_ERROR;
                        return null;

                    }
                    break;
                case POST:

                    break;
            }
        }
        else{
            resultType=ResultType.INSUFFICIENT_INPUT_ERROR;
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        this.delegate.downloadFinish(jsonObject,this.resultType);
    }

    public boolean setRequestUrl(String url){
        if(Patterns.WEB_URL.matcher(url).matches()){
            this.requestUrl=url;
            return true;
        }
        else{
            return false;
        }
    }

    public boolean setParameter(String key, String value){
        if(key!=null && value!=null && !"".equals(key) && !"".equals(value)){
            map.put(key,value);
            return true;
        }
        else {
            return false;
        }

    }

    public void setType(HttpType type){
        httpType=type;
    }

    public void setDelegate(AsyncResponse object){
        this.delegate=object;
    }


    public interface AsyncResponse {
        void downloadFinish(JSONObject jSONResponse,ResultType resultType1);
    }

}
