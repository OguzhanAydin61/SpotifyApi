package oguzhan.spotify.webapi.RateyourSong;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.Toast;

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
    private String track;
    private int Buttonname = 0;
    private Context mContext = getContext();
    public static boolean isEnable = true;

    public TracksResultsAdaptor(Context context, ArrayList<Tracks> tracks) {


        super(context, 0, tracks);
        this.context = context;
        this.tracks = tracks;
        inflater = LayoutInflater.from(context);
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
      //  notifyDataSetChanged();
    }

    public boolean getIsEnable() {
        return isEnable;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.oylama_list_view, null);

            holder = new ViewHolder();
            holder.tracksName = (TextView) convertView.findViewById(R.id.SongName);
            holder.tracksAlbum = (TextView) convertView.findViewById(R.id.AlbumName);
            //  holder.trackOy = (TextView) convertView.findViewById(R.id.OyShow);
            holder.trackButon = (Button) convertView.findViewById(R.id.oylamaButton);
            holder.imgview = (ImageView) convertView.findViewById(R.id.resimler);

            convertView.setTag(holder);


        } else {

            holder = (ViewHolder) convertView.getTag();

        }
       //  holder.trackButon.setEnabled(isEnable);

        Tracks sarki = tracks.get(position);
        Log.d("position info: ", " " + tracks.get(position));
        if (sarki != null) {

            holder.tracksName.setText(sarki.getTracksName());
            holder.tracksAlbum.setText(sarki.getTrackArtist());
//            holder.trackOy.setText(sarki.getOylama());
            holder.trackButon.setText("oylama: ");
            Log.d("getImage: ", sarki.getImageUri());
            if (sarki.getImageUri() == null) {

            } else {
                holder.imgview.setImageURI(Uri.parse(sarki.getImageUri()));
                Picasso.with(context).load(sarki.getImageUri()).into(holder.imgview);
            }
            holder.trackButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                      if (isEnable) {
                    FirebaseTracks oyla = new FirebaseTracks();
                    String f = LoginActivity.tr;
                    Log.d("fDegeri", "" + f);
//                    String deneme = "songulaydin61";
//                    final String trID = deneme;
//
                    if (v.getId() == R.id.oylamaButton) {
                        Log.d("track.getPosition", " " + tracks.get(position));
                        Log.d("position info click: ", " " + tracks.get(position).getTrackid());
                        int hold = tracks.get(position).getTrackid();

                        oyla.Trackplay(hold, f);
                        int al = TrackID.oySonucu;
                        //    holder.trackButon.setBackgroundColor(Color.WHITE);
                       //  holder.trackButon.setEnabled(false);
                    }

                  isEnable = false;
//                    notifyDataSetChanged();
                }else {Toast.makeText(mContext,"Zaten Oy verdiniz ",Toast.LENGTH_SHORT).show();
                          //TODO mesaj göster oy kullandınız
                            }

                      }
            });


        }
        return convertView;
    }

    private static class ViewHolder {
        TextView tracksName;
        TextView tracksAlbum;
        TextView trackOy;
        Button trackButon;
        ImageView imgview;


    }

}
