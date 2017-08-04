package oguzhan.spotify.webapi.RateyourSong;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

/**
 * Created by oguzhanaydin on 24.07.2017.
 */

public class FirebaseTracks {
    private int oySayisi;
    private int holdTrackvalue = 0;
    private static boolean control;
    private int Trackid;
    private String TracksUrl;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference yaz = db.getReference();
    static int oy;
    static long flag;

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

    public void isTime() {








    }

    public void Trackplay(final int Tracksize, final String trID) {


        Log.d("trID:", "" + trID);

        DatabaseReference oku2 = FirebaseDatabase.getInstance().getReference().child("Users").child(trID);

        ValueEventListener dinle2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("data child count ", "" + dataSnapshot.getChildrenCount());


                Log.d("flag status: ", "" + dataSnapshot.getChildrenCount());


                if (dataSnapshot.getChildrenCount() < 3) {
                    yaz.child("Users").child(trID).child("id").setValue(Tracksize);
                    DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("Tracks").child("" + Tracksize).child("oylama");
                    ValueEventListener listen = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            oy = dataSnapshot.getValue(int.class);
                            Log.i("Oylama Bilgisi", "Oy: " + dataSnapshot.getValue(int.class));
                            TrackID.oySonucu = oy;
                            Log.d("sonOy: ", "" + oy);
                            oy++;
                            yaz.child("Tracks").child("" + Tracksize).child("oylama").setValue(oy);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    oku.addListenerForSingleValueEvent(listen);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        oku2.addListenerForSingleValueEvent(dinle2);


    }

    public void UseridRemove(final int Tracksize, final String trID) {

        DatabaseReference oku2 = FirebaseDatabase.getInstance().getReference().child("Users").child(trID);

        ValueEventListener dinle = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        };
    }
public void PlayMusic(){

}

}
