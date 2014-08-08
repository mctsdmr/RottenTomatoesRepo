package tomatoes.rotten.erkanerol.refactor;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import BackEnd.Container;

public class SearchMovieActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.rotten_green)));
        Intent intent=getIntent();
        Bundle extras=intent.getExtras();

        MovieListFragment fragment=new MovieListFragment();
        Bundle args = new Bundle();

        int searchType=extras.getInt(MyConstants.SEARCH_TYPE);

        if(searchType==MyConstants.SEARCH_TYPE_MOVIE){
            getActionBar().setTitle(getResources().getString(R.string.search_title));
            args.putInt(MyConstants.LIST_FRAGMENT_TYPE, MyConstants.FRAGGMENT_TYPE_SEARCH);
            args.putString(MyConstants.SEARCH_KEY, extras.getString(MyConstants.SEARCH_KEY));
        }
        else if(searchType==MyConstants.SEARCH_TYPE_SIMILAR){
            args.putInt(MyConstants.LIST_FRAGMENT_TYPE,MyConstants.FRAGGMENT_TYPE_SIMILAR);
            args.putString(MyConstants.SEARCH_SIMILAR_REQUEST,extras.getString(MyConstants.SEARCH_SIMILAR_REQUEST));
            getActionBar().setTitle(getResources().getString(R.string.similar));
        }
        else{
            getActionBar().setTitle(getResources().getString(R.string.favorites));
            args.putInt(MyConstants.LIST_FRAGMENT_TYPE,MyConstants.FRAGGMENT_TYPE_FAVORITES);
        }
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.search_fragment,fragment,"fragment").commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
