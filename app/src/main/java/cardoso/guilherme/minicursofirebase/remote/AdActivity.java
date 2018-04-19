package cardoso.guilherme.minicursofirebase.remote;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import cardoso.guilherme.minicursofirebase.BuildConfig;
import cardoso.guilherme.minicursofirebase.R;

public class AdActivity extends AppCompatActivity {

    private View adView;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        setTitle("Anuncios");

        adView = findViewById(R.id.ad_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        mFirebaseRemoteConfig.setDefaults(R.xml.show_ads);

        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(AdActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();
                            if(mFirebaseRemoteConfig.getBoolean("ad_enabled"))
                                adView.setVisibility(View.VISIBLE);
                            else
                                adView.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
