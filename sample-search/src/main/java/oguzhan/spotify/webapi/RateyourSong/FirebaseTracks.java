package oguzhan.spotify.webapi.RateyourSong;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by oguzhanaydin on 24.07.2017.
 */

public class FirebaseTracks {
    private int oySayisi;
    private int holdTrackvalue = 0;
    private boolean flag;

    private int Trackid;
    private String TracksUrl;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference yaz = db.getReference();
    static int oy;


    public void setTrackid(int Trackid) {
        this.Trackid = Trackid;

    }

    public int getTrackid() {
        return Trackid;
    }

    public void setTracksUrl(String TracksUrl) {
        this.TracksUrl = TracksUrl;
    }

    public String getTracksUrl() {

        return TracksUrl;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean getFlag() {
        return flag;
    }

    public void Trackplay(final int Tracksvoteid, final String trID) {


        Log.d("trID:", "" + trID);


        yaz.child("Users").child(trID).child("id").setValue(Tracksvoteid);
        DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("Tracks").child("" + Tracksvoteid).child("oylama");
        ValueEventListener listen = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                oy = dataSnapshot.getValue(int.class);
                Log.i("Oylama Bilgisi", "Oy: " + dataSnapshot.getValue(int.class));
                TrackID.oySonucu = oy;
                Log.d("sonOy: ", "" + oy);
                oy++;
                yaz.child("Tracks").child("" + Tracksvoteid).child("oylama").setValue(oy);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        oku.addListenerForSingleValueEvent(listen);


    }

    public void SongRemove() {

        DatabaseReference oku4 = FirebaseDatabase.getInstance().getReference().child("Song").child("Track");
        oku4.removeValue();
    }

    public void VoteRemove(int Tracksize) {
        for (int i = 0; i < Tracksize; i++) {
            DatabaseReference oku2 = FirebaseDatabase.getInstance().getReference().child("Tracks").child("" + i).child("oylama");
            oku2.setValue(1);
        }
    }

    public void UserVoteRemove(String usrID) {
        DatabaseReference oku2 = FirebaseDatabase.getInstance().getReference().child("Users").child(usrID).child("id");
        oku2.removeValue();
    }

    public void RemoveSeekTime() {

        DatabaseReference oku4 = FirebaseDatabase.getInstance().getReference().child("Time");

        oku4.removeValue();
    }

    public void ControlSeekTime() {

        DatabaseReference oku4 = FirebaseDatabase.getInstance().getReference().child("Time");
        ValueEventListener dinle = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int oy = dataSnapshot.child("write").getValue(int.class);
                if (oy > 0) {
                    oy--;
                } else {
                    RemoveSeekTime();
                    SongRemove();

                }

                yaz.child("Time").child("write").setValue(oy);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        oku4.addListenerForSingleValueEvent(dinle);
    }

}
