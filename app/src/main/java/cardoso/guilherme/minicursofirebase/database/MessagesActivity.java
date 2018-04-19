package cardoso.guilherme.minicursofirebase.database;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cardoso.guilherme.minicursofirebase.R;

public class MessagesActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private Button btnEnviar;
    private EditText etMessage;
    private TextView tvMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        setTitle("Mensagens");

        btnEnviar = findViewById(R.id.btnEnviar);
        etMessage = findViewById(R.id.etMessage);
        tvMessages = findViewById(R.id.tvMessages);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("messages");

        // Read from the database
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tvMessages.setText("");

                for(DataSnapshot message : dataSnapshot.getChildren()) {
                    Message msg = message.getValue(Message.class);
                    tvMessages.setText(tvMessages.getText() + "\n" + msg.getAuthor() + ":\n" + msg.getContent()  + "\n");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.setAuthor(mUser.getEmail());
                message.setContent(etMessage.getText().toString());

                mRef.push().setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        etMessage.setText("");
                        etMessage.clearFocus();
                    }
                });
            }
        });
    }
}
