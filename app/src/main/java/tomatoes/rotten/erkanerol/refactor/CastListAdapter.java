package tomatoes.rotten.erkanerol.refactor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import BackEnd.Actors;

/**
 * Created by erkanerol on 8/8/14.
 */
public class CastListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Actors> actors;
    LayoutInflater mInflater;

    public CastListAdapter(Context context,ArrayList<Actors> actors){
        this.context=context;
        this.actors=actors;
        mInflater= (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return actors.size();
    }

    @Override
    public Object getItem(int position) {
        return actors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return actors.indexOf(getItem(position));
    }

    private class ViewHolder {
        TextView name;
        TextView characters;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layaut_single_cast, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.actorName);
            holder.characters = (TextView) convertView.findViewById(R.id.character);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Actors actor= (Actors) getItem(position);

        holder.name.setText(actor.name);
        holder.characters.setText(actor.characters);


        return convertView;
    }
}
