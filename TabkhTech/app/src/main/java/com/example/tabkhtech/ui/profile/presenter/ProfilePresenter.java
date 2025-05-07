package com.example.tabkhtech.ui.profile.presenter;

import androidx.lifecycle.LiveData;

//import com.example.tabkhtech.model.pojos.User;

public interface ProfilePresenter {
    void loadUserData();
    void onUpdatePasswordClicked();
    void onSignOutClicked();
    void onDestroy();
}