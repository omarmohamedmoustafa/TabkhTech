package com.example.tabkhtech.ui.profile.presenter;

public interface ProfilePresenter {
    void loadUserData();
    void onUpdatePasswordClicked();
    void onSignOutClicked();
    void onDestroy();
}