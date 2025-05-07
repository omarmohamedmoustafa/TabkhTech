//package com.example.tabkhtech;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.tabkhtech.authentication.signup.view.SignUpActivity;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.button.MaterialButton;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.GoogleAuthProvider;
//
//public class SignInActivity extends AppCompatActivity {
//
//    EditText loginEmail, loginPassword;
//    Button loginButton;
//    TextView signupRedirectText;
//    FirebaseAuth mAuth;
//    private SharedPreferences sharedPreferences;
//    MaterialButton googleSignInButton,guestModeButton ;
//    private GoogleSignInClient mGoogleSignInClient;
//    private static final int RC_SIGN_IN = 1001;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Check if user is already logged in
//        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
//        boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);
//
//        if (isSignedIn) {
//            redirectToMainActivity();
//            return;
//        }
//
//        setContentView(R.layout.activity_sign_in);
//
//        loginEmail = findViewById(R.id.loginEmail);
//        loginPassword = findViewById(R.id.loginPassword);
//        loginButton = findViewById(R.id.login_button);
//        signupRedirectText = findViewById(R.id.signupRedirectText);
//        googleSignInButton = findViewById(R.id.btnGoogleSignIn);
//        mAuth = FirebaseAuth.getInstance();
//        guestModeButton = findViewById(R.id.btnGuestSignIn);
//        // Initialize Google Sign-In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        loginButton.setOnClickListener(view -> {
//            String email = loginEmail.getText().toString().trim();
//            String password = loginPassword.getText().toString().trim();
//
//            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//                Toast.makeText(SignInActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            mAuth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            // Save login state
//                            sharedPreferences.edit()
//                                    .putBoolean("isSignedIn", true)
//                                    .putBoolean("isGuest", false)
//                                    .putString("userId", mAuth.getCurrentUser().getUid())
//                                    .putString("userName", mAuth.getCurrentUser().getDisplayName())
//                                    .putString("userEmail", mAuth.getCurrentUser().getEmail())
//                                    .putString("userProfileImage", mAuth.getCurrentUser().getPhotoUrl() != null ? mAuth.getCurrentUser().getPhotoUrl().toString() : "")
//                                    .apply();
//                            Toast.makeText(SignInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
//                            redirectToMainActivity();
//                        } else {
//                            Toast.makeText(SignInActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        });
//
//        googleSignInButton.setOnClickListener(view -> {
//            mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
//                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//                startActivityForResult(signInIntent, RC_SIGN_IN);
//            });
//        });
//
//        signupRedirectText.setOnClickListener(view -> {
//            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
//            startActivity(intent);
//            finish();
//        });
//
//        guestModeButton.setOnClickListener(view -> {
//            sharedPreferences.edit()
//                    .putBoolean("isGuest", true)
//                    .putBoolean("isSignedIn", false)
//                    .putString("userId", "guest")
//                    .apply();
//            redirectToMainActivity();
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account.getIdToken());
//            } catch (ApiException e) {
//                Log.e("GoogleSignIn", "Sign-in failed", e);
//                Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        // Save login state
//                        sharedPreferences.edit()
//                                .putBoolean("isSignedIn", true)
//                                .putBoolean("isGuest", false)
//                                .putString("userId", mAuth.getCurrentUser().getUid())
//                                .putString("userName", mAuth.getCurrentUser().getDisplayName())
//                                .putString("userEmail", mAuth.getCurrentUser().getEmail())
//                                .putString("userProfileImage", mAuth.getCurrentUser().getPhotoUrl() != null ? mAuth.getCurrentUser().getPhotoUrl().toString() : "")
//                                .apply();
//                        Toast.makeText(this, "Google sign-in successful!", Toast.LENGTH_SHORT).show();
//                        redirectToMainActivity();
//                    } else {
//                        Toast.makeText(this, "Google sign-in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void redirectToMainActivity() {
//        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//    }
//}