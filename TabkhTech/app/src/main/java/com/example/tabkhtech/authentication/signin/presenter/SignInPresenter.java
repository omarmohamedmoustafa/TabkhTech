package com.example.tabkhtech.authentication.signin.presenter;


public interface SignInPresenter {
    void signInWithEmail(String email, String password);
    void signInWithGoogle(String idToken);
    void continueAsGuest();
    void initiateGoogleSignIn();
    void redirectToSignUpActivity();
}