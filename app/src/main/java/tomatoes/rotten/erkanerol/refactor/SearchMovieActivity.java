package tomatoes.rotten.erkanerol.refactor;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import fragments.MovieListFragment;
import util.Connections;

public class SearchMovieActivity extends FragmentActivity {

    public int searchType=MyConstants.SEARCH_TYPE_NOSEARCH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_movie);
        Connections.isNetworkConnected(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        MovieListFragment fragment=new MovieListFragment();
        Bundle args = new Bundle();

        searchType=extras.getInt(MyConstants.SEARCH_TYPE);

        if(searchType==MyConstants.SEARCH_TYPE_NOSEARCH){
            getActionBar().setTitle(getResources().getString(R.string.search_title));
            return;
        }

        if(searchType==MyConstants.SEARCH_TYPE_MOVIE){

            getActionBar().setTitle(getResources().getString(R.string.search_title));

            args.putInt(MyConstants.LIST_FRAGMENT_TYPE, MyConstants.FRAGGMENT_TYPE_SEARCH);
            args.putString(MyConstants.SEARCH_KEY, extras.getString(MyConstants.SEARCH_KEY));

        } else if (searchType == MyConstants.SEARCH_TYPE_FAVORITES) {
            getActionBar().setTitle(getResources().getString(R.string.favorites));
            args.putInt(MyConstants.LIST_FRAGMENT_TYPE,MyConstants.FRAGGMENT_TYPE_FAVORITES);
        }
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.search_fragment,fragment,"fragment").commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(searchType!=MyConstants.SEARCH_TYPE_FAVORITES){
            getMenuInflater().inflate(R.menu.search_movie, menu);
            SearchManager searchManager =  (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =  (SearchView) menu.findItem(R.id.search).getActionView();
            final Activity activity=this;
            searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    query=query.replaceAll("\\s","+");

                    Intent intent=new Intent(activity,SearchMovieActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    Bundle extras=new Bundle();
                    extras.putString(MyConstants.SEARCH_KEY,query);
                    extras.putInt(MyConstants.SEARCH_TYPE, MyConstants.SEARCH_TYPE_MOVIE);

                    intent.putExtras(extras);

                    startActivity(intent);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
