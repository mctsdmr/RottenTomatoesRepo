package tomatoes.rotten.erkanerol.refactor;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import BackEnd.Container;
import BackEnd.Converter;
import BackEnd.Movie;

/**
 * Created by erkanerol on 8/5/14.
 */
public class MovieFragment extends Fragment implements Downloader.AsyncResponse{

    public Movie movie;
    public Movie detailedMovie;
    public View rootView;
    public int downloadState=MyConstants.DOWNLOAD_STATE_BEFORE;

    public MovieFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args=getArguments();
        movie=(Movie)args.getSerializable(MyConstants.MOVIE_OBJECT);
        String request= MyConstants.MOVIE_DETAIL_REQUEST_BEGIN+
                        movie.id+
                        MyConstants.MOVIE_DETAIL_REQUEST_END+
                        MyConstants.API_KEY;
        Downloader downloader=new Downloader();
        downloader.delegate=this;
        downloader.execute(request);
        downloadState=MyConstants.DOWNLOAD_STATE_WORK;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_movie,container,false);
        if(downloadState==MyConstants.DOWNLOAD_STATE_END){
            change();
            onPlaceDetailedMovie();
        }
        return rootView;
    }




    public void onPlaceDetailedMovie(){
        ImageView poster= (ImageView) rootView.findViewById(R.id.movie_poster);
        Picasso.with(getActivity())
                .load(movie.posters.original)
                .placeholder(R.drawable.poster_default)
                .into(poster);


        TextView title= (TextView) rootView.findViewById(R.id.movie_title);
        title.setText(movie.title);

        if(movie.runtime!=null){
            TextView runtime= (TextView)rootView.findViewById(R.id.movie_runtime);
            runtime.setText(movie.runtime);
        }

        TextView mpaa= (TextView) rootView.findViewById(R.id.mpaa_rating);
        if(movie.mpaa_rating!=null && !movie.mpaa_rating.equals("Unrated")){

            mpaa.setText(movie.mpaa_rating);
        }
        else{
            mpaa.setVisibility(View.INVISIBLE);
        }



        if(movie.ratings.critics_score!=null && !movie.ratings.critics_score.equals("-1")){
            TextView critics=(TextView) rootView.findViewById(R.id.criticsScore);
            critics.setText(movie.ratings.critics_score);
        }
        else{
            TextView criticsText=(TextView)rootView.findViewById(R.id.criticsText);
            criticsText.setVisibility(View.INVISIBLE);
        }

        if(movie.ratings.audience_score!=null && !movie.ratings.audience_score.equals("-1") ){
            TextView audience= (TextView) rootView.findViewById(R.id.audienceScore);
            audience.setText(movie.ratings.audience_score);
        }
        else{
            TextView audienceText=(TextView)rootView.findViewById(R.id.audienceText);
            audienceText.setVisibility(View.INVISIBLE);
        }


        if(movie.genres!=null && !"".equals(movie.genres)){
            TextView genres=(TextView)rootView.findViewById(R.id.genres);
            genres.setText(movie.genres);
        }
        else{
            TextView genresText=(TextView)rootView.findViewById(R.id.genresText);
            genresText.setVisibility(View.GONE);
        }



        if(movie.synopsis!=null && !movie.synopsis.equals("")){
            TextView synopsis=(TextView)rootView.findViewById(R.id.synopsis);
            synopsis.setText(movie.synopsis);
        }
        else{
            TextView synopsisText=(TextView)rootView.findViewById(R.id.synopsisText);
            synopsisText.setVisibility(View.GONE);
        }

        if(movie.actors!=null){
            TextView cast=(TextView)rootView.findViewById(R.id.cast);
            String cast_merge="";
            for(int i=0;i<movie.actors.length;++i){
                cast_merge+=movie.actors[i].name;
                if(movie.actors[i].characters!=null){
                    cast_merge+=" ("+movie.actors[i].characters+") ";
                }
                if(i!=movie.actors.length-1)
                    cast_merge+=",";
            }
            cast.setText(cast_merge);
        }
        else{
            TextView castText=(TextView)rootView.findViewById(R.id.castText);
            castText.setVisibility(View.GONE);
        }

        if(!"".equals(movie.studio)){
            TextView studio=(TextView)rootView.findViewById(R.id.studio);
            studio.setText(movie.studio);
        }
        else{
            TextView studioText=(TextView)rootView.findViewById(R.id.studioText);
            studioText.setVisibility(View.GONE);
        }

        if(!"".equals(movie.directors) && movie.directors!=null){
            TextView director=(TextView)rootView.findViewById(R.id.director);
            director.setText(movie.directors);
        }
        else{
            TextView directorText=(TextView)rootView.findViewById(R.id.directorText);
            directorText.setVisibility(View.GONE);
        }



        Button imdb= (Button) rootView.findViewById(R.id.imdb);
        if(movie.alternate_ids.imdb!=null){
            final String id=movie.alternate_ids.imdb;
            imdb.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://www.imdb.com/title/tt"+id;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        }
        else
            imdb.setVisibility(View.GONE);



        final ImageView imageView=(ImageView)rootView.findViewById(R.id.favorite);

        if(DbManager.search(movie)){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.favorite));
            imageView.setOnClickListener(favorite);
        }
        else{
            imageView.setOnClickListener(notFavorite);
        }


        TextView similar=(TextView)rootView.findViewById(R.id.similar);
        similar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SearchMovieActivity.class);

                Bundle extras=new Bundle();
                extras.putInt(MyConstants.SEARCH_TYPE,MyConstants.SEARCH_TYPE_SIMILAR);
                String query=movie.links.similar+"?";
                extras.putString(MyConstants.SEARCH_SIMILAR_REQUEST,query);

                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }


    View.OnClickListener favorite=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageView imageView=(ImageView)v;
            DbManager.delete(movie);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_important));
            imageView.setOnClickListener(notFavorite);
        }
    };

    View.OnClickListener notFavorite=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageView imageView=(ImageView)v;
            DbManager.add(movie);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.favorite));
            imageView.setOnClickListener(favorite);
        }
    };

    @Override
    public void downloadFinish(JSONObject jSONResponse, int successFlag, int total) {
        try {
            detailedMovie= Converter.convertMovie(jSONResponse);
            movie=detailedMovie;
            onPlaceDetailedMovie();
            change();
            downloadState=MyConstants.DOWNLOAD_STATE_END;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void change(){
        ProgressBar progress=(ProgressBar)rootView.findViewById(R.id.fullProgressBar);
        progress.setVisibility(View.GONE);

        ScrollView scroll=(ScrollView)rootView.findViewById(R.id.fullScrollView);
        scroll.setVisibility(View.VISIBLE);
    }

}

