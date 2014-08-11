package tomatoes.rotten.erkanerol.refactor;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import BackEnd.Container;
import BackEnd.Converter;
import BackEnd.Movie;


public class SimilarFragment extends Fragment implements Downloader.AsyncResponse {

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
        request+=MyConstants.API_KEY;

        Downloader downloader=new Downloader();
        downloader.delegate=this;
        downloader.execute(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_similar, container, false);




        return rootView;
    }


    @Override
    public void downloadFinish(JSONObject jSONResponse, int successFlag, int total) {
        if(successFlag==MyConstants.DOWNLOAD_FLAG_CORRECT){
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
                                                 Intent movieActivity=new Intent(getActivity(),FullScreenMovieActivity.class);
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
