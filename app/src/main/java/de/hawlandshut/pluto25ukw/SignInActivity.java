package de.hawlandshut.pluto25ukw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    // 3.1 UI-Variablen deklarieren
    EditText mEMail;
    EditText mPassword;
    Button mButtonSignIn;
    Button mButtonResetPassword;
    Button mButtonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sign_in), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        // 3.2 UI-Variablen Initialisieren
        mEMail = findViewById( R.id.sign_in_email);
        mPassword = findViewById( R.id.sign_in_password);
        mButtonSignIn = findViewById( R.id.sign_in_button_sign_in);

        // TODO: Name der Variable besser: R.id.sign_in_button_reset_password
        mButtonResetPassword = findViewById( R.id.sign_in_reset_password);
        mButtonCreateAccount = findViewById( R.id.sign_in_button_create_account);

        mButtonSignIn.setOnClickListener( this );
        mButtonResetPassword.setOnClickListener( this );
        mButtonCreateAccount.setOnClickListener( this );

        // TODO: Prefill for testing - remove later!
        mEMail.setText("fhgreipl@gmail.com");
        mPassword.setText( "123456");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.sign_in_button_sign_in){
            doSignIn();
        }

        if (i == R.id.sign_in_reset_password){
            doResetPassword();
        }

        if (i == R.id.sign_in_button_create_account){
            Intent intent = new Intent(getApplication(), CreateAccountActivity.class);
            startActivity(intent);
        }
    }

    private void doResetPassword() {
        String email = mEMail.getText().toString();
        // Validation: email ok?
        mAuth.sendPasswordResetEmail( email)
                .addOnCompleteListener(this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    // TODO: Message anpassen!
                                    Toast.makeText(getApplicationContext(), "Mail sent.",
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    // TODO: Message anpassen!
                                    Toast.makeText(getApplicationContext(),
                                            "Sending mail failed : "
                                                    + task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });

    }

    private void doSignIn() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // This should never happen
            return;
        } else {
            String email = mEMail.getText().toString();
            String password = mPassword.getText().toString();
            // Validations...
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Your are signed in!",
                                                Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Sign in failed : " + task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

        }
    }
}