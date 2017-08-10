package oguzhan.spotify.webapi.RateyourSong;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.spotify.sdk.android.player.PlaybackState;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Connectivity;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;
import com.spotify.sdk.android.player.Player;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spotify.sdk.android.player.Metadata;

import oguzhan.spotify.webapi.android.SpotifyApi;
import oguzhan.spotify.webapi.android.SpotifyCallback;
import oguzhan.spotify.webapi.android.SpotifyError;
import oguzhan.spotify.webapi.android.SpotifyService;
import oguzhan.spotify.webapi.android.models.Pager;
import oguzhan.spotify.webapi.android.models.Playlist;
import oguzhan.spotify.webapi.android.models.PlaylistTrack;
import oguzhan.spotify.webapi.android.models.Track;
import oguzhan.spotify.webapi.android.models.UserPublic;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Headers;

public class Main2Activity extends AppCompatActivity implements Player.NotificationCallback, ConnectionStateCallback {
    public static final String TAG = "Main2Activity";
    private ArrayList<Tracks> tracks;
    private ListView listView;
    private String image;
    private final List<Track> mItems = new ArrayList<>();
    private TracksResultsAdaptor listViewAdapter;
    private Button exit;
    private Tracks sarkilar = new Tracks();
    private BroadcastReceiver mNetworkStateReceiver;
    private static int holdTrackvalue = 0;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference yaz = db.getReference();
    private MediaPlayer mediaPlayer;
    private static String nowPlay = " ";
    private Context mContext;
    final GenericTypeIndicator<List<Tracks>> t = new GenericTypeIndicator<List<Tracks>>() {
    };
    private static int sayac;
    private String usr;
    private int size;
    private static String hold;
    private static String holdUserid;
    private List<Tracks> tracksList;

    private FirebaseTracks deneme = new FirebaseTracks();
    private int ara;
    private String token;
    private Metadata mMetadata;
    private static final String CLIENT_ID = "ee6b609c51e94cd584dcbdf1d3d070b7";
    public static long difference;
    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";
    private SpotifyService mService;
    private PlaybackState mCurrentPlaybackState;
    private SpotifyPlayer mPlayer;
    private String playlistURl;
    private static int tracksizeHold;
    private static boolean bayrak;
    private static boolean flagControlPlayer = false;
    private static int cik = 0;

    private static long durationTime;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        token = intent.getStringExtra(EXTRA_TOKEN);
        Log.d("Got token 2: ", token);
        mContext = getApplicationContext();
        exit = (Button) findViewById(R.id.Exitbutton);
        usr = intent.getStringExtra("isim");
        holdUserid = usr;
        Log.d("isimStart ", usr);
        SpotifyApi spotifyApi = new SpotifyApi();
        spotifyApi.setAccessToken(token);
        mService = spotifyApi.getService();

        // Playlist weList=new Playlist();
        //spotify:user:spotify:playlist:37i9dQZEVXbhi5krBnMhMV
        //spotify:track:0R0JMH92qc5mepN2if6lhU
        play(token);

