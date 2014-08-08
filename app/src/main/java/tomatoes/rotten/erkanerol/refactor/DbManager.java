package tomatoes.rotten.erkanerol.refactor;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import BackEnd.Movie;

/**
 * Created by erkanerol on 8/6/14.
 */
public class DbManager {

    public static DBHelper db;
    public static ArrayList<Movie> favorites;


    public static void initialize(Context context){
        db=new DBHelper(context);
        favorites=db.read();
    }

    public static void close(){
        db.write(favorites);
    }

    public static void add(Movie movie){
        if(!search(movie))
            favorites.add(movie);
    }

    public static boolean search(Movie movie){
        for(int i=0;i<favorites.size();++i){
            Log.v("i:",""+i);
            Log.v("favorites.get(i):",favorites.get(i).id+"");
            Log.v("movie.id",movie.id);
            if(favorites.get(i).id.equals(movie.id)){
                return true;
            }
        }
        return false;
    }

    public static void delete(Movie movie){
        for(int i=0;i<favorites.size();++i){
            Log.v("i:",""+i);
            Log.v("favorites.get(i):",favorites.get(i).id+"");
            Log.v("movie.id",movie.id);
            if(favorites.get(i).id.equals(movie.id)){
                favorites.remove(i);
                return;
            }
        }
    }

}
