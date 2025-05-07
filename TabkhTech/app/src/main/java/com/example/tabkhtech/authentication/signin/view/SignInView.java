package com.example.tabkhtech.authentication.signin.view;

public interface SignInView {
    void showToast(String message);
    void redirectToMainActivity();
    void redirectToSignUpActivity();
    void startGoogleSignInIntent();
}