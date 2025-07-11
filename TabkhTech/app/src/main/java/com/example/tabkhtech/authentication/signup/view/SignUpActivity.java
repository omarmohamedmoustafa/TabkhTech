package com.example.tabkhtech.authentication.signup.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tabkhtech.MainActivity;
import com.example.tabkhtech.R;
import com.example.tabkhtech.authentication.signin.view.SignInActivity;
import com.example.tabkhtech.authentication.signup.presenter.SignUpPresenter;
import com.example.tabkhtech.authentication.signup.presenter.SignUpPresenterImpl;
import com.example.tabkhtech.model.local.MealLocalDataSourceImpl;
import com.example.tabkhtech.model.remote.firebase.FirebaseAuthManager;
import com.example.tabkhtech.model.remote.retrofit.MealRemoteDataSourceImpl;
import com.example.tabkhtech.model.repository.RepositoryImpl;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements SignUpView {

    EditText emailEditText, passwordEditText, nameEditText, confirmPasswordEditText;
    Button signUpButton;
    TextView loginRedirectText;
    MaterialButton googleSignUpButton;
    private SignUpPresenter presenter;
    private SharedPreferences sharedPreferences;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already logged in
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);
        boolean isGuest = sharedPreferences.getBoolean("isGuest", false);
        if (isSignedIn) {
            redirectToMainActivity();
            return;
        }

        setContentView(R.layout.activity_sign_up);

        // Initialize views
        emailEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPassword);
        nameEditText = findViewById(R.id.etName);
        confirmPasswordEditText = findViewById(R.id.etConfirmPassword);
        signUpButton = findViewById(R.id.btnSignUp);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        googleSignUpButton = findViewById(R.id.btnGoogleSignUp);
        Button guestButton = findViewById(R.id.btnGuest);

        // Initialize FirebaseAuthManager
        FirebaseAuthManager authManager = new FirebaseAuthManager(
                FirebaseAuth.getInstance(),
                sharedPreferences,
                getString(R.string.default_web_client_id),
                this
        );

        // Initialize GoogleSignInClient
        mGoogleSignInClient = authManager.getGoogleSignInClient();
        // Initialize presenter
        presenter = new SignUpPresenterImpl(this, authManager, RepositoryImpl.getInstance(MealLocalDataSourceImpl.getInstance(this), MealRemoteDataSourceImpl.getInstance()));

        // Set up click listeners
        signUpButton.setOnClickListener(view -> {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();
            presenter.signUpWithEmail(name, email, password, confirmPassword);
        });

        loginRedirectText.setOnClickListener(view -> redirectToSignInActivity());

        googleSignUpButton.setOnClickListener(view -> presenter.initiateGoogleSignUp());

        guestButton.setOnClickListener(v -> presenter.continueAsGuest());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                presenter.signUpWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.e("GoogleSignIn", "Sign-in failed", e);
                showToast("Google sign-in failed: " + e.getMessage());
            }
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void redirectToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void redirectToSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void startGoogleSignUpIntent() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}