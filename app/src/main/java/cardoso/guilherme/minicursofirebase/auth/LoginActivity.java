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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cardoso.guilherme.minicursofirebase.HomeActivity;
import cardoso.guilherme.minicursofirebase.R;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = "CreateAccountActivity";

    private FirebaseAuth mAuth;

    private Button btnCreateLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        btnCreateLogin = findViewById(R.id.btnLogin);

        btnCreateLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email;
                String password;

                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                login(email, password);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // User is signed in
            Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged: signed_out");
        }
    }

    private void login(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail: success");
                            Toast.makeText(LoginActivity.this, "Login realizado com sucesso",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d(TAG, "signInWithEmail: failed - ", task.getException());
                            Toast.makeText(LoginActivity.this, "Erro ao tentar entrar",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
