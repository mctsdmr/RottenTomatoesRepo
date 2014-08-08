package tomatoes.rotten.erkanerol.refactor;


public class MyConstants {

    public static final int MAIN_FRAGMENT_MOVIE=0;
    public static final int MAIN_FRAGMENT_DVD=1;

    public static final String MAIN_TYPE="mainType";
    public static final int    MAIN_PAGE_COUNT=4;


    public static final String[] API_REQUEST={
            "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?",
            "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?",
            "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/opening.json?",
            "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/upcoming.json?",
            "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/top_rentals.json?",
            "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/current_releases.json?",
            "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/new_releases.json?",
            "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/upcoming.json?",
            "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q="
    };

    public static final String MOVIE_DETAIL_REQUEST_BEGIN="http://api.rottentomatoes.com/api/public/v1.0/movies/";
    public static final String MOVIE_DETAIL_REQUEST_END=".json?";

    public static final String LIST_FRAGMENT_TYPE="listFragmentType";
    public static final int FRAGMENT_TYPE_BOX_OFFICE=0;
    public static final int FRAGGMENT_TYPE_IN_THEATERS=1;
    public static final int FRAGGMENT_TYPE_OPENING_MOVIES=2;
    public static final int FRAGGMENT_TYPE_UPCOMING_MOVIES=3;
    public static final int FRAGGMENT_TYPE_TOP_RENTALS=4;
    public static final int FRAGGMENT_TYPE_CURRENT_RELEASES=5;
    public static final int FRAGGMENT_TYPE_NEW_DVD=6;
    public static final int FRAGGMENT_TYPE_UPCOMING_DVD=7;
    public static final int FRAGGMENT_TYPE_SEARCH=8;
    public static final int FRAGGMENT_TYPE_SIMILAR=9;
    public static final int FRAGGMENT_TYPE_FAVORITES=10;

    public static final String API_KEY="&apikey=dzsdnwhb5hj2pb68v7bubaz6";

    public static final int FRAGMENT_STATE_INITIAL=0;
    public static final int FRAGMENT_STATE_ONVIEW=1;
    public static final int FRAGMENT_STATE_STOP=1;

    public static final int DOWNLOAD_STATE_BEFORE=0;
    public static final int DOWNLOAD_STATE_WORK=1;
    public static final int DOWNLOAD_STATE_END=2;

    public static final int DOWNLOAD_FLAG_CORRECT=0;
    public static final int DOWNLOAD_FLAG_CONNECTION_ERROR=1;
    public static final int DOWNLOAD_FLAG_API_ERROR=2;

    public static final int PAGE_SIZE_TYPE1=50;
    public static final int PAGE_SIZE_TYPE2=10;

    public static final String PAGE="&page=";

    public static final String SEARCH_KEY="search_key";
    public static final String SEARCH_TYPE="search_type";
    public static final String CAST_KEY="cast_key";


    public static final int  SEARCH_TYPE_MOVIE=0;
    public static final int SEARCH_TYPE_SIMILAR=1;
    public static final int SEARCH_TYPE_FAVORITES=2;
    public static final String SEARCH_SIMILAR_REQUEST="search_similar_request";


    public static final String COUNTRY_TEXT ="&country=";
    public static final String PAGE_LIMIT="&page_limit=";
    public static final String LIMIT="&limit=";
    public static String COUNTRY_SELECTED="us";
    public static int selected=0;


    public static final String MOVIE_ARRAY="movie_array";
    public static final String MOVIE_OBJECT="movie_object";
    public static final String POSITION="position";





}
