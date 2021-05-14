package com.example.khamelea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.internal.NavigationMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {

    // Firebase Authorization
    FirebaseAuth firebaseAuth;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        //Actionbar and its title
        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        // Initialize
        firebaseAuth = FirebaseAuth.getInstance();

        // Default fragment
        actionBar.setTitle("Home");
        HomeFragment fragmentH = new HomeFragment();
        FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
        ftH.replace(R.id.content, fragmentH, "");
        ftH.commit();

    }



    private void checkUserStatus() {
        // Get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            // Set the email of the logged in user
            //tvUserEmail.setText(user.getEmail());
        }
        else {
            // User is not signed in, got to main activity
            startActivity(new Intent(Dashboard.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        // Check on start of app
        checkUserStatus();
        super.onStart();
    }

    // Inflate options menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Handle menu item clicks

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                actionBar.setTitle("Home");
                HomeFragment fragmentH = new HomeFragment();
                FragmentTransaction ftH = getSupportFragmentManager().beginTransaction();
                ftH.replace(R.id.content, fragmentH, "");
                ftH.commit();
                return true;
            case R.id.profile:
                actionBar.setTitle("Profile");
                ProfileFragment fragmentP = new ProfileFragment();
                FragmentTransaction ftP = getSupportFragmentManager().beginTransaction();
                ftP.replace(R.id.content, fragmentP, "");
                ftP.commit();
                return true;
            case R.id.users:
                actionBar.setTitle("Users");
                UsersFragment fragmentU = new UsersFragment();
                FragmentTransaction ftU = getSupportFragmentManager().beginTransaction();
                ftU.replace(R.id.content, fragmentU, "");
                ftU.commit();
                return true;
            case R.id.logout:
                firebaseAuth.signOut();
                checkUserStatus();
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

}