package kaaes.spotify.webapi.samplesearch;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by oguzhanaydin on 19.07.2017.
 */

public class User {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private String userid;

    public void setUserid(String userid){

        this.userid=userid;
    }
    public String getUserid(){

        return userid;

    }
    public void sendToFirebaseUserInfo(String userid,String name){

//        String keyvalue=myRef.push().getKey();
        myRef.child("Users").child(userid).setValue(name);




    }


}
