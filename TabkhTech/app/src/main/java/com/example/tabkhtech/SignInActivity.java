package com.example.tabkhtech;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.example.tabkhtech.authentication.signup.view.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView signupRedirectText;
    FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already logged in
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);
        boolean isGuest = sharedPreferences.getBoolean("isGuest", false);

        if (isSignedIn || isGuest) {
            redirectToMainActivity();
            return;
        }

        setContentView(R.layout.activity_sign_in);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(view -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(SignInActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Save login state
                            sharedPreferences.edit()
                                    .putBoolean("isSignedIn", true)
                                    .putBoolean("isGuest", false)
                                    .apply();

                            Toast.makeText(SignInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            redirectToMainActivity();
                        } else {
                            Toast.makeText(SignInActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        signupRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}