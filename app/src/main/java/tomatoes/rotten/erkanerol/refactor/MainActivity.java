package tomatoes.rotten.erkanerol.refactor;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import BackEnd.Container;


public class MainActivity extends FragmentActivity {

    public MainFragment movies;
    public MainFragment dvds;

    public int currentMainFragment=MyConstants.MAIN_FRAGMENT_MOVIE;

    private String id_name="tomatoes.rotten.erkanerol.refactor";


    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbManager.initialize(this);
        readCountry();

        initializeFragments();
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, movies, "fragment")
                    .commit();
        }

        initializeActionBar();
        initializeNavigationDrawer();
    }

    private void initializeNavigationDrawer() {
        ArrayList<String> array=new ArrayList(Arrays.asList(getResources().getStringArray(R.array.navigation_drawer)));

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array));
        mDrawerList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerClick(position);
            }
        });


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private void initializeActionBar() {

        String[] actions=getResources().getStringArray(R.array.action_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, actions);

        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.rotten_green)));

        ActionBar.OnNavigationListener navigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                if(currentMainFragment!=itemPosition){
                    change(itemPosition);
                    currentMainFragment=itemPosition;
                }
                return false;
            }
        };

        getActionBar().setListNavigationCallbacks(adapter, navigationListener);
    }


    public void initializeFragments(){

        movies=new MainFragment();
        dvds=new MainFragment();

        Bundle movie_bundle=new Bundle();
        movie_bundle.putInt(MyConstants.MAIN_TYPE,MyConstants.MAIN_FRAGMENT_MOVIE);
        movies.setArguments(movie_bundle);

        Bundle dvd_bundle=new Bundle();
        dvd_bundle.putInt(MyConstants.MAIN_TYPE,MyConstants.MAIN_FRAGMENT_DVD);
        dvds.setArguments(dvd_bundle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager =  (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =  (SearchView) menu.findItem(R.id.search).getActionView();
        final Activity activity=this;
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query=query.replaceAll("\\s","+");

                Intent intent=new Intent(activity,SearchMovieActivity.class);

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

        return true;
    }


    private void change(int itemPosition) {
        if(itemPosition==0)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, movies, "fragment")
                    .commit();
        else
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, dvds, "fragment")
                    .commit();
    }

    private void drawerClick(int position) {

        switch (position){
            case 0:
                Intent favorites=new Intent(this,SearchMovieActivity.class);
                Bundle extras=new Bundle();
                extras.putInt(MyConstants.SEARCH_TYPE, MyConstants.SEARCH_TYPE_FAVORITES);
                favorites.putExtras(extras);
                startActivity(favorites);
                break;
            case 1:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + id_name));
                if (!MyStartActivity(intent)) {
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+id_name));
                    if (!MyStartActivity(intent)) {
                        Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 2:
                final Dialog country=new Dialog(this);
                country.setContentView(R.layout.layout_country);

                ListView listView= (ListView) country.findViewById(R.id.country_list);

                ArrayList<String> array=new ArrayList(Arrays.asList(getResources().getStringArray(R.array.country)));
                final ArrayList<String> codes=new ArrayList(Arrays.asList(getResources().getStringArray(R.array.codes)));
                listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array));

                listView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(position!=MyConstants.selected){
                                    MyConstants.COUNTRY_SELECTED=codes.get(position);
                                    MyConstants.selected=position;
                                    initializeFragments();
                                    change(currentMainFragment);
                                }
                                country.dismiss();
                            }
                        }
                );

                country.setTitle(getResources().getString(R.string.countryText));
                country.show();
                break;
            case 3:
                Dialog about=new Dialog(this);
                about.setContentView(R.layout.layout_about);
                about.setTitle(getResources().getString(R.string.companyName));
                about.show();
                break;
        }
        return;
    }


    private boolean MyStartActivity(Intent aIntent) {
        try
        {
            startActivity(aIntent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        DbManager.close();
        writeCountry();
        super.onDestroy();

    }

    private void readCountry(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int selected=sharedPref.getInt("country", 0);
        ArrayList<String> codes=new ArrayList(Arrays.asList(getResources().getStringArray(R.array.codes)));
        MyConstants.COUNTRY_SELECTED=codes.get(selected);
        MyConstants.selected=selected;
    }

    private void writeCountry(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("country",MyConstants.selected);
        editor.commit();
    }
}
