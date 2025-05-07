//package com.example.tabkhtech.model.remote.firebase;
//
//import android.content.SharedPreferences;
//
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//
//public class FirebaseAuthManager {
//    private FirebaseAuth mAuth;
//    private SharedPreferences sharedPreferences;
//    private GoogleSignInClient mGoogleSignInClient;
//    private String defaultWebClientId;
//
//    public interface AuthCallback {
//        void onSuccess(FirebaseUser user);
//        void onFailure(String errorMessage);
//    }
//
//    public FirebaseAuthManager(FirebaseAuth mAuth, SharedPreferences sharedPreferences, String defaultWebClientId, android.content.Context context) {
//        this.mAuth = mAuth;
//        this.sharedPreferences = sharedPreferences;
//        this.defaultWebClientId = defaultWebClientId;
//
//        // Initialize Google Sign-In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(defaultWebClientId)
//                .requestEmail()
//                .build();
//        this.mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
//    }
//
//    public void signUpWithEmail(String email, String password, AuthCallback callback) {
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        if (user != null) {
//                            callback.onSuccess(user);
//                        } else {
//                            callback.onFailure("No user found after sign-up");
//                        }
//                    } else {
//                        callback.onFailure(task.getException().getMessage());
//                    }
//                });
//    }
//
//    public void signInWithEmail(String email, String password, AuthCallback callback) {
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        if (user != null) {
//                            callback.onSuccess(user);
//                        } else {
//                            callback.onFailure("No user found after sign-in");
//                        }
//                    } else {
//                        callback.onFailure(task.getException().getMessage());
//                    }
//                });
//    }
//
//    public void signInWithGoogle(String idToken, AuthCallback callback) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        if (user != null) {
//                            callback.onSuccess(user);
//                        } else {
//                            callback.onFailure("No user found after Google sign-in");
//                        }
//                    } else {
//                        callback.onFailure(task.getException().getMessage());
//                    }
//                });
//    }
//
//    public void initiateGoogleSignIn(Runnable onSignOutComplete) {
//        mGoogleSignInClient.signOut().addOnCompleteListener(task -> onSignOutComplete.run());
//    }
//
//    public GoogleSignInClient getGoogleSignInClient() {
//        return mGoogleSignInClient;
//    }
//
//    public void saveUserData(FirebaseUser user, String name) {
//        String displayName = name != null ? name : user.getDisplayName();
//        sharedPreferences.edit()
//                .putBoolean("isSignedIn", true)
//                .putBoolean("isGuest", false)
//                .putString("userId", user.getUid())
//                .putString("userName", displayName)
//                .putString("userEmail", user.getEmail())
//                .putString("userProfileImage", user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "")
//                .apply();
//    }
//
//    public void saveGuestData() {
//        sharedPreferences.edit()
//                .putBoolean("isGuest", true)
//                .putBoolean("isSignedIn", false)
//                .putString("userId", "guest")
//                .apply();
//    }
//}

package com.example.tabkhtech.model.remote.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Manages Firebase authentication operations, including email sign-up/sign-in, Google sign-in, and guest mode.
 * Handles saving user data to SharedPreferences for use across the app.
 */
public class FirebaseAuthManager {
    private static final String TAG = "FirebaseAuthManager";
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private GoogleSignInClient mGoogleSignInClient;
    private String defaultWebClientId;

    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(String errorMessage);
    }

    public FirebaseAuthManager(FirebaseAuth mAuth, SharedPreferences sharedPreferences, String defaultWebClientId, Context context) {
        this.mAuth = mAuth;
        this.sharedPreferences = sharedPreferences;
        this.defaultWebClientId = defaultWebClientId;

        // Initialize Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(defaultWebClientId)
                .requestEmail()
                .build();
        this.mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public void signUpWithEmail(String email, String password, AuthCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            callback.onSuccess(user);
                        } else {
                            Log.e(TAG, "No user found after sign-up");
                            callback.onFailure("No user found after sign-up");
                        }
                    } else {
                        Log.e(TAG, "Sign-up failed", task.getException());
                        callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Sign-up failed");
                    }
                });
    }

    public void signInWithEmail(String email, String password, AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            callback.onSuccess(user);
                        } else {
                            Log.e(TAG, "No user found after sign-in");
                            callback.onFailure("No user found after sign-in");
                        }
                    } else {
                        Log.e(TAG, "Sign-in failed", task.getException());
                        callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Sign-in failed");
                    }
                });
    }

    public void signInWithGoogle(String idToken, AuthCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            callback.onSuccess(user);
                        } else {
                            Log.e(TAG, "No user found after Google sign-in");
                            callback.onFailure("No user found after Google sign-in");
                        }
                    } else {
                        Log.e(TAG, "Google sign-in failed", task.getException());
                        callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Google sign-in failed");
                    }
                });
    }

    public void initiateGoogleSignIn(Runnable onStartIntent) {
        onStartIntent.run();
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public void saveUserData(FirebaseUser user, String name) {
        String displayName = name != null ? name : (user.getDisplayName() != null ? user.getDisplayName() : "User");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isSignedIn", true);
        editor.putBoolean("isGuest", false);
        editor.putString("userId", user.getUid());
        editor.putString("name", displayName);
        editor.putString("email", user.getEmail() != null ? user.getEmail() : "");
        editor.putString("profileImage", user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "");
        editor.apply();
    }

    public void saveGuestData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isGuest", true);
        editor.putBoolean("isSignedIn", false);
        editor.putString("userId", "guest");
        editor.putString("name", "Guest");
        editor.putString("email", "");
        editor.putString("profileImage", "");
        editor.apply();
    }
}