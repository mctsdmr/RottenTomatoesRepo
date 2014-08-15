package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import backend.Container;
import backend.GenericDownloader;
import tomatoes.rotten.erkanerol.refactor.FullScreenMovieActivity;
import backend.Converter;
import backend.Movie;
import tomatoes.rotten.erkanerol.refactor.MyConstants;
import tomatoes.rotten.erkanerol.refactor.R;


public class SimilarFragment extends Fragment implements GenericDownloader.AsyncResponse {

    View rootView;
    ArrayList<Movie> movies=new ArrayList<Movie>();
    public SimilarInterface delegate=null;

    public SimilarFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args=getArguments();
        String request=args.getString(MyConstants.SEARCH_SIMILAR_REQUEST);
        Log.v("similar request:", request);

        GenericDownloader downloader=new GenericDownloader();
        downloader.setType(GenericDownloader.HttpType.GET);
        downloader.setDelegate(this);
        downloader.setRequestUrl(request);
        downloader.setParameter(MyConstants.GET_API_KEY,MyConstants.GET_API_VALUE);
        downloader.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_similar, container, false);
        return rootView;
    }

    @Override
    public void downloadFinish(JSONObject jSONResponse, GenericDownloader.ResultType resultType1) {
        if(resultType1== GenericDownloader.ResultType.SUCCESSFUL){
            Converter.convertMovieArray(jSONResponse,movies);
            if(movies.size()==0){
                rootView.setVisibility(View.GONE);
                delegate.removeFragment();
                return;
            }
            for(int i=0;i<movies.size();++i){
                LinearLayout myLayout = (LinearLayout) rootView.findViewById(R.id.horizontalLinear);

                ImageView image=new ImageView(getActivity());
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(120 , LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(50,0,50,0);
                image.setLayoutParams(lp);

                final int position=i;
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent movieActivity=new Intent(getActivity(), FullScreenMovieActivity.class);
                        Bundle extras=new Bundle();
                        Container container1=new Container(movies);
                        extras.putSerializable(MyConstants.MOVIE_ARRAY,container1);
                        extras.putInt(MyConstants.POSITION,position);
                        movieActivity.putExtras(extras);
                        startActivity(movieActivity);

                    }
                });



                myLayout.addView(image);

                Picasso.with(getActivity())
                        .load(movies.get(i).posters.original)
                        .placeholder(R.drawable.poster_default)
                        .into(image);

            }
        }
    }

    public interface SimilarInterface{
        void removeFragment();
    }
}
