package kaaes.spotify.webapi.samplesearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyService;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.UserPrivate;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends Activity{

    private static final String TAG = LoginActivity.class.getSimpleName();

    @SuppressWarnings("SpellCheckingInspection")
    private static final String CLIENT_ID = "8a91678afa49446c9aff1beaabe9c807";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String REDIRECT_URI = "testschema://callback";
    private Context mContext;
    private static final int REQUEST_CODE = 1337;
    User userinfo=new User();
    public SpotifyApi api = new SpotifyApi();
    ImageView image ;
String holdusrID=" boş";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();

        String token = CredentialsHandler.getToken(this);

        if(token!=null)
            token=intent.getStringExtra("Token");


        if (token == null) {
            setContentView(R.layout.activity_login);
        } else {
            startMainActivity(token,holdusrID);
        }
    }

    public void onLoginButtonClicked(View view) {
        final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"playlist-read"})
                .build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);


    }
//public void cikisİslem(){
//    final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
//            .setScopes(new String[]{"playlist-read"})
//            .build();
//
//    AuthenticationClient.stopLoginActivity(this,REQUEST_CODE);
//
//
//}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
           final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    logMessage("   Got token: " +response.getAccessToken());
                    CredentialsHandler.setToken(this, response.getAccessToken(), response.getExpiresIn(), TimeUnit.SECONDS);


                    api.setAccessToken(response.getAccessToken());
                    SpotifyService sp = api.getService();
                    sp.getMe(new Callback<UserPrivate>() {
                        @Override
                        public void success(UserPrivate userPrivate, Response responsess) {
//      usrId=userPrivate.id;

                            holdusrID=userPrivate.id;


                        userinfo.setUserid(holdusrID);
                           Log.d("???? ",userinfo.getUserid());
                           // Toast.makeText(LoginActivity.this,"User info in StartMainACtive "+holdusrID,Toast.LENGTH_LONG).show();
                            Log.d("start ",holdusrID);
                               logMessage("User success and user id:  "+ userPrivate.id);
                            // Toast.makeText(null,userPrivate.country,Toast.LENGTH_LONG);
                            String hold=userPrivate.display_name;
                            if(hold==null||hold=="")
                                hold="İsim bilgisi yok";

                            userinfo.sendToFirebaseUserInfo(holdusrID,hold);
                            startMainActivity(response.getAccessToken(),userinfo.getUserid());
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("User failure", error.toString());
                        }
                    });

                    break;

                // Auth flow returned an error
                case ERROR:
                    logError("Auth error: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    logError("Auth result: " + response.getType());
            }
        }
    }

    private void startMainActivity(String token,String usr) {
        Intent intent = MainActivity.createIntent(this);
        intent.putExtra(MainActivity.EXTRA_TOKEN, token);

        intent.putExtra("isim",usr);
      startActivity(intent);
      finish();
    }

    private void logError(String msg) {
        Toast.makeText(this, "Error: " + msg, Toast.LENGTH_SHORT).show();
        Log.e(TAG, msg);
    }

    private void logMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, msg);
    }
}
