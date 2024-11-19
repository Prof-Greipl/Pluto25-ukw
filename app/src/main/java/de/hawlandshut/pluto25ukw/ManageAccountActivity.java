package de.hawlandshut.pluto25ukw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ManageAccountActivity extends AppCompatActivity implements View.OnClickListener{

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
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.manage_account_button_sign_out){
            doSignOut();
        }

        if (i == R.id.manage_account_button_send_activation_mail){
            doSendActivationMail();
        }

        if (i == R.id.manage_account_button_delete_account){
            doDeleteAccount();
        }
    }

    private void doDeleteAccount() {
        Toast.makeText(getApplicationContext(), "Your pressed Delete Account", Toast.LENGTH_LONG).show();
    }

    private void doSendActivationMail() {
        Toast.makeText(getApplicationContext(), "Your pressed Send ActMail", Toast.LENGTH_LONG).show();
    }

    private void doSignOut() {
        Toast.makeText(getApplicationContext(), "Your pressed Sign Out", Toast.LENGTH_LONG).show();
    }
}