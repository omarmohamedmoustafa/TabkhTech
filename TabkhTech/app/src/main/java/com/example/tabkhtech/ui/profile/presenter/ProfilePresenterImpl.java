package com.example.tabkhtech.ui.profile.presenter;

import android.util.Log;

import com.example.tabkhtech.model.repository.Repository;
import com.example.tabkhtech.ui.profile.view.ProfileView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Presenter for handling profile-related operations, including loading user data and managing sign-out.
 */
public class ProfilePresenterImpl implements ProfilePresenter {
    private static final String TAG = "ProfilePresenterImpl";
    private ProfileView view;

    public ProfilePresenterImpl(ProfileView view) {
        this.view = view;
    }

    @Override
    public void loadUserData() {
        // Delegate to view to handle SharedPreferences and load user data
        view.loadUserData();
    }

    @Override
    public void onUpdatePasswordClicked() {
        view.showUpdatePasswordDialog();
    }

    @Override
    public void onSignOutClicked() {
        view.showSignOutConfirmation();
    }

    @Override
    public void onDestroy() {
        view = null; // Prevent memory leaks
    }
}