package tomatoes.rotten.erkanerol.refactor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import BackEnd.Container;
import BackEnd.Converter;
import BackEnd.Movie;


public class MovieListFragment1 extends Fragment implements Downloader.AsyncResponse {

    public int type;
    public int page=1;
    public int total=1000;

    public int fragmentState = MyConstants.FRAGMENT_STATE_INITIAL;
    public int downloadState = MyConstants.DOWNLOAD_STATE_BEFORE;

    ArrayList<Movie> movies = movies = new ArrayList<Movie>();
    public ListView mListView;
    MovieListAdapter adapter;

    View rootView;
    View headerView;
    View footerView;

    public MovieListFragment1() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        type = args.getInt(MyConstants.LIST_FRAGMENT_TYPE);

        if (type < 10) {
            download();
        }
        else{
            movies=DbManager.favorites;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mListView = (ListView) rootView.findViewById(R.id.movie_list);
        mListView.setDividerHeight(15);

        adapter = new MovieListAdapter(getActivity(), movies);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent movieActivity=new Intent(getActivity(),FullScreenMovieActivity.class);
                Bundle extras=new Bundle();
                Container container1=new Container(movies);
                extras.putSerializable(MyConstants.MOVIE_ARRAY,container1);
                if(type<8){
                    extras.putInt(MyConstants.POSITION,position-1);
                }

                else
                    extras.putInt(MyConstants.POSITION,position);
                movieActivity.putExtras(extras);
                startActivity(movieActivity);

            }
        });
        if (isTypeOne()) {
            onCreateViewForTypeOne();
        }
        else if(isTypeTwo()){
            onCreateViewForTypeTwo();
        }
        fragmentState = MyConstants.FRAGMENT_STATE_ONVIEW;
        return rootView;

    }

    public void download(){
        Downloader downloader = new Downloader();
        downloader.delegate = this;
        String request = createRequest();
        downloader.execute(request);
        downloadState = MyConstants.DOWNLOAD_STATE_WORK;
    }

    public void onCreateViewForTypeOne() {
        setHeader();
        if(downloadState==MyConstants.DOWNLOAD_STATE_WORK){
            setFooter();
            footerLoading();
        }
        return;
    }


    private void onCreateViewForTypeTwo() {
        setHeader();
        setFooter();
        if(downloadState==MyConstants.DOWNLOAD_STATE_WORK){
            footerLoading();
        }
        else if((page-1)*MyConstants.PAGE_SIZE_TYPE2<total){
            footerLoadMore();
        }
        else
            mListView.removeFooterView(footerView);
        return;
    }

    private void setFooter() {
        Log.v("I am here","2");
        footerView = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null, false);
        mListView.addFooterView(footerView);
    }

    private void setHeader() {
        if(type<MyConstants.MAIN_PAGE_COUNT*2){
            headerView=(RelativeLayout)getActivity().getLayoutInflater().inflate(R.layout.listview_header,null,false);
            TextView headerText= (TextView) headerView.findViewById(R.id.header_text);
            String header=getResources().getStringArray(R.array.headers)[type];
            headerText.setText(header);

            if(type==0){
                ImageView image=(ImageView)headerView.findViewById(R.id.left);
                image.setVisibility(View.INVISIBLE);
            }

            if(type==MyConstants.MAIN_PAGE_COUNT-1){
                ImageView image=(ImageView)headerView.findViewById(R.id.right);
                image.setVisibility(View.INVISIBLE);
            }
            headerView.setClickable(false);
            mListView.addHeaderView(headerView,null,false);
        }
    }



    public String createRequest() {
        if(type==MyConstants.FRAGGMENT_TYPE_SEARCH){
            String key=getArguments().getString(MyConstants.SEARCH_KEY);
            String request=     MyConstants.API_REQUEST[type]+
                    key+
                    MyConstants.API_KEY+
                    MyConstants.PAGE_LIMIT+
                    MyConstants.PAGE_SIZE_TYPE2+
                    MyConstants.PAGE+
                    page;
            return request;

        }
        if(type==MyConstants.FRAGGMENT_TYPE_SIMILAR){
           String request=  getArguments().getString(MyConstants.SEARCH_SIMILAR_REQUEST)+
                            MyConstants.API_KEY;
           return request;

        }
        if (isTypeOne()) {

            String request =    MyConstants.API_REQUEST[type] +
                                MyConstants.API_KEY+
                                MyConstants.LIMIT+
                                MyConstants.PAGE_SIZE_TYPE1+
                                MyConstants.COUNTRY_TEXT+
                                MyConstants.COUNTRY_SELECTED;
            return request;
        }
        else if(isTypeTwo()){

            String request =    MyConstants.API_REQUEST[type] +
                                MyConstants.API_KEY+
                                MyConstants.PAGE_LIMIT+
                                MyConstants.PAGE_SIZE_TYPE2+
                                MyConstants.PAGE+
                                page;
            return request;
        }

        String request = MyConstants.API_REQUEST[type] + MyConstants.API_KEY;
        page++;
        return request;
    }

    @Override
    public void downloadFinish(JSONObject jSONResponse, int successFlag,int total) {
        if (successFlag == MyConstants.DOWNLOAD_FLAG_CONNECTION_ERROR) {
            Toast.makeText(getActivity(), getResources().getString(R.string.connectionError), Toast.LENGTH_LONG).show();
            return;
        }
        if (successFlag == MyConstants.DOWNLOAD_FLAG_CORRECT) {

            convertJSON(jSONResponse);
            page++;

            if (isTypeOne()) {
                if(movies.size()==0){
                    footerError();
                }
                else{
                    mListView.removeFooterView(footerView);
                    Log.v("I am here","1");
                }


            }
            else if(isTypeTwo()){

                if(total!=0){
                    this.total=total;
                }
                if(movies.size()==0){
                    footerError();
                }
                else{
                    if((page-1)*MyConstants.PAGE_SIZE_TYPE2<total){
                        if (fragmentState == MyConstants.FRAGMENT_STATE_ONVIEW) {
                            footerLoadMore();
                        }
                    }
                    else{
                        mListView.removeFooterView(footerView);
                    }
                }
            }

            if (fragmentState == MyConstants.FRAGMENT_STATE_ONVIEW) {
                adapter.notifyDataSetChanged();
            }
            downloadState = MyConstants.DOWNLOAD_STATE_END;
            return;
        }


    }

    public void convertJSON(JSONObject jSONResponse) {
        Converter.convertMovieArray(jSONResponse, movies);
        Log.v("movie length",""+movies.size());
    }

    public void footerLoading() {
        ProgressBar progressBar = (ProgressBar) footerView.findViewById(R.id.downloadProgress);
        progressBar.setVisibility(View.VISIBLE);

        TextView error = (TextView) footerView.findViewById(R.id.errorText);
        error.setVisibility(View.GONE);

        TextView load_more = (TextView) footerView.findViewById(R.id.load_more);
        load_more.setVisibility(View.GONE);
    }

    public void footerError() {
        ProgressBar progressBar = (ProgressBar) footerView.findViewById(R.id.downloadProgress);
        progressBar.setVisibility(View.GONE);

        TextView error = (TextView) footerView.findViewById(R.id.errorText);
        error.setVisibility(View.VISIBLE);

        TextView load_more = (TextView) footerView.findViewById(R.id.load_more);
        load_more.setVisibility(View.GONE);
        mListView.addFooterView(footerView,null,false);
    }

    public void footerLoadMore() {

        ProgressBar progressBar = (ProgressBar) footerView.findViewById(R.id.downloadProgress);
        progressBar.setVisibility(View.GONE);

        TextView error = (TextView) footerView.findViewById(R.id.errorText);
        error.setVisibility(View.GONE);

        TextView load_more = (TextView) footerView.findViewById(R.id.load_more);
        load_more.setVisibility(View.VISIBLE);

        footerView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                footerLoading();
                download();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentState = MyConstants.FRAGMENT_STATE_STOP;
    }

    public boolean isTypeOne(){
        if (type == MyConstants.FRAGMENT_TYPE_BOX_OFFICE ||
                type == MyConstants.FRAGGMENT_TYPE_OPENING_MOVIES ||
                type == MyConstants.FRAGGMENT_TYPE_TOP_RENTALS ||
                type==MyConstants.FRAGGMENT_TYPE_SIMILAR) {
            return true;
        }
        else
            return false;
    }

    public boolean isTypeTwo(){
        if(type==MyConstants.FRAGGMENT_TYPE_IN_THEATERS ||
                type==MyConstants.FRAGGMENT_TYPE_UPCOMING_MOVIES ||
                type==MyConstants.FRAGGMENT_TYPE_CURRENT_RELEASES ||
                type==MyConstants.FRAGGMENT_TYPE_NEW_DVD ||
                type==MyConstants.FRAGGMENT_TYPE_UPCOMING_DVD ||
                type==MyConstants.FRAGGMENT_TYPE_SEARCH){

            return true;
        }
        else
            return false;
    }
}
