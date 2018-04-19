package cardoso.guilherme.minicursofirebase.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cardoso.guilherme.minicursofirebase.MainActivity;
import cardoso.guilherme.minicursofirebase.R;

public class CreateAccountActivity extends AppCompatActivity {

    private static String TAG = "CreateAccountActivity";

    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;

    private Button btnCreateAccount;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        setTitle("Criar conta");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email;
                String password;

                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                createNewAccount(email, password);
            }
        });

    }

    private void createNewAccount(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateAccountActivity.this,
                                    "Conta criada com sucesso.",
                                    Toast.LENGTH_SHORT).show();

                            Bundle params = new Bundle();
                            params.putString("email", email);
                            mFirebaseAnalytics.logEvent("create_account", params);

                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // User is signed in
                                mAuth.signOut();
                            } else {
                                // User is signed out

                            }

                            finish();
                        } else {
                            Toast.makeText(CreateAccountActivity.this,
                                    "Erro ao tentar criar conta.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
