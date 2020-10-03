package com.technologyend.firebaseexample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

public class UserDetails extends AppCompatActivity {

    private EditText mUsername, mEmail, mPhonenum, mSignup, mLastSignIn;
    private TextView tvPhone, tvEmail;
    private Button btnSaveChanges;
    private DatabaseReference mMessagesDatabaseReference, mUsersDatabaseReference;
    private String muserID, uname, uemail, uphone;
    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        setTitle("Your Details");

        mUsername = findViewById(R.id.username);
        mEmail = findViewById(R.id.email);
        mPhonenum = findViewById(R.id.phonenumET);
        mSignup = findViewById(R.id.signUpDate);
        mLastSignIn = findViewById(R.id.lastLoginIn);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        tvPhone = findViewById(R.id.userphonenumTV);
        tvEmail = findViewById(R.id.userEmailTV);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        //getting user details from Auth
        muserID = user.getUid();
        uemail = user.getEmail();
        uphone = user.getPhoneNumber();

        if(uphone != null && !uphone.equals("")){
            tvPhone.setTextColor(Color.RED);
            mPhonenum.setFocusable(false);
            mPhonenum.setEnabled(false);
        }
        if(uemail != null && !uemail.equals("")){
            tvEmail.setTextColor(Color.RED);
            mEmail.setFocusable(false);
            mEmail.setEnabled(false);
        }

        mUsersDatabaseReference = firebaseDatabase.getReference("users/"+muserID);

                mUsersDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            FriendlyUser friendlyUser = dataSnapshot.getValue(FriendlyUser.class);
                            mUsername.setText(friendlyUser.getUsername());
                            mEmail.setText(friendlyUser.getEmail());
                            mPhonenum.setText(friendlyUser.getPhonenumber());
                            mSignup.setText(friendlyUser.getSignupDate());
                            mLastSignIn.setText(friendlyUser.getLastSignIN());
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


                btnSaveChanges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FriendlyUser friendlyUser = new FriendlyUser(mUsername.getText().toString(), mEmail.getText().toString(), mPhonenum.getText().toString(), mSignup.getText().toString(), mLastSignIn.getText().toString());

                        mUsersDatabaseReference.setValue(friendlyUser);

                        Toast.makeText(UserDetails.this, "Changes Saved", Toast.LENGTH_LONG).show();
                    }
                });
    }
}