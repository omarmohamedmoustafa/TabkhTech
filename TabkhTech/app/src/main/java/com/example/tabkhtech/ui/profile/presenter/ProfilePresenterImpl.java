//package com.example.tabkhtech.ui.profile.presenter;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import androidx.lifecycle.LiveData;
//
//import com.example.tabkhtech.model.pojos.User;
//import com.example.tabkhtech.model.repository.Repository;
//import com.example.tabkhtech.ui.profile.view.ProfileView;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class ProfilePresenterImpl implements ProfilePresenter {
//    private ProfileView view;
//    private FirebaseAuth mAuth;
//    private SharedPreferences sharedPreferences;
//    private Repository repository;
//    public ProfilePresenterImpl(ProfileView view, Context context,Repository repository) {
//        this.view = view;
//        this.mAuth = FirebaseAuth.getInstance();
//        this.sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
//        this.repository = repository;
//    }
//
//    @Override
//    public void loadUserData() {
//        boolean isGuest = sharedPreferences.getBoolean("isGuest", false);
//        boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);
//
//        if (isGuest) {
//            // Handle guest mode
//            view.showUserData("Guest User", "guest@example.com", "", "0", "0");
//            return;
//        }
//
//        // Retrieve user data from SharedPreferences
//        String name = sharedPreferences.getString("userName", "Unknown User");
//        String email = sharedPreferences.getString("userEmail", "No Email");
//        String profileImageUrl = sharedPreferences.getString("userProfileImage", "");
//
//        // Since mealsPlanned and recipesSaved are not stored in SharedPreferences, default to 0
//        view.showUserData(
//                name,
//                email,
//                profileImageUrl,
//                "0",
//                "0"
//        );
//    }
//
//    @Override
//    public void onUpdatePasswordClicked() {
//        if (sharedPreferences.getBoolean("isGuest", false)) {
//            view.showError("Guest users cannot update password");
//            return;
//        }
//        view.showUpdatePasswordDialog();
//    }
//
//    @Override
//    public void onSignOutClicked() {
//        if (sharedPreferences.getBoolean("isGuest", false)) {
//            // Clear guest mode
//            sharedPreferences.edit()
//                    .putBoolean("isGuest", false)
//                    .putBoolean("isSignedIn", false)
//                    .putString("userId", "")
//                    .putString("userName", "")
//                    .putString("userEmail", "")
//                    .putString("userProfileImage", "")
//                    .apply();
//            view.navigateToLoginScreen();
//            return;
//        }
//        view.showSignOutConfirmation();
//    }
//
//    @Override
//    public void onDestroy() {
//        view = null; // Prevent memory leaks
//    }
//
//    @Override
//    public LiveData<User> getUserById(String userId) {
//        return repository.getUserById(userId);
//    }
//}
package com.example.tabkhtech.ui.profile.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.tabkhtech.ui.profile.view.ProfileView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Presenter for handling profile-related operations, including loading user data and managing sign-out.
 */
public class ProfilePresenterImpl implements ProfilePresenter {
    private static final String TAG = "ProfilePresenterImpl";
    private ProfileView view;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    public ProfilePresenterImpl(ProfileView view, Context context) {
        this.view = view;
        this.mAuth = FirebaseAuth.getInstance();
        this.sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
    }

    @Override
    public void loadUserData() {
        boolean isGuest = sharedPreferences.getBoolean("isGuest", false);

        if (isGuest) {
            // Handle guest mode consistent with HomeFragment
            Log.d(TAG, "Loading guest user data");
            view.showUserData("Guest", "", "", "0", "0");
            return;
        }

        // Retrieve user data from SharedPreferences
        String name = sharedPreferences.getString("name", "User");
        String email = sharedPreferences.getString("email", "");
        String profileImageUrl = sharedPreferences.getString("profileImage", "");

        Log.d(TAG, "Loaded user data: name=" + name + ", email=" + email);

        // Display user data (mealsPlanned and recipesSaved default to 0 as per original)
        view.showUserData(name, email, profileImageUrl, "0", "0");
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
                    .putString("userId", "")
                    .putString("name", "")
                    .putString("email", "")
                    .putString("profileImage", "")
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