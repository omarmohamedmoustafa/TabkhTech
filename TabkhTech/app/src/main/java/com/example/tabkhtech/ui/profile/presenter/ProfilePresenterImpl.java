package com.example.tabkhtech.ui.profile.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tabkhtech.ui.profile.view.ProfileView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePresenterImpl implements ProfilePresenter {
    private ProfileView view;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private SharedPreferences sharedPreferences;

    public ProfilePresenterImpl(ProfileView view, Context context) {
        this.view = view;
        this.mAuth = FirebaseAuth.getInstance();
        this.userRef = FirebaseDatabase.getInstance().getReference("users");
        this.sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
    }

    @Override
    public void loadUserData() {
        boolean isGuest = sharedPreferences.getBoolean("isGuest", false);
        boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);

        if (isGuest) {
            // Handle guest mode
            view.showUserData("Guest User", "guest@example.com", "", "0", "0");
            return;
        }

        if (!isSignedIn || mAuth.getCurrentUser() == null) {
            view.showError("Not logged in");
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        String email = currentUser.getEmail();
        String profileImageUrl = currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "";

        // Fetch user data from Realtime Database
        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Assuming HelperClass structure from SignUpActivity
                    String name = dataSnapshot.child("name").getValue(String.class);
                    if (name == null || name.isEmpty()) {
                        // Fallback to FirebaseUser display name (e.g., for Google Sign-In)
                        name = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Unknown User";
                    }
                    // Fetch stats
                    Long mealsPlanned = dataSnapshot.child("stats").child("mealsPlanned").getValue(Long.class);
                    Long recipesSaved = dataSnapshot.child("stats").child("recipesSaved").getValue(Long.class);
                    view.showUserData(
                            name,
                            email,
                            profileImageUrl,
                            mealsPlanned != null ? mealsPlanned.toString() : "0",
                            recipesSaved != null ? recipesSaved.toString() : "0"
                    );
                } else {
                    // Fallback for users without database entry
                    String name = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Unknown User";
                    view.showUserData(name, email, profileImageUrl, "0", "0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.showError("Failed to load user data: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onUpdatePasswordClicked() {
        if (sharedPreferences.getBoolean("isGuest", false)) {
            view.showError("Guest users cannot update password");
            return;
        }
        view.showUpdatePasswordDialog();
    }

    @Override
    public void onSignOutClicked() {
        if (sharedPreferences.getBoolean("isGuest", false)) {
            // Clear guest mode
            sharedPreferences.edit()
                    .putBoolean("isGuest", false)
                    .putBoolean("isSignedIn", false)
                    .apply();
            view.navigateToLoginScreen();
            return;
        }
        view.showSignOutConfirmation();
    }

    @Override
    public void onDestroy() {
        view = null; // Prevent memory leaks
    }
}