package com.example.khamelea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    EditText firstName, lastName, email, password, passwordConfirm;
    Button btnRegister;
    TextView txtAccount;

    // Progress bar to display while registering
    ProgressDialog progressDialog;

    // Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        // Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // Initialize variables
        btnRegister = findViewById(R.id.btnRegister);
        //firstName = findViewById(R.id.txtFirst);
        //lastName = findViewById(R.id.txtLast);
        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPassword);
        //passwordConfirm = findViewById(R.id.txtConfirm);
        txtAccount = findViewById(R.id.txtAccount);

        // In the onCreate() method, initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        // Handle registration button click
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Input
                String strEmail = email.getText().toString().trim();
                String strPass = password.getText().toString().trim();
                // Validation
                if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    // Set error and focus to email EditText
                    email.setError("Invalid Email");
                    email.setFocusable(true);
                }
                else if (strPass.length() < 6) {
                    // Set error and focus to password EditText
                    password.setError("Password must be 6 characters or more");
                    password.setFocusable(true);
                }
                else {
                    registerUser(strEmail,strPass); // Register the user
                }
            }
        });

        txtAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, UserLogin.class));
                finish();
            }
        });
    }

    private void registerUser(String strEmail, String strPass) {
        // If email and password are valid, show progress dialog and start registering user
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(strEmail, strPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, dismiss dialog and start Signup activity
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Get user email and User ID
                            String getEmail = user.getEmail();
                            String getID = user.getUid();

                            // Store user info in database using HashMap

                            HashMap<Object, String> hashMap = new HashMap<>();
                            // Put information in HashMap
                            hashMap.put("Email", getEmail);
                            hashMap.put("UserID", getID);
                            hashMap.put("Name", "");
                            hashMap.put("Phone", "");
                            hashMap.put("image", "");
                            hashMap.put("cover", "");
                            //Firebase Database Instance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            // Store data in table name "Users"
                            DatabaseReference reference = database.getReference("Users");
                            // Put data in HashMap into table
                            reference.child(getID).setValue(hashMap);


                            Toast.makeText(Signup.this, "Registered...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(Signup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error, dismiss progress dialog and get and show the error message
                progressDialog.dismiss();
                Toast.makeText(Signup.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Go to previous activity
        return super.onSupportNavigateUp();
    }
}