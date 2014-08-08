package tomatoes.rotten.erkanerol.refactor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import BackEnd.Movie;

public class MovieListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    Context context;
    ArrayList<Movie> movies;

    public MovieListAdapter(Context context, ArrayList<Movie> movies) {
        super();
        this.movies=movies;
        this.context = context;
        mInflater= (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return movies.indexOf(getItem(position));
    }

    private class ViewHolder {
        ImageView poster;
        TextView title;
        TextView score;
        TextView cast;
        TextView mpaa;
        TextView duration;
        TextView synopsis;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_short_movie, null);
            holder = new ViewHolder();
            holder.poster = (ImageView) convertView.findViewById(R.id.poster);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.score = (TextView) convertView.findViewById(R.id.score);
            holder.cast= (TextView) convertView.findViewById(R.id.cast);
            holder.mpaa= (TextView) convertView.findViewById(R.id.mpaa);
            holder.duration= (TextView) convertView.findViewById(R.id.duration);
            holder.synopsis= (TextView) convertView.findViewById(R.id.shortSynopsis);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Movie movie = (Movie) getItem(position);

        holder.title.setText(movie.title+"  ("+movie.release_dates.theater+")");


        if(movie.ratings.critics_score!=null && !movie.ratings.critics_score.equals("-1")){
            holder.score.setText( "Ratings:"+movie.ratings.critics_score);
        }
        else  if(movie.ratings.audience_score!=null && !movie.ratings.audience_score.equals("-1")){
            holder.score.setText( "Ratings:"+movie.ratings.audience_score);
        }




        if(movie.actors!=null && movie.actors.length>1){
            holder.cast.setText(movie.actors[0].name+","+movie.actors[1].name);
        }
        else if(movie.actors!=null && movie.actors.length>0){
            holder.cast.setText(movie.actors[0].name);
        }

        if(movie.mpaa_rating!=null && !movie.mpaa_rating.equals("Unrated")){
            holder.mpaa.setText("MPAA: "+movie.mpaa_rating);
        }

        if(movie.runtime!=null && !"".equals(movie.runtime)){
            holder.duration.setText(movie.runtime);
        }
        Picasso.with(context)
                .load(movie.posters.original)
                .placeholder(R.drawable.poster_default)
                .into(holder.poster);

        if(!"".equals(movie.synopsis)){
            holder.synopsis.setText(movie.synopsis);
        }
        return convertView;
    }
}
