//package com.example.tabkhtech.authentication.signup.presenter;
//
//import com.example.tabkhtech.authentication.signup.view.SignUpView;
//import com.example.tabkhtech.model.remote.firebase.FirebaseAuthManager;
//
//public class SignUpPresenterImpl implements SignUpPresenter {
//    private SignUpView view;
//    private FirebaseAuthManager authManager;
//
//    public SignUpPresenterImpl(SignUpView view, FirebaseAuthManager authManager) {
//        this.view = view;
//        this.authManager = authManager;
//    }
//
//    @Override
//    public void signUpWithEmail(String name, String email, String password, String confirmPassword) {
//        if (!password.equals(confirmPassword)) {
//            view.showToast("Passwords do not match!");
//            return;
//        }
//        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
//            view.showToast("All fields are required!");
//            return;
//        }
//        if (password.length() < 8) {
//            view.showToast("Password must be at least 8 characters long!");
//            return;
//        }
//
//        authManager.signUpWithEmail(email, password, new FirebaseAuthManager.AuthCallback() {
//            @Override
//            public void onSuccess(com.google.firebase.auth.FirebaseUser user) {
//                authManager.saveUserData(user, name);
//                view.showToast("You have signed up successfully!");
//                view.redirectToMainActivity();
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//                view.showToast("Sign up failed: " + errorMessage);
//            }
//        });
//    }
//
//    @Override
//    public void signUpWithGoogle(String idToken) {
//        authManager.signInWithGoogle(idToken, new FirebaseAuthManager.AuthCallback() {
//            @Override
//            public void onSuccess(com.google.firebase.auth.FirebaseUser user) {
//                authManager.saveUserData(user, null);
//                view.showToast("Signed in: " + user.getEmail());
//                view.redirectToMainActivity();
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//                view.showToast("Authentication failed: " + errorMessage);
//            }
//        });
//    }
//
//    @Override
//    public void continueAsGuest() {
//        authManager.saveGuestData();
//        view.redirectToMainActivity();
//    }
//
//    @Override
//    public void initiateGoogleSignUp() {
//        authManager.initiateGoogleSignIn(() -> view.startGoogleSignUpIntent());
//    }
//}
package com.example.tabkhtech.authentication.signup.presenter;

import com.example.tabkhtech.authentication.signup.view.SignUpView;
import com.example.tabkhtech.model.remote.firebase.FirebaseAuthManager;
import com.example.tabkhtech.model.repository.Repository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPresenterImpl implements SignUpPresenter {
    private SignUpView view;
    private FirebaseAuthManager authManager;
    private Repository repository;

    public SignUpPresenterImpl(SignUpView view, FirebaseAuthManager authManager, Repository repository) {
        this.view = view;
        this.authManager = authManager;
        this.repository = repository;
    }

    @Override
    public void signUpWithEmail(String name, String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            view.showToast("Passwords do not match!");
            return;
        }
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            view.showToast("All fields are required!");
            return;
        }
        if (password.length() < 8) {
            view.showToast("Password must be at least 8 characters long!");
            return;
        }

        authManager.signUpWithEmail(email, password, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(com.google.firebase.auth.FirebaseUser user) {
                // Save user data to SharedPreferences
                authManager.saveUserData(user, name);

                // Save user data to Firebase Realtime Database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                databaseReference.child("name").setValue(name);
                databaseReference.child("email").setValue(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        view.showToast("You have signed up successfully!");
                        view.redirectToMainActivity();
                    } else {
                        view.showToast("Failed to save user data to database");
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showToast("Sign up failed: " + errorMessage);
            }
        });
    }

    @Override
    public void signUpWithGoogle(String idToken) {
        authManager.signInWithGoogle(idToken, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(com.google.firebase.auth.FirebaseUser user) {
                String name = user.getDisplayName() != null ? user.getDisplayName() : "";
                String email = user.getEmail() != null ? user.getEmail() : "";
                authManager.saveUserData(user, name);

                // Save user data to Firebase Realtime Database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                databaseReference.child("name").setValue(name);
                databaseReference.child("email").setValue(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        view.showToast("Signed in: " + user.getEmail());
                        view.redirectToMainActivity();
                    } else {
                        view.showToast("Failed to save user data to database");
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showToast("Authentication failed: " + errorMessage);
            }
        });
    }

    @Override
    public void continueAsGuest() {
        authManager.saveGuestData();

        view.redirectToMainActivity();
    }

    @Override
    public void initiateGoogleSignUp() {
        authManager.initiateGoogleSignIn(() -> view.startGoogleSignUpIntent());
    }
}