package oguzhan.spotify.webapi.RateyourSong;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by oguzhanaydin on 24.07.2017.
 */

public class Tracks {

    private String TracksUrl;
    private String TracksName;
    private String TrackArtist;
    private String imageURL;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();

    public void setTracksUrl(String TracksUrl){
        this.TracksUrl=TracksUrl;
    }
    public void setTracksName(String TracksName){
        this.TracksName=TracksName;
    }
    public void setTrackArtist(String TrackArtist){
        this.TrackArtist=TrackArtist;

    }
    public void setImageURL(String imageURL){
        this.imageURL=imageURL;
    }
    public String getTracksUrl(){

        return TracksUrl;
    }
    public String getTracksName(){

        return TracksName;
    }
    public String getTrackArtist(){

        return TrackArtist;
    }


}
