package com.example.tabkhtech.authentication.signin.view;

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
import com.example.tabkhtech.authentication.signin.presenter.SignInPresenter;
import com.example.tabkhtech.authentication.signin.presenter.SignInPresenterImpl;
import com.example.tabkhtech.authentication.signup.view.SignUpActivity;
import com.example.tabkhtech.model.remote.firebase.FirebaseAuthManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements SignInView {

    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView signupRedirectText;
    MaterialButton googleSignInButton, guestModeButton;
    private SignInPresenter presenter;
    private SharedPreferences sharedPreferences;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already logged in
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);

        if (isSignedIn) {
            redirectToMainActivity();
            return;
        }

        setContentView(R.layout.activity_sign_in);

        // Initialize views
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        googleSignInButton = findViewById(R.id.btnGoogleSignIn);
        guestModeButton = findViewById(R.id.btnGuestSignIn);

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
        presenter = new SignInPresenterImpl(this, authManager);

        // Set up click listeners
        loginButton.setOnClickListener(view -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            presenter.signInWithEmail(email, password);
        });

        googleSignInButton.setOnClickListener(view -> presenter.initiateGoogleSignIn());

        signupRedirectText.setOnClickListener(view -> presenter.redirectToSignUpActivity());

        guestModeButton.setOnClickListener(view -> presenter.continueAsGuest());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                presenter.signInWithGoogle(account.getIdToken());
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void redirectToSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void startGoogleSignInIntent() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}