package tomatoes.rotten.erkanerol.refactor;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.util.ArrayList;

import backend.Actors;
import backend.Converter;
import adapters.CastListAdapter;
import backend.Downloader;
import tomatoes.rotten.erkanerol.refactor.MyConstants;
import tomatoes.rotten.erkanerol.refactor.R;

public class CastActivity extends Activity implements Downloader.AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);

        Bundle args=getIntent().getExtras();
        String request=args.getString(MyConstants.CAST_KEY);
        Downloader downloader=new Downloader();
        downloader.setDelegate(this);
        downloader.execute(request+"?"+MyConstants.API_KEY);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.cast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void downloadFinish(JSONObject jSONResponse, int successFlag, int total) {
        ProgressBar progressBar= (ProgressBar) findViewById(R.id.castDownloadProgress);
        progressBar.setVisibility(View.GONE);

        ListView castList=(ListView)findViewById(R.id.castList);
        castList.setVisibility(View.VISIBLE);
        castList.setDivider(null);
        castList.setDividerHeight(0);

        ArrayList<Actors> actors=new ArrayList<Actors>();
        Converter.convertActorsArray(jSONResponse,actors);

        CastListAdapter adapter=new CastListAdapter(this,actors);
        castList.setAdapter(adapter);
    }
}
