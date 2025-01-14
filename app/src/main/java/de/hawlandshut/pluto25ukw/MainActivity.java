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
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hawlandshut.pluto25ukw.model.Post;
import de.hawlandshut.pluto25ukw.test.TestData;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "xxx MainActivity";

    // TODO: Only for testing remove later
    private static final String TEST_MAIL = "fhgreipl@gmail.com";
    private static final String TEST_PASSWORD ="123456";

    FirebaseAuth mAuth;
    FirebaseFirestore mDb;
    ListenerRegistration mListenerRegistration;

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

        mRecyclerView = findViewById( R.id.recycler_view);
        mRecyclerView.setLayoutManager( new LinearLayoutManager( this ));
        mRecyclerView.setAdapter( mAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if ( user == null){
            mAdapter.mPostList.clear();
            if ( mListenerRegistration != null){
                mListenerRegistration.remove();
                mListenerRegistration = null;
                Log.d(TAG, "Listener removed...");
            }
            // Kein User angemeldet
            Intent intent = new Intent(getApplication(), SignInActivity.class);
            startActivity(intent);
        } else {
            // Listener erzeugen
            if (mListenerRegistration == null) {
                Log.d(TAG, "Listerner created...");
                mListenerRegistration = createMyEventListener();
            }
        }
        Log.d(TAG, "onStart called.");
    }

    ListenerRegistration createMyEventListener(){
        // Step 1: Create query
        Query query = mDb.collection("posts")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(5);

        // Step 2; Define, how you react to updates from the listener
        EventListener<QuerySnapshot> listener;
        listener = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                // snaphot ist eine List von documents
                if (error != null){
                    return; // This should not happen (a reason might be missing rights to access firestore)
                }

                // Wir haben eine neue Liste (snapshot) from docs von Firestore erhalten
                // und wollen diese Liste anzeigen, also..

                // ... löschen alle Elemente der Liste
                mAdapter.mPostList.clear();

                for (  QueryDocumentSnapshot doc : snapshot ){
                    Post post = Post.fromDocument( doc );
                    mAdapter.mPostList.add( post );
                    Log.d(TAG, doc.getId() + " Email " + doc.get("email"));
                }
                mAdapter.notifyDataSetChanged();
            }
        };

        return query.addSnapshotListener( listener ) ;
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