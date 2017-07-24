package oguzhan.spotify.webapi.RateyourSong;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import oguzhan.spotify.webapi.android.models.Track;

/**
 * Created by oguzhanaydin on 24.07.2017.
 */

public class TracksResultsAdaptor extends ArrayAdapter<Tracks> {
    private final LayoutInflater inflater;
    private final Context context;
    private ViewHolder holder;
    private final ArrayList<Tracks> tracks;
    public TracksResultsAdaptor(Context context, ArrayList<Tracks> tracks){


        super(context,0, tracks);
        this.context = context;
        this.tracks = tracks;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount(){
        return tracks.size();
    }
    @Override
    public Tracks getItem(int position) {
        return tracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tracks.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.oylama_list_view, null);

            holder = new ViewHolder();
             holder.tracksName = (TextView) convertView.findViewById(R.id.baslik);
            holder.tracksArtist = (TextView) convertView.findViewById(R.id.altbaslik);
            holder.trackButon=(Button)convertView.findViewById(R.id.oylamaButton);
            holder.imgview=(ImageView)convertView.findViewById(R.id.resimler);
            convertView.setTag(holder);

        }
        else{

            holder = (ViewHolder)convertView.getTag();
        }

        Tracks sarki = tracks.get(position);
        if(sarki != null){
            holder.tracksName.setText(sarki.getTracksName());
            holder.tracksArtist.setText(sarki.getTrackArtist());
//holder.imgview.setImageURI(sarki.);
        }
        return convertView;
    }
    private static class ViewHolder implements View.OnClickListener{
        TextView tracksName;
        TextView tracksArtist;
        Button trackButon;
        ImageView imgview;

        @Override
        public void onClick(View v) {
            if(v.getId()==trackButon.getId()){

            }
        }
    }

}
