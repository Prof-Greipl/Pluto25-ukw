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

import de.hawlandshut.pluto25ukw.test.TestData;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "xxx MainActivity";

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
        //
        // Intent intent = new Intent(getApplication(), ManageAccountActivity.class);
        // startActivity(intent);
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
        if ( i == R.id.menu_main_help){
            Toast.makeText(getApplicationContext(), "Your pressed Help", Toast.LENGTH_LONG).show();
            return true;
        }

        if ( i == R.id.menu_main_manage_account){
            Toast.makeText(getApplicationContext(), "Your pressed Manage Account", Toast.LENGTH_LONG).show();
            return true;
        }

        if (item.getItemId() == R.id.menu_main_sign_in) {
            Intent intent = new Intent(getApplication(), SignInActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.menu_main_post) {
            Intent intent = new Intent(getApplication(), PostActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called.");
    }
}