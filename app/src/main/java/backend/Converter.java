package backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by erkanerol on 7/25/14.
 */
public class Converter{

    public static ArrayList<Movie> convertMovieArray(JSONObject json,ArrayList<Movie> array){
        try {
            JSONArray jArray=json.getJSONArray("movies");
            int length=jArray.length();

            for(int i=0;i<length;++i){
                JSONObject jObject=jArray.getJSONObject(i);
                array.add(convertMovie(jObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return array;
    }


    public static Movie convertMovie(JSONObject jObject) throws JSONException {
        Movie movie=new Movie();
        movie.id=jObject.optString("id");
        movie.title=jObject.optString("title");
        movie.year=jObject.optString("year");
        movie.mpaa_rating=jObject.optString("mpaa_rating");
        movie.runtime=jObject.optString("runtime");
        movie.critics_consensus=jObject.optString("critics_consensus");
        movie.studio=jObject.optString("studio");

        JSONArray genresArray=jObject.optJSONArray("genres");


        if(genresArray!=null){

            movie.genres="";
            int i;
            for(i=0;i<genresArray.length()-1;++i)
                movie.genres+=genresArray.getString(i)+",";
            movie.genres+=genresArray.getString(i);

        }


        JSONArray directorArray=jObject.optJSONArray("abridged_directors");
        if(directorArray!=null){
            movie.directors="";
            int i;
            for(i=0;i<directorArray.length()-1;++i){
                JSONObject director=directorArray.getJSONObject(i);
                movie.directors+=director.getString("name")+",";
            }
            JSONObject director=directorArray.getJSONObject(i);
            movie.directors+=director.getString("name");
        }



        JSONObject release=jObject.optJSONObject("release_dates");
        if(release!=null){
            movie.release_dates.theater=release.optString("theater");
            movie.release_dates.dvd=release.optString("dvd");
        }

        JSONObject rating=jObject.optJSONObject("ratings");
        if(rating!=null){
            movie.ratings.audience_rating=rating.optString("audience_rating");
            movie.ratings.audience_score=rating.optString("audience_score");
            movie.ratings.critics_score=rating.optString("critics_score");
            movie.ratings.critics_rating=rating.optString("critics_rating");
        }

        movie.synopsis=jObject.optString("synopsis");



        JSONObject posters=jObject.optJSONObject("posters");
        if(posters!=null){
            movie.posters.thumbnail=posters.optString("thumbnail");
            movie.posters.profile=posters.optString("profile");
            movie.posters.detailed=posters.optString("detailed");
            movie.posters.original=posters.optString("original");
        }

        JSONArray abridged_cast=jObject.optJSONArray("abridged_cast");
        if(abridged_cast!=null && abridged_cast.length()>0){

            movie.actors=new Actors[abridged_cast.length()];


            for(int k=0;k<abridged_cast.length();++k){
                movie.actors[k]=new Actors();
                JSONObject actor= null;
                try {
                    actor = abridged_cast.getJSONObject(k);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                movie.actors[k].id=actor.optString("id");
                movie.actors[k].name=actor.optString("name");

                JSONArray charArray=actor.optJSONArray("characters");
                if(charArray!=null && charArray.length()>0){
                    movie.actors[k].characters="";
                    int i;
                    for(i=0;i<charArray.length()-1;++i)
                        movie.actors[k].characters+=charArray.getString(i)+",";
                    movie.actors[k].characters+=charArray.getString(i);
                }
            }
        }

        JSONObject alternate_ids=jObject.optJSONObject("alternate_ids");
        if(alternate_ids!=null) {
            movie.alternate_ids.imdb =alternate_ids.optString("imdb");
        }

        JSONObject links=jObject.optJSONObject("links");
        if(links!=null){
            movie.links.self=links.optString("self");
            movie.links.alternate=links.optString("alternate");
            movie.links.cast=links.optString("cast");
            movie.links.reviews=links.optString("reviews");
            movie.links.similar=links.optString("similar");
        }
        return  movie;
    }


    public static ArrayList<Actors> convertActorsArray(JSONObject json,ArrayList<Actors> array){
        try {
            JSONArray jArray=json.getJSONArray("cast");
            int length=jArray.length();

            for(int i=0;i<length;++i){
                JSONObject jObject=jArray.getJSONObject(i);
                array.add(convertActors(jObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return array;
    }

    private static Actors convertActors(JSONObject jObject) {
        Actors actor=new Actors();
        actor.id=jObject.optString("id");
        actor.name=jObject.optString("name");


        JSONArray charArray=jObject.optJSONArray("characters");
        if(charArray!=null && charArray.length()>0){
            actor.characters="";
            int i;
            for(i=0;i<charArray.length()-1;++i)
                try {
                    actor.characters+=charArray.getString(i)+",";

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            try {
                actor.characters+=charArray.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return actor;
    }
}
