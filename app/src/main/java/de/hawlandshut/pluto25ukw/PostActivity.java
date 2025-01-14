package de.hawlandshut.pluto25ukw;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity  implements View.OnClickListener{

    private static final String TAG = "xxx PostActivity";

    Button mButtonPost;
    EditText mEditTextTitle;
    EditText mEditTextText;

    FirebaseAuth mAuth;
    FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mButtonPost = findViewById( R.id.postButtonPost);
        mEditTextTitle = findViewById( R.id.postEditTextTitle);
        mEditTextText = findViewById( R.id.postEditTextText);

        mButtonPost.setOnClickListener( this );

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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
        FirebaseUser user = mAuth.getCurrentUser();
        if ( user == null){
            finish();  // This should never happen!
        }

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("uid", user.getUid());
        postMap.put("email", user.getEmail());
        postMap.put("title", mEditTextTitle.getText().toString());
        postMap.put("body", mEditTextText.getText().toString());
        postMap.put("createdAt", FieldValue.serverTimestamp());
        mDb.collection("posts").add( postMap ).addOnCompleteListener(this,
                new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful(  )){
                            Toast.makeText(getApplicationContext(), "Posted!",
                                    Toast.LENGTH_LONG).show();
                            Log.d(TAG, "DocumentSnapshot added!");
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Posting failed!.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}