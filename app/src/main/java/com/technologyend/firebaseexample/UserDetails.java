package com.technologyend.firebaseexample;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

public class UserDetails extends AppCompatActivity {

    EditText mUsername;
    private DatabaseReference mMessagesDatabaseReference, mUsersDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        setTitle("Your Details");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        mUsername = findViewById(R.id.username);
        String mUserID = getIntent().getStringExtra("UID");
        mUsersDatabaseReference = firebaseDatabase.getReference("users/"+mUserID);

                mUsersDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            FriendlyUser friendlyUser = dataSnapshot.getValue(FriendlyUser.class);

                            mUsername.setText(friendlyUser.getUsername());
                        }
                        else{
                            Toast.makeText(UserDetails.this, "Unable to Load Data", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(UserDetails.this, "Some Error Occured!", Toast.LENGTH_LONG).show();
                    }
                });


    }
}