package oguzhan.spotify.webapi.RateyourSong;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by oguzhanaydin on 1.08.2017.
 */

public class TrackID extends Application {
    private String Trackid;
    private int Tracksize;
public static String UseridInfo;
    public static int oySonucu;

    public void setTracksize(int Trackssize) {
        this.Tracksize = Tracksize;
    }

    public int getTracksize() {
        return Tracksize;
    }


    public void setTrackid(String Trackid) {
        this.Trackid = Trackid;
    }

    public String getTrackid() {
        return Trackid;
    }


}
