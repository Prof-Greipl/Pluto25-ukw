package de.hawlandshut.pluto25ukw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hawlandshut.pluto25ukw.test.TestData;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "xxx MainActivity";

    // TODO: Only for testing remove later
    private static final String TEST_MAIL = "fhgreipl@gmail.com";
    private static final String TEST_PASSWORD ="123456";

    FirebaseAuth mAuth;
    FirebaseFirestore mDb;
    RecyclerView mRecyclerView;
    CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();

        //Adapter einstellen
        mAdapter = new CustomAdapter();

        // TODO: Adapter mit Testdaten füllen - später raus!
        mAdapter.mPostList = TestData.createPostList(3);

        mRecyclerView = findViewById( R.id.recycler_view);
        mRecyclerView.setLayoutManager( new LinearLayoutManager( this ));
        mRecyclerView.setAdapter( mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if ( user == null){
            // Kein User angemeldet
            Intent intent = new Intent(getApplication(), SignInActivity.class);
            startActivity(intent);
        }
        Log.d(TAG, "onStart called.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();

        if( i == R.id.menu_test_auth){
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null){
                Toast.makeText(getApplicationContext(),
                        "No user authenticated.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "User auth: " + user.getEmail() + "(" + user.isEmailVerified() +")",
                        Toast.LENGTH_LONG).show();
            }
            return true;
        }

        if ( i == R.id.menu_main_test_write){


            Map<String, Object> testMap = new HashMap<>();
            testMap.put("Neue", "datastring");
            /*
            testMap.put("key_bool", false);
            testMap.put("key_float", 1.5);
            testMap.put("key_int", 1);
            testMap.put("key_date", new Date());
*/
            mDb.collection("posts").add( testMap );
            return true;
        }
        if ( i == R.id.menu_create_user){
            doCreateUser(TEST_MAIL, TEST_PASSWORD);
            return true;
        }

        if ( i == R.id.menu_sign_out){
            mAuth.signOut();
            Toast.makeText(getApplicationContext(), "You are signed SignOut", Toast.LENGTH_LONG).show();
            return true;
        }

        if (i == R.id.menu_delete){
            doDeleteUser();
        }
        if ( i == R.id.menu_main_help){
            Toast.makeText(getApplicationContext(), "Your pressed Help", Toast.LENGTH_LONG).show();
            return true;
        }

        if (i == R.id.menu_sign_in){
            doSignIn( TEST_MAIL, TEST_PASSWORD);
            return true;
        }

        if (i == R.id.menu_verification_mail){
            doSendVerificationMail();
            return true;
        }

        if (i == R.id.menu_password_reset_email){
            doSendPasswordResetEmail( TEST_MAIL);
            return true;
        }

        if ( i == R.id.menu_main_manage_account){
            Intent intent = new Intent(getApplication(), ManageAccountActivity.class);
            startActivity(intent);
            return true;
        }

        if (item.getItemId() == R.id.menu_main_post) {
            Intent intent = new Intent(getApplication(), PostActivity.class);
            startActivity(intent);
            return true;
        }

        return true;
    }

    private void doSendPasswordResetEmail(String testMail) {
       FirebaseUser user = mAuth.getCurrentUser();
       mAuth.sendPasswordResetEmail( testMail)
               .addOnCompleteListener(this,
                       new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()){
                                   Toast.makeText(getApplicationContext(), "Mail sent.",
                                           Toast.LENGTH_LONG).show();
                               }
                               else {
                                   Toast.makeText(getApplicationContext(),
                                           "Sending mail failed : "
                                                   + task.getException().getMessage(),
                                           Toast.LENGTH_LONG).show();
                               }
                           }
                       });
    }

    private void doSendVerificationMail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            Toast.makeText(getApplicationContext(), "Please sign in first.",
                    Toast.LENGTH_LONG).show();
        }
        else {
            user.sendEmailVerification()
                    .addOnCompleteListener(this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Mail sent.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),
                                                "Send failed : " + task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
        }
    }

    private void doSignIn(String testMail, String testPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Toast.makeText(getApplicationContext(), "Please sign out first",
                    Toast.LENGTH_LONG).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(testMail, testPassword)
                    .addOnCompleteListener(this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Your are signed in!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),
                                                "Sign in failed : " + task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
        }
    }

    private void doDeleteUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            Toast.makeText(getApplicationContext(), "No user signed in",
                    Toast.LENGTH_LONG).show();
        }
        else {
            user.delete()
                    .addOnCompleteListener(this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "User deleted.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),
                                                "Deletion failed : " + task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
        }
    }


    private void doCreateUser(String testMail, String testPassword) {
        mAuth.createUserWithEmailAndPassword( testMail, testPassword)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "User created.",
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),
                                            "Failed : " + task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called.");
    }
}