package oguzhan.spotify.webapi.RateyourSong;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.spotify.sdk.android.authentication.AuthenticationClient;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    private ArrayList<Tracks> tracks;
    private ListView listView;
    private TracksResultsAdaptor listViewAdapter;
    private Button exit;
    private Button oyVer;
    private String usr;
    private String token;
    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        token = intent.getStringExtra(EXTRA_TOKEN);
        exit=(Button)findViewById(R.id.Exitbutton);
        usr =intent.getStringExtra("isim");
        Log.d("isimStart ",usr);

        initialize();
        fillArrayList(tracks);

    }
    public void onCikisButton(View view){
        AuthenticationClient.clearCookies(this);
        Intent intent =new Intent(this,LoginActivity.class);
        String tut=null;
        intent.putExtra("Token",tut);
        startActivity(intent);
        finish();

    }
    public static Intent createIntent(Context context) {
        return new Intent(context, Main2Activity.class);
    }


    private void initialize() {
        tracks = new ArrayList<Tracks>();
        listView = (ListView) findViewById(R.id.tracks_list_view);
        listViewAdapter = new TracksResultsAdaptor(Main2Activity.this,tracks);
        listView.setAdapter(listViewAdapter);
    }

    private void fillArrayList(ArrayList<Tracks>tracks){

        for (int index = 0; index <10; index++) {//döngü şarkı size bağlı
            Tracks sarki = new Tracks();//içini dolduracağız
            tracks.add(sarki);
        }

        }
}
