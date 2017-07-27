package oguzhan.spotify.webapi.RateyourSong;

import android.media.Image;
import android.net.Uri;
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

public class Tracks {

    private String TracksUrl;
    private String TracksName;
    private String TrackArtist;
    private Uri imageURL;
    private int oylama;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    //private DatabaseReference myRef = db.getReference();
//    private static final String TAG = Tracks.class.getSimpleName();

    public void setTracksUrl(String TracksUrl) {
        this.TracksUrl = TracksUrl;
    }

    public void setTracksName(String TracksName) {
        this.TracksName = TracksName;
    }

    public void setTrackArtist(String TrackArtist) {
        this.TrackArtist = TrackArtist;

    }

    public Tracks(String TracksUrl, String TracksName, String TrackArtist, Uri imageURL) {

        this.TrackArtist = TrackArtist;
        this.TracksUrl = TracksUrl;
        this.TracksName = TracksName;
        this.imageURL = imageURL;

    }

    public void setOylama(int oylama) {
        this.oylama = oylama;
    }

    public int getOylama() {

        return oylama;
    }

    public void setImageUri(Uri imageURL) {
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

    public Uri getImageUri() {
        return imageURL;
    }


}
