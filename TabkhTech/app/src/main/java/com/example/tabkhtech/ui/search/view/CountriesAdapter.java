package com.example.tabkhtech.ui.search.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tabkhtech.R;
import com.example.tabkhtech.model.pojos.Country;

import java.util.List;
import java.util.Map;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> {

    private List<Country> countries;
    private OnCountryClickListener countryClickListener;
    private static final Map<String, String> COUNTRY_TO_CODE = Map.ofEntries(
            Map.entry("American", "US"),   // United States
            Map.entry("British", "GB"),    // United Kingdom
            Map.entry("Canadian", "CA"),   // Canada
            Map.entry("Chinese", "CN"),    // China
            Map.entry("Croatian", "HR"),   // Croatia
            Map.entry("Dutch", "NL"),      // Netherlands
            Map.entry("Egyptian", "EG"),   // Egypt
            Map.entry("Filipino", "PH"),   // Philippines
            Map.entry("French", "FR"),     // France
            Map.entry("Greek", "GR"),      // Greece
            Map.entry("Indian", "IN"),     // India
            Map.entry("Irish", "IE"),      // Ireland
            Map.entry("Italian", "IT"),    // Italy
            Map.entry("Jamaican", "JM"),   // Jamaica
            Map.entry("Japanese", "JP"),   // Japan
            Map.entry("Kenyan", "KE"),     // Kenya
            Map.entry("Malaysian", "MY"),  // Malaysia
            Map.entry("Mexican", "MX"),    // Mexico
            Map.entry("Moroccan", "MA"),   // Morocco
            Map.entry("Polish", "PL"),     // Poland
            Map.entry("Portuguese", "PT"), // Portugal
            Map.entry("Russian", "RU"),    // Russian Federation
            Map.entry("Spanish", "ES"),    // Spain
            Map.entry("Thai", "TH"),       // Thailand
            Map.entry("Tunisian", "TN"),   // Tunisia
            Map.entry("Turkish", "TR"),    // Turkey
            Map.entry("Ukrainian", "UA"),  // Ukraine
            Map.entry("Uruguayan", "UY"),  // Uruguay
            Map.entry("Vietnamese", "VN")  // Vietnam
    );

    public CountriesAdapter(List<Country> countries, OnCountryClickListener countryClickListener) {
        this.countries = countries;
        this.countryClickListener = countryClickListener;
    }

    public void setFilteredList(List<Country> filteredList) {
        this.countries = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.title.setText(country.getStrArea());

        // Get the country code from the map, default to "US" if not found
        String countryCode = COUNTRY_TO_CODE.getOrDefault(country.getStrArea(), "US");
        String countryFlagUrl = "https://flagsapi.com/" + countryCode + "/shiny/64.png";

        Glide.with(holder.itemView.getContext())
                .load(countryFlagUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> countryClickListener.onCountryClick(country));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.searchItemImg);
            title = itemView.findViewById(R.id.searchItemTitle);
        }
    }
}