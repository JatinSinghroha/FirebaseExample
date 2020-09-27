package com.technologyend.firebaseexample;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER =  2;
    private static final String MYSHAREDPREF = "mysharedpref";
    private static final String FULLNAME = "fullname";
    private MessageAdapter mMessageAdapter;
    private EditText mMessageEditText;
    private ListView messageListView;
    private Button mSendButton;
    private String mUsername, mPhoneNum;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private StorageReference mChatPhotosStorageReference;
    private static final String channelID = "NewMSG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> whitelistedCountries = new ArrayList<String>();
        whitelistedCountries.add("US");
        whitelistedCountries.add("NP");
        whitelistedCountries.add("IN");
       
        mUsername = ANONYMOUS;

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        mMessagesDatabaseReference = firebaseDatabase.getReference().child("messages");
        mChatPhotosStorageReference = firebaseStorage.getReference().child("chat_photos");


        ProgressBar progressBar = findViewById(R.id.progressBar);
        messageListView = findViewById(R.id.messageListView);
        messageListView.setItemsCanFocus(true);
        ImageButton photoPickerButton = findViewById(R.id.photoPickerButton);
        mMessageEditText =  findViewById(R.id.messageEditText);
        mSendButton = findViewById(R.id.sendButton);

        // Initialize message ListView and its adapter
        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        messageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        // ImagePickerButton shows an image picker to upload a image for a message
        photoPickerButton.setOnClickListener(view -> {
        });

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        mSendButton.setOnClickListener(view -> {
            FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername, null);
            mMessagesDatabaseReference.push().setValue(friendlyMessage);
            mMessageEditText.setText("");
            //throw new RuntimeException("Test Crash");
        });

        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user != null){
                //user signed in
                mPhoneNum = user.getPhoneNumber();
                onSignedInInitialize(user.getDisplayName());

            }
            else{
                //user not signed in
                onSignedOutCleanup();
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("IN").setWhitelistedCountries(whitelistedCountries).build(),
                                        new AuthUI.IdpConfig.EmailBuilder().build()))
                                .build(),
                        RC_SIGN_IN);
            }
        };

        // ImagePickerButton shows an image picker to upload a image for a message
        photoPickerButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
        });
    }


    private void onSignedInInitialize(String username) {
        mUsername = username;
        if(mUsername == null || mUsername.equals("") || mUsername.equals(ANONYMOUS)) {
            getName();
        }
        attachDatabaseReadListener();
    }
    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        mMessageAdapter.clear();
        detachDatabaseReadListner();

    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            Query myMostViewedPostsQuery = mMessagesDatabaseReference.limitToLast(15);
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    FriendlyMessage friendlyMessage = snapshot.getValue(FriendlyMessage.class);
                    mMessageAdapter.add(friendlyMessage);

                    if(mMessageAdapter.getCount() >= 15){
                        scrollMyListViewToBottom(mMessageAdapter.getPosition(friendlyMessage));
                    }

                    assert friendlyMessage != null;
                    String currentMsgUname = friendlyMessage.getName();
                    String currentMsgText = friendlyMessage.getText();

                    if(!currentMsgUname.equals(mUsername) && mMessageAdapter.getCount() >15 && mMessageAdapter.getPosition(friendlyMessage) > 14) {
                        //Toast.makeText(MainActivity.this, mMessageAdapter.getPosition(friendlyMessage)+"", Toast.LENGTH_SHORT).show();
                                createNotification(currentMsgUname, currentMsgText);
                        }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            myMostViewedPostsQuery.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListner(){
        if(mChildEventListener != null)
        mMessagesDatabaseReference.removeEventListener(mChildEventListener);
        mChildEventListener = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sign_out_menu){
            SharedPreferences sharedPreferences = getSharedPreferences(MYSHAREDPREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(FULLNAME, "");
            editor.apply();
            AuthUI.getInstance().signOut(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != Activity.RESULT_OK) {
                finish();
            }
        }
        if (requestCode == RC_PHOTO_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri selectedImageUri = data.getData();

                // Get a reference to store file at chat_photos/<FILENAME>
                assert selectedImageUri != null;
                final StorageReference photoRef = mChatPhotosStorageReference.child(Objects.requireNonNull(selectedImageUri.getLastPathSegment()));

                // Upload file to Firebase Storage
                photoRef.putFile(selectedImageUri)
                        .addOnSuccessListener(this, taskSnapshot -> photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            FriendlyMessage friendlyMessage = new FriendlyMessage(null, mUsername, uri.toString());
                            mMessagesDatabaseReference.push().setValue(friendlyMessage);
                            Toast.makeText(MainActivity.this, "Image Uploaded, Loading Now", Toast.LENGTH_LONG).show();
                        }));
            } else {
                Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener!=null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
       detachDatabaseReadListner();
        mMessageAdapter.clear();
    }

    private void askForName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter your Name: ");
        builder.setCancelable(false);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            SharedPreferences sharedPreferences = getSharedPreferences(MYSHAREDPREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(FULLNAME, mPhoneNum+" ~ "+input.getText().toString());
            editor.apply();
            mUsername = sharedPreferences.getString(FULLNAME, "");
        });

        builder.show();
    }

    private void getName(){
                SharedPreferences sharedPreferences = getSharedPreferences(MYSHAREDPREF, MODE_PRIVATE);
                mUsername = sharedPreferences.getString(FULLNAME, "");

        if(mUsername == null || mUsername.equals("") || mUsername.equals(ANONYMOUS)){
            askForName();
        }
    }


        private void createNotification(String uname, String txtMsg) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String msgtext;
                if(txtMsg == null || txtMsg.equals("")){
                    msgtext = "New Image Uploaded. Check now!";
                    //Toast.makeText(MainActivity.this, "Image Upload", Toast.LENGTH_SHORT).show();
                }else{
                    msgtext = txtMsg;
                    //Toast.makeText(MainActivity.this, "Text Upload", Toast.LENGTH_SHORT).show();
                }
                CharSequence name = "NewMessage";
                String description = "New Message Notification";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(channelID, name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
                //Intent
                Intent resultIntent = new Intent(this, MainActivity.class);
                resultIntent.setAction(Intent.ACTION_MAIN);
                resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                        resultIntent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("New Message from "+uname)
                        .setContentText(msgtext)
                        .setContentIntent(pendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(txtMsg))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true);
                notificationManager.notify(1, builder.build());
            }
                }
    private void scrollMyListViewToBottom(int pos) {
        messageListView.post(() -> messageListView.setSelection(pos));
    }
}


