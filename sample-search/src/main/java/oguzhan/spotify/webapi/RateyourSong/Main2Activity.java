package oguzhan.spotify.webapi.RateyourSong;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Image;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.spotify.sdk.android.authentication.AuthenticationClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Headers;

public class Main2Activity extends AppCompatActivity {
    public static final String TAG = "Main2Activity";
    private ArrayList<Tracks> tracks;
    private ListView listView;
    private final List<Track> mItems = new ArrayList<>();
    private TracksResultsAdaptor listViewAdapter;
    private Button exit;
    private Button oyVer;
    private String usr;
    private String token;
    private Image holdimage;
    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";
    private SpotifyService mService;
    private PreviewPlayer mPlayer;
    private String playlistURl;
    private String trackname=" ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        token = intent.getStringExtra(EXTRA_TOKEN);
        exit = (Button) findViewById(R.id.Exitbutton);
        usr = intent.getStringExtra("isim");
        Log.d("isimStart ", usr);

        SpotifyApi spotifyApi = new SpotifyApi();
        spotifyApi.setAccessToken(token);
        mService = spotifyApi.getService();
        // Playlist weList=new Playlist();

        playlistURl = "37i9dQZF1DXdnOj1VEuhgb";
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
                int size = playlist.tracks.items.size();
                for (int i = 0; i < size; i++)
                    plyHold.put(i, playlist.tracks.items.get(i).track);


                // String muzik= playlist.tracks.items.get(0).track.preview_url.toString();
                //mPlayer.play(muzik);
                Log.e("item external: ", String.valueOf(playlist.tracks.items.get(0).track.external_urls.toString()));


                initialize();
                fillArrayList(tracks, plyHold);
            }
        });


      /*
        Pager<PlaylistTrack> playlistTracks=mService.getPlaylistTracks(usr,"spotify:user:spotify:playlist:37i9dQZF1DWXBcLLksEQAf");
        List<PlaylistTrack> listemiz=new ArrayList<PlaylistTrack>();
        listemiz.add(0,playlistTracks.items.get(0));
       */


    }

    public void onCikisButton(View view) {
        AuthenticationClient.clearCookies(this);
        Intent intent = new Intent(this, LoginActivity.class);
        String tut = null;
        intent.putExtra("Token", tut);
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

    private void fillArrayList(ArrayList<Tracks> tracks, Map<Integer, Track> plyHold) {

        for (int index = 0; index < plyHold.size(); index++) {//döngü şarkı size bağlı

            Log.i("image: ", plyHold.get(index).album.images.get(0).url);
            Uri b = Uri.parse(plyHold.get(index).album.images.get(0).url);
            if(plyHold.get(index).preview_url!=null||plyHold.get(index).preview_url!=""){

                trackname=plyHold.get(index).preview_url;
            }else
                trackname="Boş";



            Tracks sarki = new Tracks(trackname, plyHold.get(index).name, plyHold.get(index).album.name,b );//içini dolduracağız

            tracks.add(sarki);
        }
        listViewAdapter.notifyDataSetChanged();

    }
}
