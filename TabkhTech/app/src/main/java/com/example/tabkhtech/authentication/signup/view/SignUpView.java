package com.example.tabkhtech.authentication.signup.view;

public interface SignUpView {
    void showToast(String message);
    void redirectToMainActivity();
    void redirectToSignInActivity();
    void startGoogleSignUpIntent();
}