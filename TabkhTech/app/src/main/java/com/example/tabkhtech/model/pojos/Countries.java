package com.example.tabkhtech.model.pojos;

import java.util.List;

public class Countries {
    private List<Country> countries;

    public List<Country> getCountries() {
        return countries;
    }

    @Override
    public String toString() {
        return "Countries{" +
                "countries=" + countries +
                '}';
    }

    public Countries(List<Country> countries) {
        this.countries = countries;
    }
    private class Country {
        private String strArea;

        public Country(String strArea) {
            this.strArea = strArea;
        }

        public String getName() { return strArea; }

        @Override
        public String toString() {
            return "Country{" +
                    "name='" + strArea + '\'' +
                    '}';
        }
    }

}
