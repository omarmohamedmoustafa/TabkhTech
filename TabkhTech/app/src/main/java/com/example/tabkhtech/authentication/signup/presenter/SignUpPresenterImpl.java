package com.example.tabkhtech.authentication.signup.presenter;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.authentication.signup.view.SignUpView;
import com.example.tabkhtech.model.pojos.User;
import com.example.tabkhtech.model.repository.Repository;

public class SignUpPresenterImpl implements SignUpPresenter{
    private SignUpView view;
    private Repository repository;

    public SignUpPresenterImpl(SignUpView view, Repository repository) {
        this.view = view;
        this.repository = repository;
    }


    @Override
    public void insertUser(User user) {
        repository.insertUser(user);
    }

    @Override
    public LiveData<User> getUserById(String userId) {
        return repository.getUserById(userId);
    }

    @Override
    public void updateUser(User user) {
        repository.updateUser(user);
    }

}
