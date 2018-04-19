package cardoso.guilherme.minicursofirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cardoso.guilherme.minicursofirebase.auth.CreateAccountActivity;
import cardoso.guilherme.minicursofirebase.auth.LoginActivity;
import cardoso.guilherme.minicursofirebase.remote.AdActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnCreateAccount;
    private Button btnAdScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Tela Inicial");

        btnCreateAccount = findViewById(R.id.createAccount);
        btnLogin = findViewById(R.id.login);
        btnAdScreen = findViewById(R.id.adScreen);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        btnAdScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdActivity.class);
                startActivity(intent);
            }
        });
    }
}
