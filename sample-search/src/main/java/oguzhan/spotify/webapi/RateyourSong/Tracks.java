package oguzhan.spotify.webapi.RateyourSong;

import android.media.Image;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;

/**
 * Created by oguzhanaydin on 24.07.2017.
 */

public class Tracks implements Parcelable {
    private int TracksSize;
    private int Trackid;
    private String TracksUrl;
    private String TracksName;
    private String TrackArtist;

    private String imageURL;
    private int holdTrackvalue = 0;
    private int j = 0;
    private String hold;
    private int oylama;
    private boolean flag = false;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    //private DatabaseReference myRef = db.getReference();
//    private static final String TAG = Tracks.class.getSimpleName();

    protected Tracks(Parcel in) {

        TracksSize = in.readInt();
        TracksUrl = in.readString();
        TracksName = in.readString();
        TrackArtist = in.readString();
        imageURL = in.readString();
        oylama = in.readInt();

    }

    public static final Creator<Tracks> CREATOR = new Creator<Tracks>() {
        @Override
        public Tracks createFromParcel(Parcel in) {
            return new Tracks(in);
        }

        @Override
        public Tracks[] newArray(int size) {
            return new Tracks[size];
        }
    };

    public void setTracksUrl(String TracksUrl) {
        this.TracksUrl = TracksUrl;
    }


    public Tracks() {
//        TrackArtist = "Coldplay";
//        TracksUrl = "spotify:track:7HBnZdg7fIQwqMhQhci0VV";
//        TracksName = "Hypnotised - EP Mix";
//       imageURL = Uri.parse("https://i.scdn.co/image/626af44e5fe61e9ae5ddc78845301793400faf14");
//        TracksSize = 30;

    }

    public void setTracksName(String TracksName) {
        this.TracksName = TracksName;
    }

    public void setTrackArtist(String TrackArtist) {
        this.TrackArtist = TrackArtist;

    }


    public void setTrackid(int Trackid) {
        this.Trackid = Trackid;
    }

    public int getTrackid() {
        return Trackid;
    }

    public Tracks(String TracksUrl, String TracksName, String TrackArtist, String imageURL, int oylama) {

        this.TrackArtist = TrackArtist;
        this.TracksUrl = TracksUrl;
        this.TracksName = TracksName;
        this.imageURL = imageURL;
        this.oylama = oylama;

    }

    public void setTracksSize(int TracksSize) {
        this.TracksSize = TracksSize;
    }

    public int getTracksSize() {
        return TracksSize;
    }

    public void setOylama(int oylama) {
        this.oylama = oylama;
    }

    public int getOylama() {

        return oylama;
    }

    public void setImageUri(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTracksUrl() {

        return TracksUrl;
    }

    public String getTracksName() {

        return TracksName;
    }

    public String getTrackArtist() {

        return TrackArtist;
    }

    public String getImageUri() {
        return imageURL;
    }


    public int getTrackplayId(int Tracksize) {
        j = 0;

        for (int i = 0; i < Tracksize; i++) {
            Log.d("Trackplay", "Working +" + i);

            DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("Tracks").child("id:" + i).child("Oylama");

            oku.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (holdTrackvalue < dataSnapshot.getValue(int.class)) {
                        holdTrackvalue = dataSnapshot.getValue(int.class);
                        Log.d("Oylama sonuclari: ", holdTrackvalue + " adet oy");

                        Log.d("Trackid", " " + j);
                        setTrackid(j);


                    }
                    j++;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



/*
            ValueEventListener dinle = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (holdTrackvalue < dataSnapshot.getValue(int.class)) {
                        holdTrackvalue = dataSnapshot.getValue(int.class);
                        Log.d("Oylama sonuclari: ", holdTrackvalue + " adet oy");

                        Log.d("Trackid", " " + j);
                        setTrackid(j);


                    }
                    j++;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            oku.addValueEventListener(dinle);
*/
        }

        return getTrackid();


    }

    public String getTrackinFirebase(int trackid) {
        Log.d("Gelen child track id:", "" + trackid);
        Log.d("getTrackid degeri", " " + getTrackid());
        DatabaseReference oku2 = FirebaseDatabase.getInstance().getReference().child("Tracks").child("id:" + trackid).child("Track");

        ValueEventListener dinle2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Caliyor...", "Track: " + dataSnapshot.getValue(String.class));
                setTracksUrl(dataSnapshot.getValue(String.class));
                hold = dataSnapshot.getValue(String.class);
                setTracksUrl(hold);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        oku2.addValueEventListener(dinle2);
        return getTracksUrl();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(TracksSize);
        dest.writeString(TracksUrl);
        dest.writeString(TracksName);
        dest.writeString(TrackArtist);
//        dest.writeParcelable(imageURL, flags);
        dest.writeInt(oylama);
    }
}
