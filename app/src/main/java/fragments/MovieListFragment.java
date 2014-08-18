package fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import adapters.MovieListAdapter;
import backend.Container;
import backend.Converter;
import backend.GenericDownloader;
import backend.Movie;
import database.DbManager;
import tomatoes.rotten.erkanerol.refactor.FullScreenMovieActivity;
import tomatoes.rotten.erkanerol.refactor.MyConstants;
import tomatoes.rotten.erkanerol.refactor.R;
import util.Connections;


public class MovieListFragment extends Fragment implements GenericDownloader.AsyncResponse {

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

    public MovieListFragment() {
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
            movies= new ArrayList<Movie>(DbManager.favorites);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(type<8)
            setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mListView = (ListView) rootView.findViewById(R.id.movie_list);
        mListView.setDividerHeight(15);

        adapter = new MovieListAdapter(getActivity(), movies);


        mListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent movieActivity=new Intent(getActivity(), FullScreenMovieActivity.class);
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

        mListView.setAdapter(adapter);
        fragmentState = MyConstants.FRAGMENT_STATE_ONVIEW;

        Connections.isNetworkConnected(getActivity());

        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v("burada","01");
        inflater.inflate(R.menu.main, menu);
        Log.v("burada","0");
        SearchManager searchManager =  (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =  (SearchView) menu.findItem(R.id.mainSearch).getActionView();

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Movie> newArray=search(movies,query);
                MovieListAdapter newAdapter = new MovieListAdapter(getActivity(), newArray);
                mListView.setAdapter(newAdapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v("burada", "2");
                return false;
            }


        });

        MenuItem menuItem = menu.findItem(R.id.mainSearch);
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.v("burada", "3");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.v("burada", "4");
                mListView.setAdapter(adapter);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

    private ArrayList<Movie> search(ArrayList<Movie> movies, String query) {
        ArrayList<Movie> newArray=new ArrayList<Movie>();
        for(int i=0;i<movies.size();++i){
            if(movies.get(i).title.toLowerCase().contains(query.toLowerCase())){
                newArray.add(movies.get(i));
            }

        }
        return newArray;
    }

    public void download(){
            GenericDownloader downloader=new GenericDownloader();
            downloader.setType(GenericDownloader.HttpType.GET);
            downloader.setDelegate(this);
            createRequest(downloader);
            downloadState = MyConstants.DOWNLOAD_STATE_WORK;
            downloader.execute();
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

            if(type==0 || type==MyConstants.MAIN_PAGE_COUNT){
                ImageView image=(ImageView)headerView.findViewById(R.id.left);
                image.setVisibility(View.INVISIBLE);
            }

            if(type==MyConstants.MAIN_PAGE_COUNT-1 || type==MyConstants.MAIN_PAGE_COUNT*2-1){
                ImageView image=(ImageView)headerView.findViewById(R.id.right);
                image.setVisibility(View.INVISIBLE);
            }
            headerView.setClickable(false);
            mListView.addHeaderView(headerView,null,false);
        }
    }



    public void createRequest(GenericDownloader downloader) {

        downloader.setRequestUrl(MyConstants.API_REQUEST[type]);
        downloader.setParameter(MyConstants.GET_API_KEY,MyConstants.GET_API_VALUE);

        if(type==MyConstants.FRAGGMENT_TYPE_SEARCH){
            String key=getArguments().getString(MyConstants.SEARCH_KEY);
            downloader.setParameter(MyConstants.GET_SEARCH_KEY,key);
            downloader.setParameter(MyConstants.GET_PAGE_LIMIT,""+MyConstants.PAGE_SIZE_TYPE2);
            downloader.setParameter(MyConstants.PAGE,page+"");
            return;
        }

        if (isTypeOne()) {
            downloader.setParameter(MyConstants.GET_LIMIT,""+MyConstants.PAGE_SIZE_TYPE1);
            downloader.setParameter(MyConstants.GET_COUNTRY_TEXT,MyConstants.COUNTRY_SELECTED);
            return;
        }

        else if(isTypeTwo()){
            downloader.setParameter(MyConstants.GET_PAGE_LIMIT,""+MyConstants.PAGE_SIZE_TYPE2);
            downloader.setParameter(MyConstants.PAGE,page+"");
            return ;
        }
    }


    @Override
    public void downloadFinish(JSONObject jSONResponse, GenericDownloader.ResultType resultType1) {
        if(resultType1== GenericDownloader.ResultType.HTTP_ERROR){
            return;
        }
        if(resultType1== GenericDownloader.ResultType.SUCCESSFUL){
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

                if( jSONResponse.optInt("total")!=0){
                    this.total=jSONResponse.optInt("total");
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

                int index = mListView.getFirstVisiblePosition();
                View v = mListView.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();
                mListView.setSelectionFromTop(index, top);
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
        mListView.setOnScrollListener(noLoad);
    }

    public void footerError() {
        ProgressBar progressBar = (ProgressBar) footerView.findViewById(R.id.downloadProgress);
        progressBar.setVisibility(View.GONE);

        TextView error = (TextView) footerView.findViewById(R.id.errorText);
        error.setVisibility(View.VISIBLE);

        TextView load_more = (TextView) footerView.findViewById(R.id.load_more);
        load_more.setVisibility(View.GONE);
        mListView.addFooterView(footerView,null,false);
        mListView.setOnScrollListener(noLoad);
    }

    public void footerLoadMore() {

        footerView.setVisibility(View.GONE);

        mListView.setOnScrollListener(load);
    }

    AbsListView.OnScrollListener load=new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(firstVisibleItem+visibleItemCount>movies.size()){
                footerView.setVisibility(View.VISIBLE);
                footerLoading();
                download();
            }
        }
    };

    AbsListView.OnScrollListener noLoad=new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            return;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            return;
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        fragmentState = MyConstants.FRAGMENT_STATE_STOP;
        Log.v("onPause",type+"");
    }

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    public boolean isTypeOne(){
        if (type == MyConstants.FRAGMENT_TYPE_BOX_OFFICE ||
                type == MyConstants.FRAGMENT_TYPE_OPENING_MOVIES ||
                type == MyConstants.FRAGMENT_TYPE_TOP_RENTALS) {
            return true;
        }
        else
            return false;
    }

    public boolean isTypeTwo(){
        if(type==MyConstants.FRAGMENT_TYPE_IN_THEATERS ||
                type==MyConstants.FRAGMENT_TYPE_UPCOMING_MOVIES ||
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
