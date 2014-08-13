package tomatoes.rotten.erkanerol.refactor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import backend.Container;
import backend.Movie;
import adapters.MovieFragmentPagerAdapter;
import tomatoes.rotten.erkanerol.refactor.MyConstants;
import tomatoes.rotten.erkanerol.refactor.R;

public class FullScreenMovieActivity extends FragmentActivity {

    ArrayList<Movie> movies;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_movie);

        Bundle extras=getIntent().getExtras();
        movies=((Container)extras.getSerializable(MyConstants.MOVIE_ARRAY)).movies;
        int position=extras.getInt(MyConstants.POSITION);
        final MovieFragmentPagerAdapter adapter=new MovieFragmentPagerAdapter(getSupportFragmentManager(),movies);

        viewPager=(ViewPager)findViewById(R.id.moviePager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.full_screen_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_share) {
            int position=viewPager.getCurrentItem();
            Movie movie=movies.get(position);
            String share=movie.title+" "+"http://www.rottentomatoes.com/m/"+movies.get(position).id+"   #rottentomatoes";


            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT,share);
            startActivity(sharingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
