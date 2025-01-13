package de.hawlandshut.pluto25ukw;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ManageAccountActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;

    // 3.1 UI-Variablen deklarieren
    TextView mLineId, mLineMail, mLineVerified;
    Button mButtonSignOut, mButtonSendActivationMail, mButtonDeleteAccount;
    EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 3.2 UI-Variablen Initialisieren
        mLineId =  findViewById( R.id.manage_account_line_id);
        mLineMail = findViewById( R.id.manage_account_line_email);
        mLineVerified = findViewById( R.id.manage_account_line_verified);
        mButtonDeleteAccount = findViewById( R.id.manage_account_button_delete_account);
        mButtonSignOut = findViewById( R.id.manage_account_button_sign_out);
        mButtonSendActivationMail = findViewById( R.id.manage_account_button_send_activation_mail);
        mPassword = findViewById( R.id.manage_account_password);

        // Listener für Buttons setzen
        mButtonSendActivationMail.setOnClickListener( this );
        mButtonSignOut.setOnClickListener( this );
        mButtonDeleteAccount.setOnClickListener( this );

        // TODO: Prefill for testing - remove later!
        mPassword.setText("123456");
        mLineId.setText("User Id :  acasaert");

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user== null){
            // This should never happen!
            return;
        }
        mLineMail.setText("Email : " +  user.getEmail());
        mLineVerified.setText("Email verified : " +  user.isEmailVerified());
        mLineId.setText("UID : " + user.getUid() );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.manage_account_button_sign_out){
            doSignOut();
            finish();
        }

        if (i == R.id.manage_account_button_send_activation_mail){
            doSendActivationMail();
        }

        if (i == R.id.manage_account_button_delete_account){
            doDeleteAccount();
        }
    }

    private void doDeleteAccount() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            // This should never happen
            return;
        }
        String password = mPassword.getText().toString();
        // TODO: Validation...
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), password);
        user.reauthenticate( credential )
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Reauth  ok",
                                    Toast.LENGTH_LONG).show();
                            finalDeletion();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Reauth  failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void doSendActivationMail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            finish(); // This should never happen.
        } else{
            user.sendEmailVerification()
                    .addOnCompleteListener(this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),
                                                "Verification E-Mail sent",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),
                                                "Verification sending failed : " + task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
        }
    }

    private void doSignOut() {
        mAuth.signOut();
        Toast.makeText(getApplicationContext(), "Your pressed Sign Out", Toast.LENGTH_LONG).show();
    }

    void finalDeletion() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // This should never happen!
        } else {
            user.delete()
                    .addOnCompleteListener(this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "User deleted.",
                                                Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        // TODO: Check message - too technical
                                        Toast.makeText(getApplicationContext(),
                                                "Deletion failed : " + task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

        }
    }
}