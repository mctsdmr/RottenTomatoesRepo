package database;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import backend.Movie;

/**
 * Created by erkanerol on 8/6/14.
 */
public class DbManager {

    public static DBHelper db;
    public static ArrayList<Movie> favorites;
    public static boolean isInitialized=false;

    public static Comparator<Movie> comparator = new Comparator<Movie>() {
        public int compare(Movie m1, Movie m2) {
            return m1.compareTo(m2);
        }
    };


    public static void initialize(Context context){
        db=new DBHelper(context);
        favorites=db.read();
        isInitialized=true;
        Collections.sort(favorites);
    }


    public static void close(){
        db.write(favorites);
    }

    public static void add(Movie movie) {
        if(isInitialized){
            if(search(movie)<0){
                favorites.add(movie);
                Collections.sort(favorites);
            }

        }
    }


    public static int  search(Movie movie) {
        if(isInitialized){
            int index=Collections.binarySearch(favorites,movie,comparator);
            Log.v("search movie title:",movie.title);
            Log.v("search movie index",""+index);
            return index;
        }
        return -1;
    }

    //Precondition: initialize()
    public static void delete(Movie movie) {
        int index=search(movie);
        if(index>=0){
            favorites.remove(index);
        }

    }

}
