package fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

import adapters.CastListAdapter;
import backend.Actors;
import backend.Movie;
import database.DbManager;
import tomatoes.rotten.erkanerol.refactor.CastActivity;
import tomatoes.rotten.erkanerol.refactor.MyConstants;
import tomatoes.rotten.erkanerol.refactor.R;

/**
 * Created by erkanerol on 8/15/14.
 */
public class ViewPlacer {
    Context context;
    Movie movie;
    String emptyString="";

    public ViewPlacer(Context context,Movie movie){
        super();
        this.context=context;
        this.movie=movie;
    }

    public void movieFragmentPlacer(View rootView){

        imageLoader(rootView, R.id.movie_poster, movie.posters.original,R.drawable.poster_default);

        textPlacer(rootView,R.id.movie_title,R.id.movie_title,movie.title,emptyString,View.INVISIBLE);

        textPlacer(rootView,R.id.movie_runtime,R.id.movie_runtime,movie.runtime,emptyString,View.INVISIBLE);

        textPlacer(rootView,R.id.mpaa_rating,R.id.mpaa_rating,movie.mpaa_rating,"Unrated",View.INVISIBLE);

        textPlacer(rootView,R.id.criticsText,R.id.criticsScore,movie.ratings.critics_score,"-1",View.INVISIBLE);

        textPlacer(rootView,R.id.audienceText,R.id.audienceScore,movie.ratings.audience_score,"0",View.INVISIBLE);

        imdbButton(rootView);

        favoriteImage(rootView);

        textPlacer(rootView, R.id.genresText, R.id.genres, movie.genres, emptyString, View.GONE);

        textPlacer(rootView,R.id.directorText,R.id.director,movie.directors,emptyString,View.GONE);

        textPlacer(rootView,R.id.synopsisText,R.id.synopsis,movie.synopsis,emptyString,View.GONE);

        castLister(rootView);

        textPlacer(rootView,R.id.studioText,R.id.studio,movie.studio,emptyString,View.GONE);

    }

    public void imageLoader(View rootView,int imageViewId,String imageUrl,int defaultImageId){
        ImageView image= (ImageView) rootView.findViewById(imageViewId);
        Picasso.with(context)
                .load(imageUrl)
                .placeholder(defaultImageId)
                .into(image);
    }

    private void favoriteImage(View rootView) {

        final ImageView imageView=(ImageView)rootView.findViewById(R.id.favorite);

        if(DbManager.search(movie)>=0){
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.favorite));
            imageView.setOnClickListener(favorite);
        }
        else{

            imageView.setOnClickListener(notFavorite);
        }

    }

    public void imdbButton(View rootView){
        Button imdb= (Button) rootView.findViewById(R.id.imdb);
        if(movie.alternate_ids.imdb!=null){
            final String id=movie.alternate_ids.imdb;
            imdb.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://www.imdb.com/title/tt"+id;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });
        }
        else
            imdb.setVisibility(View.GONE);
    }

    public void castLister(View rootView){
        if(movie.actors!=null){
            LinearLayout myLayout = (LinearLayout) rootView.findViewById(R.id.cast);

            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            for(int i=0;i<movie.actors.length;++i){
                LinearLayout cast= (LinearLayout) inflater.inflate(R.layout.layaut_single_cast, null);

                TextView name = (TextView) cast.findViewById(R.id.actorName);
                TextView characters = (TextView) cast.findViewById(R.id.character);
                TextView characterText = (TextView) cast.findViewById(R.id.characterText);

                name.setText(movie.actors[i].name);
                if(movie.actors[i].characters!=null && !"".equals(movie.actors[i].characters)) {
                    characters.setText(movie.actors[i].characters);
                    Log.v("name: ", movie.actors[i].name);
                    Log.v("character: ", "*"+movie.actors[i].characters+"*");
                }
                else {
                    characters.setVisibility(View.GONE);
                    characterText.setVisibility(View.GONE);
                }
                myLayout.addView(cast);
            }

        }
        else{
            TextView castText=(TextView)rootView.findViewById(R.id.castText);
            castText.setVisibility(View.GONE);
        }


        TextView showAllCast=(TextView)rootView.findViewById(R.id.showAllCast);
        showAllCast.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CastActivity.class);

                Bundle extras=new Bundle();
                extras.putString(MyConstants.CAST_KEY, movie.links.cast);
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });

    }

    public void textPlacer(View rootView,int headerId,int contentId,String content,String defaultValue,int visibility){
        TextView contentTextView=(TextView)rootView.findViewById(contentId);
        if(content!=null && !defaultValue.equals(content) ){
            contentTextView.setText(content);
        }
        else{
            TextView headerTextView=(TextView)rootView.findViewById(headerId);
            headerTextView.setVisibility(visibility);
            contentTextView.setVisibility(visibility);
        }

    }

    public View.OnClickListener favorite=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageView imageView=(ImageView)v;
            DbManager.delete(movie);
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_important));
            imageView.setOnClickListener(notFavorite);
        }
    };

    public View.OnClickListener notFavorite=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageView imageView=(ImageView)v;
            DbManager.add(movie);
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.favorite));
            imageView.setOnClickListener(favorite);
        }
    };
}
