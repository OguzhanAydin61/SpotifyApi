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
import com.google.firebase.database.ValueEventListener;
import com.spotify.sdk.android.player.PlaybackState;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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

public class Main2Activity extends AppCompatActivity implements Player.NotificationCallback {
    public static final String TAG = "Main2Activity";
    private ArrayList<Tracks> tracks;
    private ListView listView;
    private String image;
    private final List<Track> mItems = new ArrayList<>();
    private TracksResultsAdaptor listViewAdapter;
    private Button exit;
    private Tracks sarkilar = new Tracks();
    private BroadcastReceiver mNetworkStateReceiver;
    private int holdTrackvalue = 0;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference yaz = db.getReference();
    private MediaPlayer mediaPlayer;
    private Context mContext;
    private String usr;
    private int size;
    private String hold;
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
        Log.d("isimStart ", usr);
        SpotifyApi spotifyApi = new SpotifyApi();
        spotifyApi.setAccessToken(token);
        mService = spotifyApi.getService();

        // Playlist weList=new Playlist();
        //spotify:user:spotify:playlist:37i9dQZEVXbhi5krBnMhMV
        //spotify:track:0R0JMH92qc5mepN2if6lhU
        play(token);


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


        DatabaseReference oku3 = FirebaseDatabase.getInstance().getReference().child("Size");
        oku3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Oylama size", " " + dataSnapshot.getValue(int.class));

