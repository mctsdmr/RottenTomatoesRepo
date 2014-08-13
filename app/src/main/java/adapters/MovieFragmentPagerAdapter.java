package adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import backend.Movie;
import fragments.MovieFragment;
import tomatoes.rotten.erkanerol.refactor.MyConstants;

/**
 * Created by erkanerol on 8/5/14.
 */
public class MovieFragmentPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Movie> movies;

    public MovieFragmentPagerAdapter(FragmentManager fm, ArrayList<Movie> movies) {
        super(fm);
        this.movies=movies;
    }

    @Override
    public Fragment getItem(int i) {
        MovieFragment fragment=new MovieFragment();
        Bundle extras=new Bundle();
        extras.putSerializable(MyConstants.MOVIE_OBJECT,movies.get(i));
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public int getCount() {
        return movies.size();
    }
}
