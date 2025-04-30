package com.example.tabkhtech.model.remote;

import com.example.tabkhtech.model.pojos.Country;

import java.util.List;

public interface CountryNetworkCallback {
    public void onCountrySuccess(List<Country> countries);
    public void onFailure(String errorMessage);
}

