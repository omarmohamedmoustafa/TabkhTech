package com.example.tabkhtech.ui.profile.view;

public interface ProfileView {
    void showUserData(String name, String email, String profileImageUrl, String mealsPlanned, String recipesSaved);
    void showUpdatePasswordDialog();
    void showSignOutConfirmation();
    void navigateToLoginScreen();
    void showError(String message);
    void loadUserData();
}