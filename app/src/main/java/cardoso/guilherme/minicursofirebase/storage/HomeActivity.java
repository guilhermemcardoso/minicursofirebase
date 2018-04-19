package cardoso.guilherme.minicursofirebase.storage;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import cardoso.guilherme.minicursofirebase.R;
import cardoso.guilherme.minicursofirebase.database.MessagesActivity;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class HomeActivity extends AppCompatActivity {

    private static String TAG = "HomeActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;

    private Button btnLogout;
    private Button btnGallery;
    private Button btnCamera;
    private Button btnMessages;

    private ImageView profileImage;
    private TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mUser = mAuth.getCurrentUser();

        EasyImage.configuration(this).setImagesFolderName("Galeria");

        btnLogout = findViewById(R.id.btnLogout);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnMessages = findViewById(R.id.btnMessages);

        profileImage = findViewById(R.id.profile_image);
        welcomeMessage = findViewById(R.id.welcome_message);


        if(mUser != null) {
            welcomeMessage.setText("Olá " + mUser.getEmail());
        } else {
            welcomeMessage.setText("");
        }

        mStorageRef = mStorage.getReference();
        StorageReference imageRef = mStorageRef.child(mUser.getUid() + ".jpeg");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mAuth.signOut();
                    finish();
                } else {
                    // User is signed out

                }
            }
        });

        btnMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MessagesActivity.class);
                startActivity(intent);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openCamera(HomeActivity.this, 1024);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openGallery(HomeActivity.this, 1024);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePicked(File file, EasyImage.ImageSource imageSource, int i) {
                final StorageReference imageRef = mStorageRef.child(mUser.getUid() + ".jpeg");
                UploadTask uploadTask = imageRef.putFile(Uri.fromFile(file));

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(profileImage);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mUser = mAuth.getCurrentUser();

        if (mUser != null) {
            // User is signed in
            Log.d(TAG, "onAuthStateChanged: signed_in: " + mUser.getUid());
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged: signed_out");
            finish();
        }
    }
}
