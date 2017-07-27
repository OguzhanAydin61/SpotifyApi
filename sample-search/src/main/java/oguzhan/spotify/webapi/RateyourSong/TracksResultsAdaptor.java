package oguzhan.spotify.webapi.RateyourSong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.Image;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URL;
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
    private int oylama;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference yaz = db.getReference();
    private int oy;

    public TracksResultsAdaptor(Context context, ArrayList<Tracks> tracks) {


        super(context, 0, tracks);
        this.context = context;
        this.tracks = tracks;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.oylama_list_view, null);

            holder = new ViewHolder();
            holder.tracksName = (TextView) convertView.findViewById(R.id.SongName);
            holder.tracksAlbum = (TextView) convertView.findViewById(R.id.AlbumName);
            holder.tracksUrl = (TextView) convertView.findViewById(R.id.Urlname);
            holder.trackButon = (Button) convertView.findViewById(R.id.oylamaButton);
            holder.imgview = (ImageView) convertView.findViewById(R.id.resimler);
            convertView.setTag(holder);


        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        Tracks sarki = tracks.get(position);
        if (sarki != null) {

            holder.tracksName.setText(sarki.getTracksName());
            holder.tracksAlbum.setText(sarki.getTrackArtist());
            holder.trackButon.setText("oylama");
            holder.trackButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.oylamaButton) {
                        DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("Tracks").child("Oylama");

                        ValueEventListener dinle = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                               oy=dataSnapshot.getValue(int.class);
                                Log.i("Oylama Bilgisi", "Oy: " + oy);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        };
                         oku.addValueEventListener(dinle);
                        oy++;
                        yaz.child("Tracks").child("Oylama").setValue(oy);

                    }
                }
            });
            // holder.imgview.setImageURI(sarki.getImageUri());
            Picasso.with(context).load(sarki.getImageUri()).into(holder.imgview);


        }
        return convertView;
    }

    private static class ViewHolder {
        TextView tracksName;
        TextView tracksAlbum;
        TextView tracksUrl;
        Button trackButon;
        ImageView imgview;

    }

}
