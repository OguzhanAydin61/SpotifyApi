package oguzhan.spotify.webapi.RateyourSong;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oguzhanaydin on 24.07.2017.
 */

public class FirebaseTracks {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("Tracks");
    private int oySayisi;
    Map<String,Integer>sarkilar=new HashMap<String,Integer>();

public void setOySayisi(Map<String,Integer> sarkilar) {
    sarkilar = new HashMap<String, Integer>();

}

}