                for (int i = 0; i < dataSnapshot.getValue(int.class); i++) {
                    final int j = i;

                    DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("Tracks").child("" + i).child("oylama");

                    oku.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (holdTrackvalue < dataSnapshot.getValue(int.class)) {
                                holdTrackvalue = dataSnapshot.getValue(int.class);
                                Log.d("Oylama sonuclari: ", holdTrackvalue + " adet oy");
                                int i1 = j;
                                Log.d("jDeger: ", "" + j);
                                sarkilar.setTrackid(i1);
                                Log.d("getTrackid degeri", " " + sarkilar.getTrackid());

                                DatabaseReference oku2 = FirebaseDatabase.getInstance().getReference().child("Tracks").child("" + sarkilar.getTrackid()).child("tracksUrl");

                                ValueEventListener dinle2 = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.d("Caliyor...", "Track: " + dataSnapshot.getValue(String.class));
                                        sarkilar.setTracksUrl(dataSnapshot.getValue(String.class));
                                        hold = dataSnapshot.getValue(String.class);
                                        sarkilar.setTracksUrl(hold);
                                        DatabaseReference oku4 = FirebaseDatabase.getInstance().getReference().child("Time");

                                        ValueEventListener timeListen = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot2) {

                                                Log.d("time child count", "" + dataSnapshot2.getChildrenCount());

                                                if (dataSnapshot2.getChildrenCount() < 1) {
                                                    Date minute = Calendar.getInstance().getTime();
                                                    yaz.child("Time").child("seekTime").setValue(minute.getTime());
                                                    difference = 0;
                                                    Log.d("controll is ", "null");

                                                } else {

                                                    Date endTime = Calendar.getInstance().getTime();
                                                    long start = dataSnapshot2.child("seekTime").getValue(long.class);
                                                    difference = endTime.getTime() - start;

                                                    long diffsecond = difference / 1000 % 60;
                                                    long differenceMinutes = difference / (60 * 1000) % 60;
                                                    difference = (differenceMinutes * 60) + diffsecond;
                                                    Log.d("controll is ", "not null minute" + differenceMinutes + " second " + diffsecond);
                                                }


                                                Log.d("seekTime", "" + difference);
                                                Log.d("seektime then uri", " " + hold);
                                                mPlayer.playUri(mOperationCallback, hold, 0, 0);
                                                try {
                                                    Thread.sleep(10);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                mPlayer.seekToPosition(mOperationCallback,1000*(int)difference);


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        };
                                        oku4.addListenerForSingleValueEvent(timeListen);




                                        Log.d("mPlayer:", "" + mPlayer.isShutdown() + " " + mPlayer.isLoggedIn());

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                };
                                oku2.addValueEventListener(dinle2);


                                Log.d("TrackUrl ", "getTrackUrl: " + sarkilar.getTracksUrl());

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void playSong() {
        DatabaseReference oku3 = FirebaseDatabase.getInstance().getReference().child("Size");
        oku3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Oylama size", " " + dataSnapshot.getValue(int.class));

                for (int i = 0; i < dataSnapshot.getValue(int.class); i++) {
                    final int j = i;

                    DatabaseReference oku = FirebaseDatabase.getInstance().getReference().child("Tracks").child("" + i).child("oylama");

                    oku.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (holdTrackvalue < dataSnapshot.getValue(int.class)) {
                                holdTrackvalue = dataSnapshot.getValue(int.class);
                                Log.d("Oylama sonuclari: ", holdTrackvalue + " adet oy");
                                int i1 = j;
                                Log.d("jDeger: ", "" + j);
                                sarkilar.setTrackid(i1);
                                Log.d("getTrackid degeri", " " + sarkilar.getTrackid());

                                DatabaseReference oku2 = FirebaseDatabase.getInstance().getReference().child("Tracks").child("" + sarkilar.getTrackid()).child("tracksUrl");

                                ValueEventListener dinle2 = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.d("Caliyor...", "Track: " + dataSnapshot.getValue(String.class));
                                        sarkilar.setTracksUrl(dataSnapshot.getValue(String.class));
                                        hold = dataSnapshot.getValue(String.class);
                                        sarkilar.setTracksUrl(hold);
                                        DatabaseReference oku4 = FirebaseDatabase.getInstance().getReference().child("Time");

                                        ValueEventListener timeListen = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot2) {

                                                Log.d("time child count", "" + dataSnapshot2.getChildrenCount());

                                                if (dataSnapshot2.getChildrenCount() < 1) {
                                                    Date minute = Calendar.getInstance().getTime();
                                                    yaz.child("Time").child("seekTime").setValue(minute.getTime());
                                                    difference = 0;
                                                    Log.d("controll is ", "null");

                                                } else {

                                                    Date endTime = Calendar.getInstance().getTime();
                                                    long start = dataSnapshot2.child("seekTime").getValue(long.class);
                                                    difference = endTime.getTime() - start;

                                                    long diffsecond = difference / 1000 % 60;
                                                    long differenceMinutes = difference / (60 * 1000) % 60;
                                                    difference = (differenceMinutes * 60) + diffsecond;
                                                    Log.d("controll is ", "not null minute" + differenceMinutes + " second " + diffsecond);
                                                }


                                                Log.d("seekTime", "" + difference);
                                                Log.d("seektime then uri", " " + hold);
                                                mPlayer.playUri(mOperationCallback, hold, 0, 0);
                                                try {
                                                    Thread.sleep(10);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                mPlayer.seekToPosition(mOperationCallback,1000*(int)difference);


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        };
                                        oku4.addListenerForSingleValueEvent(timeListen);




                                        Log.d("mPlayer:", "" + mPlayer.isShutdown() + " " + mPlayer.isLoggedIn());

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                };
                                oku2.addValueEventListener(dinle2);


                                Log.d("TrackUrl ", "getTrackUrl: " + sarkilar.getTracksUrl());

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        }
        boolean playing = (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying);
        Log.i("Playing status: ", "Song: " + playing);

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


    }

    public void play(String myToken) {
        if (mPlayer == null) {
            Config playerConfig = new Config(getApplicationContext(), myToken, CLIENT_ID);
            // Since the Player is a static singleton owned by the Spotify class, we pass "this" as
            // the second argument in order to refcount it properly. Note that the method
            // Spotify.destroyPlayer() also takes an Object argument, which must be the same as the
            // one passed in here. If you pass different instances to Spotify.getPlayer() and
            // Spotify.destroyPlayer(), that will definitely result in resource leaks.
            mPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                @Override
                public void onInitialized(SpotifyPlayer player) {
                    mPlayer.setConnectivityStatus(mOperationCallback, getNetworkConnectivity(Main2Activity.this));
                    // Trigger UI refresh
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



    public void onCikisButton(View view) {
        // mPlayer.playUri(mOperationCallback, "spotify:track:1CgUldlwNfY2JQY3sTEwPn", 0, 0);
        //   AuthenticationClient.clearCookies(this);
        //    Intent intent = new Intent(this, LoginActivity.class);
        //    String tut = null;
        //   intent.putExtra("Token", tut);
        if (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying) {
            Log.d("song Time: ", "" + mCurrentPlaybackState.positionMs);


        }
        //  startActivity(intent);
        //  finish();
//        if (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying) {
//            mPlayer.pause(mOperationCallback);
//        } else {
//            mPlayer.resume(mOperationCallback);
//        }


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

    private void fillArrayList(ArrayList<Tracks> tracks, Map<Integer, Track> plyHold) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference();
        tracks.clear();
        for (int index = 0; index < plyHold.size(); index++) {//döngü şarkı size bağlı
            Log.d("plyhold: ", plyHold.get(index).album.images.get(0).url);
            image = (plyHold.get(index).album.images.get(0).url);
            String hold = plyHold.get(index).external_urls.get("spotify");//track url eldesi(player için)
            String hold2 = hold.substring(31);
            String spotifyURL = "spotify:track:" + hold2;
            // tracks.get(index).setTrackid(index);

            Tracks sarki = new Tracks(spotifyURL, plyHold.get(index).name, plyHold.get(index).artists.get(0).name, image, plyHold.get(index).popularity, index);//içini dolduruyoruz
            tracks.add(sarki);
//             dbref.child("Tracks").child(index + "").setValue(sarki);
//            dbref.child("Size").setValue(plyHold.size());
        }


        //Player kısmı
     /*  try {
            String hold3="https://open.spotify.com/album/5dN7F9DV0Qg1XRdIgW8rke";
            String hold = plyHold.get(0).external_urls.get("spotify");
            String hold2 = plyHold.get(8).preview_url;
            Uri uri = Uri.parse(hold2);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(mContext, uri);//context bakılacak !
            mediaPlayer.prepareAsync();//sıkıntılı!!
            mediaPlayer.start();

            if(mediaPlayer.isPlaying()){
                Log.d("playing TRUE","PLAYYY");
            } else{
                Log.d("playing FALSE","NOOOOO");

            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
            }

        });*/
        listViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPlaybackEvent(PlayerEvent event) {

        mCurrentPlaybackState = mPlayer.getPlaybackState();
        mMetadata = mPlayer.getMetadata();
        Log.i("PlaybackEvent", "Player state: " + mCurrentPlaybackState);
        if(mCurrentPlaybackState.isPlaying)
            Log.d("Caliyorrrrrrrrrrrrr","if içinde");
        else {
            Log.d("Caliyorrrrrrrrrrrrr","if dışında");
            playSong();
        }
        Log.e("PlaybackEvent", "Metadata: " + mMetadata);

    }

    @Override
    public void onPlaybackError(Error error) {
        Log.e("onPlaybackError: ", "Hata: " + error);
    }


}
