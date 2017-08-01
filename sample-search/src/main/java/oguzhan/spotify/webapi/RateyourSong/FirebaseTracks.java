package oguzhan.spotify.webapi.RateyourSong;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oguzhanaydin on 24.07.2017.
 */

public class FirebaseTracks {
    private int oySayisi;
  private int holdTrackvalue=0;
    private boolean flag;
    private int Trackid;
    private String TracksUrl;

    public void setTrackid(int Trackid){
        this.Trackid=Trackid;

    }
    public int getTrackid(){
        return Trackid;
    }

    public void setTracksUrl(String TracksUrl) {
        this.TracksUrl = TracksUrl;
    }

    public String getTracksUrl() {

        return TracksUrl;
    }


    public String Trackplay(int Tracksize) {

        for (int i = 0; i < Tracksize; i++) {
            Log.d("Trackplay", "Working +" + i);
            flag = false;
            DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("Tracks").child("id:" + i).child("Oylama");

            ValueEventListener dinle = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (holdTrackvalue < dataSnapshot.getValue(int.class)) {
                        holdTrackvalue = dataSnapshot.getValue(int.class);
                        Log.i("Oylama sonuclari: ", holdTrackvalue + " adet oy");

                        flag = true;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            oku.addValueEventListener(dinle);
            if(flag){
                Log.d("Track indis","indis: "+i);
                setTrackid(i);
            }
        }
        Log.d("getTrackid", " " + getTrackid());
        DatabaseReference oku2 = FirebaseDatabase.getInstance().getReference().child("Tracks").child("id:" + 11).child("Track");

        ValueEventListener dinle2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Caliyor...", "Track: " + dataSnapshot.getValue(String.class));
                setTracksUrl(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        oku2.addValueEventListener(dinle2);

        Log.d("donen sarki",getTracksUrl());
        return getTracksUrl();
    }

}