        bayrak = false;
        playlistURl = "37i9dQZF1DXd8Yptw1g5FC";
        mService.getUser(usr, new Callback<UserPublic>() {
            @Override
            public void success(UserPublic userPublic, Response response) {
                Log.d(TAG, "user info is come: " + userPublic.display_name);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "not come user info");
            }
        });

        //  spotify:user:spotify:playlist:37i9dQZF1DXdnOj1VEuhgb
        mService.getPlaylist("spotify", playlistURl, new SpotifyCallback<Playlist>() {
            @Override
            public void failure(SpotifyError error) {
                Log.e(TAG, "Hata" + error.getMessage() + "  " + error.getLocalizedMessage());

            }

            @Override
            public void success(Playlist playlist, Response response) {
                //playlist.tracks.items.get(0).track.album.images.get(0);

                for (int i = 0; i < playlist.tracks.items.size(); i++)
                    Log.d("sarkilarimiz", "success: " + playlist.tracks.items.get(i).track.external_urls);

                Map<Integer, Track> plyHold = new HashMap<>();
                Log.d("Size", "before size");
                size = playlist.tracks.items.size();
                Log.d("Size", "after size " + size);
                sarkilar.setTracksSize(size);

                for (int i = 0; i < size; i++) {


                    plyHold.put(i, playlist.tracks.items.get(i).track);


                }
                // String muzik= playlist.tracks.items.get(0).track.preview_url.toString();
                //mPlayer.play(muzik);
                Log.e("item external: ", playlist.tracks.items.get(0).track.external_urls.toString());
                initialize();
                fillArrayList(tracks, plyHold);

            }
        });


        mNetworkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mPlayer != null) {
                    Connectivity connectivity = getNetworkConnectivity(getBaseContext());
                    mPlayer.setConnectivityStatus(mOperationCallback, connectivity);

                }
            }
        };

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkStateReceiver, filter);
        if (mPlayer != null) {
            mPlayer.addNotificationCallback(Main2Activity.this);
            mPlayer.addConnectionStateCallback(Main2Activity.this);
        }
        boolean playing = (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying);
        Log.i("Playing status: ", "Song: " + playing);
        deneme.setFlag(false);
        DatabaseReference read = FirebaseDatabase.getInstance().getReference().child("Size");
        ValueEventListener readListen = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int SIZE = dataSnapshot.getValue(int.class);
                Log.d("Size first play", " " + SIZE);

                playSong(SIZE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        read.addListenerForSingleValueEvent(readListen);


    }


    @Override
    protected void onPause() {
        super.onPause();

    }


    public void playSong(final int OylamaSize) {

        Log.d("Girdi", "playSong");
        Log.d("Girdi2 ", "size " + OylamaSize);


        DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("Tracks");

        ValueEventListener dinliyorum = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tracksList = dataSnapshot.getValue(t);

                for (int i = 0; i < OylamaSize; i++) {
                    final int j = i;
                    if (holdTrackvalue < dataSnapshot.child("" + i).child("oylama").getValue(int.class)) {
                        holdTrackvalue = dataSnapshot.child("" + i).child("oylama").getValue(int.class);
                        Log.d("Oylama sonuclari: ", holdTrackvalue + " adet oy");

                        Log.d("jDeger: ", "" + j);
                        sarkilar.setTrackid(j);
                        Log.d("getTrackid degeri", " " + sarkilar.getTrackid());
                        sarkilar.setTracksUrl(dataSnapshot.child("" + j).child("tracksUrl").getValue(String.class));
                        Log.d("Caliyor...", "Track: " + dataSnapshot.child("" + j).child("tracksUrl").getValue(String.class));
                        hold = dataSnapshot.child("" + j).child("tracksUrl").getValue(String.class);
                        sarkilar.setTracksUrl(hold);
                        durationTime = dataSnapshot.child("" + j).child("durationTime").getValue(long.class);
                        bayrak = true;

                    }

                }
                Log.d("bayrakCalisiyor", "bayrak");
                Log.d("bayrak", "" + bayrak);
                if (bayrak) {
                    Log.d("bayrakCalisiyor", "bayrak");


                    zaman(OylamaSize);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        oku.addListenerForSingleValueEvent(dinliyorum);
        Log.d("bayrak2", " " + bayrak);


    }

    private void zaman(final int sizeINFO) {

        final long hold2;
        DatabaseReference oku4 = FirebaseDatabase.getInstance().getReference().child("Time");
        Log.d("Time", "Girdi");
        ValueEventListener timeListen = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot2) {
                Date endTime2 = Calendar.getInstance().getTime();
                Log.d("dataSonuc",""+dataSnapshot2.hasChild("seekTime"));
                if (!dataSnapshot2.hasChild("seekTime"))
                {  //Log.d("SongRemove:", ""+dataSnapshot2.child("seekTime").getValue(long.class)+ durationTime+"  "+endTime2.getTime());
                   // if (dataSnapshot2.child("seekTime").getValue(long.class)+ durationTime <  (endTime2.getTime())) {

                        Log.d("Song", "Remove");

                      }



                Log.d("playsong sayac2", " " + sayac++);
                Log.d("timeChildCount", "" + dataSnapshot2.getChildrenCount());

                if (dataSnapshot2.getChildrenCount() < 1) {
                    Date minute = Calendar.getInstance().getTime();
                    yaz.child("Time").child("seekTime").setValue(minute.getTime());
                    Log.d("durationTime", "" + durationTime);
                    yaz.child("Time").child("durationTime").setValue(durationTime);

                    difference = 0;
                    Log.d("controll is ", "null");


                } else {

                    Date endTime = Calendar.getInstance().getTime();
                    long start = dataSnapshot2.child("seekTime").getValue(long.class);
                    long endtimer = endTime.getTime();
                    difference = endtimer - start;

                    Log.d("startTime", "" + start);
                    Log.d("startendTime", "" + endtimer);
                    long diffsecond = difference / 1000 % 60;
                    long differenceMinutes = difference / (60 * 1000) % 60;
                    //  difference = (differenceMinutes * 60) + diffsecond;
                    Log.d("controll is ", "not null minute" + differenceMinutes + " second " + diffsecond);
                }

                Log.d("seekTime", "" + difference);
                Log.d("seektime then uri", " " + hold);
                playerSet(difference, durationTime,sizeINFO);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        oku4.addListenerForSingleValueEvent(timeListen);

    }

    private void playerSet(final long difference2, final long sarkiTime,final int sizeINFO) {
        Log.d("GelenDiffTime", "" + difference2);
        Log.d("GelensarkiTİme", "" + sarkiTime);
        if (difference2 > sarkiTime) {

        }
        DatabaseReference readTosong = FirebaseDatabase.getInstance().getReference().child("Song");
        ValueEventListener listenToSong = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nowPlay = dataSnapshot.child("Track").getValue(String.class);
                if (null == dataSnapshot.child("Track").getValue(String.class)) {
                    deneme.VoteRemove(tracksizeHold);
                    Log.d("if null", "hold:" + hold + " data:" + dataSnapshot.child("Track").getValue(String.class));
                    if (dataSnapshot.getChildrenCount() < 1) {

                        Log.d("yazilanSong", " " + hold);
                        yaz.child("Song").child("Track").setValue(hold);
                        mPlayer.playUri(mOperationCallback, hold, 0, 0);
                        Log.d("difference2", " " + difference2);
                        if (difference2 > 0)
                            mPlayer.seekToPosition(mOperationCallback, (int) difference2);


                    } else {
                        Log.d("difference2", " " + difference2);

                    }
                } else if (dataSnapshot.child("Track").getValue(String.class) == hold) {
                    Log.d("büyükElseIF ==:", "hold:" + hold + " data:" + dataSnapshot.child("Track").getValue(String.class));
                    mPlayer.playUri(mOperationCallback, dataSnapshot.child("Track").getValue(String.class), 0, 0);
                    if (difference2 > 0)
                        mPlayer.seekToPosition(mOperationCallback, (int) difference2);
                } else if (dataSnapshot.child("Track").getValue(String.class) != hold) {
                    if (difference2 < sarkiTime) {
                        Log.d("büyükElseIf !=:", "hold:" + hold + " data:" + dataSnapshot.child("Track").getValue(String.class));
                        mPlayer.playUri(mOperationCallback, dataSnapshot.child("Track").getValue(String.class), 0, 0);
                        if (difference2 > 0)
                            mPlayer.seekToPosition(mOperationCallback, (int) difference2);
                    } else if (difference2 > sarkiTime) {
                        Toast.makeText(mContext, "Zaman tutarsizligi", Toast.LENGTH_SHORT).show();

                    }


                }

                listViewAdapter.setIsEnable(true);
               if( mCurrentPlaybackState!=null)
                   if(mCurrentPlaybackState.isPlaying)
                       playSong(sizeINFO);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        readTosong.addListenerForSingleValueEvent(listenToSong);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Resume ", "Girdi");


    }


    private Connectivity getNetworkConnectivity(Context context) {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return Connectivity.fromNetworkType(activeNetwork.getType());
        } else {
            return Connectivity.OFFLINE;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart", "Girdi");


    }

    public void play(final String myToken) {
        if (mPlayer == null) {
            final Config playerConfig = new Config(getApplicationContext(), myToken, CLIENT_ID);
            mPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                @Override
                public void onInitialized(SpotifyPlayer player) {
                    mPlayer.setConnectivityStatus(mOperationCallback, getNetworkConnectivity(Main2Activity.this));
                    // Trigger UI refresh
                    Log.e("Test  display name: ", playerConfig.displayName);
                    Log.e("Test brandname", playerConfig.brandName);
                    Log.e("Test uniqueID", playerConfig.uniqueId);
                    Log.e("Test getaccessToken", myToken);
                }

                @Override
                public void onError(Throwable error) {
                }
            });
        } else {
            mPlayer.login(myToken);
        }


    }

    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            Log.d("Player Giriş :", "Başarılı");

        }

        @Override
        public void onError(Error error) {
            Log.d("Player Giriş :", "Başarısız " + error);
        }
    };

    private boolean isLoggedIn() {
        return mPlayer != null && mPlayer.isLoggedIn();
    }

    public void onCikisButton(View view) {

        Log.d("cikisButton", "girdi");

        // mPlayer.playUri(mOperationCallback, "spotify:track:1CgUldlwNfY2JQY3sTEwPn", 0, 0);
        AuthenticationClient.clearCookies(this);
        Intent intent = new Intent(this, LoginActivity.class);
        String tut = null;
        intent.putExtra("Token", tut);
        if (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying) {
            Log.d("song Time: ", "" + mCurrentPlaybackState.positionMs);
        }

        if (!isLoggedIn()) {

        } else {
            mPlayer.logout();
        }
        startActivity(intent);
        finish();


    }

    public static Intent createIntent(Context context) {
        return new Intent(context, Main2Activity.class);
    }

    private void initialize() {
        tracks = new ArrayList<Tracks>();
        listView = (ListView) findViewById(R.id.tracks_list_view);
        listViewAdapter = new TracksResultsAdaptor(Main2Activity.this, tracks);
        listView.setAdapter(listViewAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onStop", "Girdi");
        Log.d("isFinishing", " Girmedi");
        if (isFinishing()) {
            Log.d("isFinishing", " Girdi holdUserid: " + holdUserid);
            DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("Users").child("" + holdUserid);
            oku.removeValue();
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("onDestroy", " Girdi");
        unregisterReceiver(mNetworkStateReceiver);
        if (mPlayer != null) {
            mPlayer.removeNotificationCallback(Main2Activity.this);
            mPlayer.removeConnectionStateCallback(Main2Activity.this);
        }
        Spotify.destroyPlayer(this);

        super.onDestroy();
    }

    private void fillArrayList(ArrayList<Tracks> tracks, Map<Integer, Track> plyHold) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference();
        tracks.clear();
        tracksizeHold = plyHold.size();
        for (int index = 0; index < tracksizeHold; index++) {//döngü şarkı size bağlı
            Log.d("plyhold: ", plyHold.get(index).album.images.get(0).url);

            image = (plyHold.get(index).album.images.get(0).url);
            String hold = plyHold.get(index).external_urls.get("spotify");//track url eldesi(player için)
            String hold2 = hold.substring(31);
            String spotifyURL = "spotify:track:" + hold2;
            // tracks.get(index).setTrackid(index);

            long second = plyHold.get(index).duration_ms / 1000 % 60;
            long Minutes = plyHold.get(index).duration_ms / (60 * 1000) % 60;

            Tracks sarki = new Tracks(spotifyURL, plyHold.get(index).name, plyHold.get(index).artists.get(0).name, image, 1, index, plyHold.get(index).duration_ms);//içini dolduruyoruz
            tracks.add(sarki);
            //   dbref.child("Tracks").child(index + "").setValue(sarki);
            //     dbref.child("Size").setValue(plyHold.size());
        }


        listViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPlaybackEvent(PlayerEvent event) {
        mCurrentPlaybackState = mPlayer.getPlaybackState();
        mMetadata = mPlayer.getMetadata();
        Log.i("PlaybackEvent", "Player state: " + mCurrentPlaybackState);

        if (mCurrentPlaybackState.isPlaying && mCurrentPlaybackState != null) {
            Log.d("Caliyorrrrrrrrrrrrr", "if içinde");
            deneme.setFlag(true);
        } else if (!mCurrentPlaybackState.isPlaying) {

            Log.d("Caliyorrrrrrrrrrrrr", "if dışında");
            Log.d("playSong", "flagControlPlayer" + deneme.getFlag());
            if (deneme.getFlag()) {

                cik = 0;
                Log.d("flagControl", "True");

                holdTrackvalue = 0;

                flagControlPlayer = false;
                controlseekTime(durationTime);
                deneme.setFlag(false);
                bayrak = false;
                deneme.UserVoteRemove(holdUserid);

                playSong(tracksizeHold);


                Log.d("playSong", "tracksizehold" + tracksizeHold);

            }

//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        }
        Log.e("PlaybackEvent", "Metadata: " + mMetadata);

    }

    @Override
    public void onPlaybackError(Error error) {
        Log.e("onPlaybackError: ", "Hata: " + error);
    }


    @Override
    public void onLoggedIn() {

    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Error error) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    private void controlseekTime(final long time) {
        if (mCurrentPlaybackState != null) {
            if (!mCurrentPlaybackState.isPlaying) {
                Log.d("onResume", "başladı");
                DatabaseReference oku4 = FirebaseDatabase.getInstance().getReference().child("Time");
                ValueEventListener dinle = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getChildrenCount() > 0) {
                            Date endTime = Calendar.getInstance().getTime();
                            long difHold= endTime.getTime();
                            long gelenTime=dataSnapshot.child("durationTime").getValue(long.class);
                            long dif = (dataSnapshot.child("seekTime").getValue(long.class) );//bir onceki sarki suresi=nowPlay
                            long diffsecond = dif / 1000 % 60;
                            long differenceMinutes = dif / (60 * 1000) % 60;
                            Log.d("seekTİmeCOntrol:", "" + (diffsecond + (differenceMinutes * 60)) + " zaman farkı:" + (difHold ) + " zaman:" + ((gelenTime)+ dif));

                            if (dataSnapshot.child("seekTime").getValue(long.class) == null) {
                            } else if ((gelenTime+ dif) <= (difHold)) {
                                deneme.RemoveSeekTime();
                                deneme.SongRemove();
                                Log.d("onResume", "değişiyor");

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                oku4.addListenerForSingleValueEvent(dinle);


            }
        }

    }
}
