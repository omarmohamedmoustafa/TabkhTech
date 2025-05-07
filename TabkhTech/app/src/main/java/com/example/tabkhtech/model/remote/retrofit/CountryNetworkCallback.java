package com.example.tabkhtech.model.remote.retrofit;

import com.example.tabkhtech.model.pojos.Country;

import java.util.List;

public interface CountryNetworkCallback {
    public void onCountrySuccess(List<Country> countries);
    public void onFailure(String errorMessage);
}

