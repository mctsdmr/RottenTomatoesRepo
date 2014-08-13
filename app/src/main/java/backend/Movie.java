package backend;

import java.io.Serializable;

/**
 * Created by erkanerol on 7/25/14.
 */
public class Movie implements Serializable {

    private static final long serialVersionUID = -4570328251649313985L;

    public Movie(){
        this.release_dates=new ReleaseDates();
        this.ratings=new Ratings();
        this.posters=new Posters();
        this.links=new Links();
        this.alternate_ids=new AlternateIds();
    }

    public String id;
    public String title;
    public String year;
    public String genres;
    public String mpaa_rating;
    public String runtime;
    public String critics_consensus;
    public ReleaseDates release_dates;
    public Ratings ratings;
    public String synopsis;
    public String studio;
    public String directors;
    public Posters posters;
    public Actors[] actors;
    public AlternateIds alternate_ids;
    public Links links;


}


