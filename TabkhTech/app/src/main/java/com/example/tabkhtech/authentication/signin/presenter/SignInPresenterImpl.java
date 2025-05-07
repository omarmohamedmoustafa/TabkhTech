//package com.example.tabkhtech.authentication.signin.presenter;
//
//import com.example.tabkhtech.authentication.signin.view.SignInView;
//import com.example.tabkhtech.model.remote.firebase.FirebaseAuthManager;
//
//public class SignInPresenterImpl implements SignInPresenter {
//    private SignInView view;
//    private FirebaseAuthManager authManager;
//
//    public SignInPresenterImpl(SignInView view, FirebaseAuthManager authManager) {
//        this.view = view;
//        this.authManager = authManager;
//    }
//
//    @Override
//    public void signInWithEmail(String email, String password) {
//        if (email.isEmpty() || password.isEmpty()) {
//            view.showToast("All fields are required!");
//            return;
//        }
//
//        authManager.signInWithEmail(email, password, new FirebaseAuthManager.AuthCallback() {
//            @Override
//            public void onSuccess(com.google.firebase.auth.FirebaseUser user) {
//                // Retrieve the display name from FirebaseUser
//                String displayName = user.getDisplayName() != null ? user.getDisplayName() : "";
//                authManager.saveUserData(user, displayName);
//                view.showToast("Login successful!");
//                view.redirectToMainActivity();
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//                view.showToast("Login failed: " + errorMessage);
//            }
//        });
//    }
//
//    @Override
//    public void signInWithGoogle(String idToken) {
//        authManager.signInWithGoogle(idToken, new FirebaseAuthManager.AuthCallback() {
//            @Override
//            public void onSuccess(com.google.firebase.auth.FirebaseUser user) {
//                // Optionally retrieve the display name for Google sign-in
//                String displayName = user.getDisplayName() != null ? user.getDisplayName() : "";
//                authManager.saveUserData(user, displayName);
//                view.showToast("Google sign-in successful!");
//                view.redirectToMainActivity();
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//                view.showToast("Google sign-in failed: " + errorMessage);
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
//    public void initiateGoogleSignIn() {
//        authManager.initiateGoogleSignIn(() -> view.startGoogleSignInIntent());
//    }
//
//    @Override
//    public void redirectToSignUpActivity() {
//        view.redirectToSignUpActivity();
//    }
//}

package com.example.tabkhtech.authentication.signin.presenter;

import com.example.tabkhtech.authentication.signin.view.SignInView;
import com.example.tabkhtech.model.remote.firebase.FirebaseAuthManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInPresenterImpl implements SignInPresenter {
    private SignInView view;
    private FirebaseAuthManager authManager;

    public SignInPresenterImpl(SignInView view, FirebaseAuthManager authManager) {
        this.view = view;
        this.authManager = authManager;
    }

    @Override
    public void signInWithEmail(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            view.showToast("All fields are required!");
            return;
        }

        authManager.signInWithEmail(email, password, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(com.google.firebase.auth.FirebaseUser user) {
                // Retrieve user data from Firebase Realtime Database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String name = dataSnapshot.child("name").getValue(String.class);
                            String email = dataSnapshot.child("email").getValue(String.class);
                            // Save to SharedPreferences
                            authManager.saveUserData(user, name);
                            view.showToast("Login successful!");
                            view.redirectToMainActivity();
                        } else {
                            view.showToast("User data not found");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        view.showToast("Failed to retrieve user data: " + databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showToast("Login failed: " + errorMessage);
            }
        });
    }

    @Override
    public void signInWithGoogle(String idToken) {
        authManager.signInWithGoogle(idToken, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(com.google.firebase.auth.FirebaseUser user) {
                // Retrieve user data from Firebase Realtime Database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = user.getDisplayName() != null ? user.getDisplayName() : "";
                        if (dataSnapshot.exists()) {
                            name = dataSnapshot.child("name").getValue(String.class);
                        }
                        // Save to SharedPreferences
                        authManager.saveUserData(user, name);
                        view.showToast("Google sign-in successful!");
                        view.redirectToMainActivity();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        view.showToast("Failed to retrieve user data: " + databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showToast("Google sign-in failed: " + errorMessage);
            }
        });
    }

    @Override
    public void continueAsGuest() {
        authManager.saveGuestData();
        view.redirectToMainActivity();
    }

    @Override
    public void initiateGoogleSignIn() {
        authManager.initiateGoogleSignIn(() -> view.startGoogleSignInIntent());
    }

    @Override
    public void redirectToSignUpActivity() {
        view.redirectToSignUpActivity();
    }
}