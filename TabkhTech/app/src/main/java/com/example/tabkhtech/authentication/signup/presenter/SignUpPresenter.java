package com.example.tabkhtech.authentication.signup.presenter;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.User;

public interface SignUpPresenter {
    void insertUser(User user);
    LiveData<User> getUserById(String userId);
    void updateUser(User user);
}
