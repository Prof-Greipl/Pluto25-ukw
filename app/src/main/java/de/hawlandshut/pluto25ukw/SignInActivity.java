package de.hawlandshut.pluto25ukw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

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
        // 3.2 UI-Variablen Initialisieren
        mEMail = findViewById( R.id.sign_in_email);
        mPassword = findViewById( R.id.sign_in_password);
        mButtonSignIn = findViewById( R.id.sign_in_button_sign_in);
        mButtonResetPassword = findViewById( R.id.sign_in_reset_password);
        mButtonCreateAccount = findViewById( R.id.sign_in_button_create_account);

        mButtonSignIn.setOnClickListener( this );
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.sign_in_button_sign_in){
            Toast.makeText(getApplicationContext(), "Your pressed SignIn", Toast.LENGTH_LONG).show();
        }
    }
}