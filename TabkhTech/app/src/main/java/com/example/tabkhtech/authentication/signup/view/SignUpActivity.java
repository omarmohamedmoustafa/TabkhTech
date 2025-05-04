package com.example.tabkhtech.authentication.signup.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tabkhtech.MainActivity;
import com.example.tabkhtech.R;
import com.example.tabkhtech.SignInActivity;
import com.example.tabkhtech.authentication.signup.presenter.SignUpPresenter;
import com.example.tabkhtech.authentication.signup.presenter.SignUpPresenterImpl;
import com.example.tabkhtech.model.local.MealLocalDataSourceImpl;
import com.example.tabkhtech.model.pojos.User;
import com.example.tabkhtech.model.remote.MealRemoteDataSourceImpl;
import com.example.tabkhtech.model.repository.RepositoryImpl;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements SignUpView {

    EditText emailEditText, passwordEditText, nameEditText, confirmPasswordEditText;
    Button signUpButton;
    TextView loginRedirectText;
    private static final int RC_SIGN_IN = 1001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private SignUpPresenter presenter;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Database and Reference
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        // Initialize presenter
        presenter = new SignUpPresenterImpl(this, RepositoryImpl.getInstance(
                MealLocalDataSourceImpl.getInstance(this),
                MealRemoteDataSourceImpl.getInstance()
        ));

        // Check if user is already logged in
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);
        boolean isGuest = sharedPreferences.getBoolean("isGuest", false);

        if (isSignedIn || isGuest) {
            redirectToMainActivity();
            return;
        }

        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPassword);
        nameEditText = findViewById(R.id.etName);
        confirmPasswordEditText = findViewById(R.id.etConfirmPassword);
        signUpButton = findViewById(R.id.btnSignUp);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        Button guestButton = findViewById(R.id.btnGuest);
        guestButton.setOnClickListener(v -> continueAsGuest());

        loginRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });

        signUpButton.setOnClickListener(view -> {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            } else if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                String userId = firebaseUser.getUid();

                                // Create User object with userId
                                User helperClass = new User(userId, name, email, password);
                                reference.child(userId).setValue(helperClass);

                                // Save user to Room via presenter
                                presenter.insertUser(helperClass);

                                // Save login state
                                sharedPreferences.edit()
                                        .putBoolean("isSignedIn", true)
                                        .putBoolean("isGuest", false)
                                        .putString("userId", userId)
                                        .apply();

                                Toast.makeText(SignUpActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                                redirectToMainActivity();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.btnGoogleSignIn).setOnClickListener(view -> {
            mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            });
        });
    }

    private void continueAsGuest() {
        sharedPreferences.edit()
                .putBoolean("isGuest", true)
                .putBoolean("isSignedIn", false)
                .putString("userId", "guest")
                .apply();
        redirectToMainActivity();
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.e("GoogleSignIn", "Sign-in failed", e);
                Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            String name = firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "Google User";
                            String email = firebaseUser.getEmail();

                            // Create User object with userId
                            User helperClass = new User(userId, name, email, "");
                            reference.child(userId).setValue(helperClass);

                            // Save user to Room via presenter
                            presenter.insertUser(helperClass);

                            // Save login state
                            sharedPreferences.edit()
                                    .putBoolean("isSignedIn", true)
                                    .putBoolean("isGuest", false)
                                    .putString("userId", userId)
                                    .apply();

                            Toast.makeText(this, "Signed in: " + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                            redirectToMainActivity();
                        } else {
                            Toast.makeText(this, "Authentication failed: No user found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToRoom(User user) {
        presenter.insertUser(user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}