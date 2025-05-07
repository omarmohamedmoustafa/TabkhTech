package com.example.tabkhtech.authentication.signup.presenter;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface SignUpPresenter {
    void signUpWithEmail(String name, String email, String password, String confirmPassword);
    void signUpWithGoogle(String idToken);
    void continueAsGuest();
    void initiateGoogleSignUp();
}