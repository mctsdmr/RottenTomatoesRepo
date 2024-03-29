package fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import adapters.CastListAdapter;
import backend.Actors;
import backend.Converter;
import backend.GenericDownloader;
import backend.Movie;
import database.DbManager;
import tomatoes.rotten.erkanerol.refactor.CastActivity;
import tomatoes.rotten.erkanerol.refactor.MyConstants;
import tomatoes.rotten.erkanerol.refactor.R;
import util.Connections;


public class MovieFragment extends Fragment implements SimilarFragment.SimilarInterface, GenericDownloader.AsyncResponse {

    public Movie movie;
    public Movie detailedMovie;
    public View rootView;
    public int downloadState= MyConstants.DOWNLOAD_STATE_BEFORE;
    SimilarFragment similar;

    public MovieFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args=getArguments();
        movie=(Movie)args.getSerializable(MyConstants.MOVIE_OBJECT);

        GenericDownloader downloader=new GenericDownloader();
        downloader.setType(GenericDownloader.HttpType.GET);
        downloader.setRequestUrl(MyConstants.MOVIE_DETAIL_REQUEST_BEGIN + movie.id + MyConstants.MOVIE_DETAIL_REQUEST_END);
        downloader.setParameter(MyConstants.GET_API_KEY,MyConstants.GET_API_VALUE);
        downloader.setDelegate(this);
        downloader.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Connections.isNetworkConnected(getActivity());
        rootView=inflater.inflate(R.layout.fragment_movie,container,false);
        if(downloadState==MyConstants.DOWNLOAD_STATE_END){
            change();
            onPlaceDetailedMovie();
        }
        if (savedInstanceState == null) {

            similar=new SimilarFragment();
            similar.delegate=this;
            Bundle extras=new Bundle();
            extras.putString(MyConstants.SEARCH_SIMILAR_REQUEST,movie.links.similar);
            Log.v("similar:", movie.links.similar);
            similar.setArguments(extras);
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.similarFragment, similar, "fragment")
                    .commit();
        }


        return rootView;
    }




    public void onPlaceDetailedMovie(){
        ViewPlacer placer=new ViewPlacer(getActivity(),movie);
        placer.movieFragmentPlacer(rootView);
    }

    public void change(){
        ProgressBar progress=(ProgressBar)rootView.findViewById(R.id.fullProgressBar);
        progress.setVisibility(View.GONE);

        ScrollView scroll=(ScrollView)rootView.findViewById(R.id.fullScrollView);
        scroll.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeFragment() {
        getChildFragmentManager().beginTransaction().remove(similar);
    }

    @Override
    public void downloadFinish(JSONObject jSONResponse, GenericDownloader.ResultType resultType1) {
        if(resultType1== GenericDownloader.ResultType.SUCCESSFUL){
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
    }
}


