////package com.example.tabkhtech.ui.home.view;
////
////import android.content.Context;
////import android.content.SharedPreferences;
////import android.net.ConnectivityManager;
////import android.net.Network;
////import android.net.NetworkCapabilities;
////import android.net.NetworkInfo;
////import android.net.NetworkRequest;
////import android.os.Build;
////import android.os.Bundle;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.ImageView;
////import android.widget.LinearLayout;
////import android.widget.TextView;
////import android.widget.Toast;
////
////import androidx.annotation.NonNull;
////import androidx.annotation.Nullable;
////import androidx.cardview.widget.CardView;
////import androidx.fragment.app.Fragment;
////import androidx.fragment.app.FragmentContainerView;
////import androidx.fragment.app.FragmentManager;
////import androidx.fragment.app.FragmentTransaction;
////import androidx.recyclerview.widget.LinearLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////
////import com.bumptech.glide.Glide;
////import com.example.tabkhtech.R;
////import com.example.tabkhtech.model.pojos.Meal;
////import com.example.tabkhtech.ui.home.presenter.HomePresenter;
////import com.example.tabkhtech.ui.home.presenter.HomePresenterImpl;
////import com.example.tabkhtech.model.local.MealLocalDataSourceImpl;
////import com.example.tabkhtech.model.remote.retrofit.MealRemoteDataSourceImpl;
////import com.example.tabkhtech.model.repository.RepositoryImpl;
////import com.example.tabkhtech.ui.detailed_meal.view.MealFragment;
////import com.google.common.reflect.TypeToken;
////import com.google.firebase.auth.FirebaseAuth;
////import com.google.firebase.auth.FirebaseUser;
////import com.google.firebase.database.DataSnapshot;
////import com.google.firebase.database.DatabaseError;
////import com.google.firebase.database.DatabaseReference;
////import com.google.firebase.database.FirebaseDatabase;
////import com.google.firebase.database.ValueEventListener;
////import com.google.gson.Gson;
////
////import java.lang.reflect.Type;
////import java.text.SimpleDateFormat;
////import java.util.ArrayList;
////import java.util.Date;
////import java.util.Locale;
////
////public class HomeViewImpl extends Fragment implements HomeView, OnMealClickListener {
////
////    private ImageView mealImage;
////    private TextView mealName;
////    private TextView greetingText;
////    private RecyclerView rv;
////    private HomeAdapter adapter;
////    private HomePresenter presenter;
////    private Meal currentMeal;
////    private MealFragment mealFrag;
////    private FragmentManager fragmentManager;
////    private FragmentTransaction fragmentTransaction;
////    private FragmentContainerView fragmentContainerView;
////    private CardView randomMealCard;
////    private SharedPreferences sharedPreferences;
////    private String userId;
////    private ConnectivityManager connectivityManager;
////    private ConnectivityManager.NetworkCallback networkCallback;
////    private boolean isConnected;
////    private View rootView;
////
////    // SharedPreferences keys for Meal of the Day
////    private static final String PREF_MEAL_DATE = "meal_date";
////    private static final String PREF_MEAL_ID = "meal_id";
////    private static final String PREF_MEAL_NAME = "meal_name";
////    private static final String PREF_MEAL_IMAGE = "meal_image";
////    private static final String PREF_MEAL_CATEGORY = "meal_category";
////    private static final String PREF_MEAL_AREA = "meal_area";
////    private static final String PREF_MEAL_INSTRUCTIONS = "meal_instructions";
////    private static final String PREF_MEAL_YOUTUBE = "meal_youtube";
////
////    private void saveMealOfTheDay(Meal meal) {
////        if (sharedPreferences == null || meal == null) return;
////        SharedPreferences.Editor editor = sharedPreferences.edit();
////        String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
////
////        editor.putString(PREF_MEAL_DATE, currentDate);
////        editor.putString(PREF_MEAL_ID, meal.getIdMeal());
////        editor.putString(PREF_MEAL_NAME, meal.getStrMeal());
////        editor.putString(PREF_MEAL_IMAGE, meal.getStrMealThumb());
////        editor.putString(PREF_MEAL_CATEGORY, meal.getStrCategory());
////        editor.putString(PREF_MEAL_AREA, meal.getStrArea());
////        editor.putString(PREF_MEAL_INSTRUCTIONS, meal.getStrInstructions());
////        editor.putString(PREF_MEAL_YOUTUBE, meal.getStrYoutube());
////
////        // Save ingredients and measures as JSON strings
////        Gson gson = new Gson();
////        String ingredientsJson = gson.toJson(meal.getIngredients());
////        String measuresJson = gson.toJson(meal.getMeasures());
////        editor.putString("meal_ingredients", ingredientsJson);
////        editor.putString("meal_measures", measuresJson);
////
////        editor.apply();
////    }
////
////    private Meal loadMealOfTheDay() {
////        if (sharedPreferences == null) return null;
////        String savedDate = sharedPreferences.getString(PREF_MEAL_DATE, "");
////        String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
////
////        // If dates don't match, return null (meal is from a previous day)
////        if (!savedDate.equals(currentDate)) {
////            return null;
////        }
////
////        Meal meal = new Meal();
////        meal.setIdMeal(sharedPreferences.getString(PREF_MEAL_ID, ""));
////        meal.setStrMeal(sharedPreferences.getString(PREF_MEAL_NAME, ""));
////        meal.setStrMealThumb(sharedPreferences.getString(PREF_MEAL_IMAGE, ""));
////        meal.setStrCategory(sharedPreferences.getString(PREF_MEAL_CATEGORY, ""));
////        meal.setStrArea(sharedPreferences.getString(PREF_MEAL_AREA, ""));
////        meal.setStrInstructions(sharedPreferences.getString(PREF_MEAL_INSTRUCTIONS, ""));
////        meal.setStrYoutube(sharedPreferences.getString(PREF_MEAL_YOUTUBE, ""));
////
////        // Load ingredients and measures from JSON
////        Gson gson = new Gson();
////        String ingredientsJson = sharedPreferences.getString("meal_ingredients", "");
////        String measuresJson = sharedPreferences.getString("meal_measures", "");
////        if (!ingredientsJson.isEmpty()) {
////            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
////            meal.setIngredients(gson.fromJson(ingredientsJson, listType));
////        }
////        if (!measuresJson.isEmpty()) {
////            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
////            meal.setMeasures(gson.fromJson(measuresJson, listType));
////        }
////
////        return meal.getIdMeal().isEmpty() ? null : meal;
////    }
////
////    public boolean isInternetConnected(Context context) {
////        ConnectivityManager connectivityManager =
////                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
////
////        if (connectivityManager == null) {
////            return false;
////        }
////
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////            Network network = connectivityManager.getActiveNetwork();
////            if (network == null) {
////                return false;
////            }
////            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
////            return capabilities != null &&
////                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
////                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
////                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
////        } else {
////            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
////            return networkInfo != null && networkInfo.isConnected();
////        }
////    }
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        // Initialize connectivity manager
////        connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
////        isConnected = isInternetConnected(requireContext());
////
////        // Inflate layout based on connectivity
////        rootView = inflater.inflate(isConnected ? R.layout.fragment_home : R.layout.disconnected, container, false);
////
////        // Initialize SharedPreferences
////        sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
////        userId = sharedPreferences.getString("userId", "guest");
////
////        // Initialize presenter
////        presenter = new HomePresenterImpl(
////                RepositoryImpl.getInstance(MealLocalDataSourceImpl.getInstance(getContext()),
////                        MealRemoteDataSourceImpl.getInstance()),
////                this
////        );
////
////        // Load saved Meal of the Day
////        currentMeal = loadMealOfTheDay();
////        if (currentMeal == null && isConnected) {
////            presenter.getMealOfTheDay(); // Fetch new meal if none saved or outdated
////        }
////
////        // Set up network callback for real-time monitoring
////        setupNetworkCallback();
////
////        return rootView;
////    }
////
////    @Override
////    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////        super.onViewCreated(view, savedInstanceState);
////
////        // Initialize UI elements
////        mealImage = view.findViewById(R.id.itemImg);
////        mealName = view.findViewById(R.id.itemName);
////        greetingText = view.findViewById(R.id.greeting_text);
////        rv = view.findViewById(R.id.recentMealRV);
////        fragmentContainerView = view.findViewById(R.id.homeFragmentContainer);
////        randomMealCard = view.findViewById(R.id.randomMealCard);
////        fragmentManager = getParentFragmentManager();
////
////        // Initialize RecyclerView if present
////        if (rv != null) {
////            rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
////            adapter = new HomeAdapter(new ArrayList<>(), this);
////            rv.setAdapter(adapter);
////        }
////
////        // Display saved or fetched Meal of the Day
////        if (currentMeal != null && mealName != null && mealImage != null && randomMealCard != null) {
////            mealName.setText(currentMeal.getStrMeal());
////            Glide.with(this)
////                    .load(currentMeal.getStrMealThumb())
////                    .into(mealImage);
////            randomMealCard.setVisibility(View.VISIBLE);
////        } else if (!isConnected && mealName != null && mealImage != null && randomMealCard != null) {
////            // Show placeholder when offline and no saved meal
////            mealName.setText("No meal available offline");
////            mealImage.setImageResource(R.drawable.bg_google_button);
////            randomMealCard.setVisibility(View.GONE);
////        }
////
////        // Set click listener for Meal of the Day
////        if (randomMealCard != null) {
////            randomMealCard.setOnClickListener(v -> {
////                if (currentMeal != null) {
////                    onMealClick(currentMeal);
////                }
////            });
////        }
////
////        // Fetch recent meals if connected and RecyclerView exists
////        if (isConnected && presenter != null && rv != null) {
////            presenter.getAllRecentMeals(userId, 10).observe(getViewLifecycleOwner(), recentMeals -> {
////                if (recentMeals != null && !recentMeals.isEmpty()) {
////                    adapter.updateMeals(recentMeals);
////                    rv.setVisibility(View.VISIBLE);
////                } else {
////                    rv.setVisibility(View.GONE);
////                }
////            });
////        } else if (rv != null) {
////            rv.setVisibility(View.GONE);
////        }
////
////        // Handle user greeting
////        if (greetingText != null) {
////            boolean isGuest = sharedPreferences.getBoolean("isGuest", false);
////            boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);
////
////            if (isGuest) {
////                greetingText.setText("Hello Guest");
////            } else if (isSignedIn && FirebaseAuth.getInstance().getCurrentUser() != null) {
////                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
////                String firebaseUserId = currentUser.getUid();
////                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseUserId);
////
////                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(DataSnapshot dataSnapshot) {
////                        if (dataSnapshot.exists()) {
////                            String name = dataSnapshot.child("name").getValue(String.class);
////                            if (name == null || name.isEmpty()) {
////                                name = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "User";
////                            }
////                            greetingText.setText("Hello " + name);
////                        } else {
////                            String name = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "User";
////                            greetingText.setText("Hello " + name);
////                        }
////                    }
////
////                    @Override
////                    public void onCancelled(DatabaseError databaseError) {
////                        greetingText.setText("Hello User");
////                        if (isConnected) {
////                            Toast.makeText(getContext(), "Failed to load user name", Toast.LENGTH_SHORT).show();
////                        }
////                    }
////                });
////            } else {
////                greetingText.setText("Hello User");
////            }
////        }
////    }
////
////    private void setupNetworkCallback() {
////        networkCallback = new ConnectivityManager.NetworkCallback() {
////            @Override
////            public void onAvailable(@NonNull Network network) {
////                requireActivity().runOnUiThread(() -> {
////                    if (!isConnected) {
////                        isConnected = true;
////                        reloadFragment();
////                    }
////                });
////            }
////
////            @Override
////            public void onLost(@NonNull Network network) {
////                requireActivity().runOnUiThread(() -> {
////                    if (isConnected) {
////                        isConnected = false;
////                        reloadFragment();
////                    }
////                });
////            }
////        };
////
////        NetworkRequest networkRequest = new NetworkRequest.Builder()
////                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
////                .build();
////        if (connectivityManager != null) {
////            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
////        }
////    }
////
////    private void reloadFragment() {
////        if (getParentFragmentManager() != null) {
////            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
////            transaction.detach(this).attach(this).commit();
////        }
////    }
////
////    @Override
////    public void showRandomMeal(Meal meal) {
////        if (meal == null) return;
////        currentMeal = meal;
////        saveMealOfTheDay(meal);
////        if (mealName != null && mealImage != null && randomMealCard != null) {
////            mealName.setText(meal.getStrMeal());
////            Glide.with(this)
////                    .load(meal.getStrMealThumb())
////                    .into(mealImage);
////            randomMealCard.setVisibility(View.VISIBLE);
////        }
////    }
////
////    @Override
////    public void showError(String errorMessage) {
////        if (isConnected && getContext() != null) {
////            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
////        }
////    }
////
////    @Override
////    public void onMealClick(Meal meal) {
////        if (meal == null || fragmentManager == null || fragmentContainerView == null) return;
////        mealFrag = new MealFragment();
////        Bundle args = new Bundle();
////        args.putString("id", meal.getIdMeal());
////        args.putString("imageUrl", meal.getStrMealThumb());
////        args.putString("name", meal.getStrMeal());
////        args.putString("category", meal.getStrCategory());
////        args.putString("area", meal.getStrArea());
////        args.putString("instructions", meal.getStrInstructions());
////        args.putString("youtube", meal.getStrYoutube());
////        args.putBoolean("fromFavorites", false);
////        args.putStringArrayList("ingredients", new ArrayList<>(meal.getIngredients()));
////        args.putStringArrayList("measures", new ArrayList<>(meal.getMeasures()));
////
////        mealFrag.setArguments(args);
////        fragmentTransaction = fragmentManager.beginTransaction();
////        fragmentTransaction.replace(R.id.homeFragmentContainer, mealFrag);
////        fragmentTransaction.addToBackStack(null);
////        fragmentTransaction.commit();
////        fragmentContainerView.setVisibility(View.VISIBLE);
////    }
////
////    @Override
////    public void onResume() {
////        super.onResume();
////    }
////
////    @Override
////    public void onDestroyView() {
////        super.onDestroyView();
////        if (fragmentContainerView != null) {
////            fragmentContainerView.setVisibility(View.GONE);
////        }
////        // Unregister network callback to prevent leaks
////        if (connectivityManager != null && networkCallback != null) {
////            connectivityManager.unregisterNetworkCallback(networkCallback);
////            networkCallback = null;
////        }
////    }
//////    public void handleBackPress() {
//////        FragmentContainerView fragmentContainer = rootView.findViewById(R.id.homeFragmentContainer);
//////        LinearLayout mainContent = rootView.findViewById(R.id.mainContentLayout);
//////        if (fragmentContainer != null && fragmentContainer.getVisibility() == View.VISIBLE) {
//////            fragmentContainer.setVisibility(View.GONE);
//////            mainContent.setVisibility(View.VISIBLE);
//////            getChildFragmentManager().popBackStack();
//////        } else {
//////            requireActivity().onBackPressed();
//////        }
//////    }
////    public boolean handleBackPress() {
////        FragmentContainerView fragmentContainer = rootView.findViewById(R.id.homeFragmentContainer);
////        LinearLayout mainContent = rootView.findViewById(R.id.mainContentLayout);
////        if (fragmentContainer != null && fragmentContainer.getVisibility() == View.VISIBLE) {
////            fragmentContainer.setVisibility(View.GONE);
////            mainContent.setVisibility(View.VISIBLE);
////            getChildFragmentManager().popBackStack();
////            return true; // Back press handled
////        }
////        return false; // Back press not handled, terminate app
////    }
////
////}
//package com.example.tabkhtech.ui.home.view;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.net.ConnectivityManager;
//import android.net.Network;
//import android.net.NetworkCapabilities;
//import android.net.NetworkInfo;
//import android.net.NetworkRequest;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.cardview.widget.CardView;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentContainerView;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.tabkhtech.R;
//import com.example.tabkhtech.model.local.MealLocalDataSourceImpl;
//import com.example.tabkhtech.model.pojos.Meal;
////import com.example.tabkhtech.model.pojos.User;
//import com.example.tabkhtech.model.remote.retrofit.MealRemoteDataSourceImpl;
//import com.example.tabkhtech.model.repository.RepositoryImpl;
//import com.example.tabkhtech.ui.detailed_meal.view.MealFragment;
//import com.example.tabkhtech.ui.home.presenter.HomePresenter;
//import com.example.tabkhtech.ui.home.presenter.HomePresenterImpl;
//import com.google.common.reflect.TypeToken;
//import com.google.gson.Gson;
//
//import java.lang.reflect.Type;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Locale;
//
//public class HomeViewImpl extends Fragment implements HomeView, OnMealClickListener {
//
//    private ImageView mealImage;
//    private TextView mealName;
//    private TextView greetingText;
//    private RecyclerView rv;
//    private HomeAdapter adapter;
//    private HomePresenter presenter;
//    private Meal currentMeal;
//    private MealFragment mealFrag;
//    private FragmentManager fragmentManager;
//    private FragmentTransaction fragmentTransaction;
//    private FragmentContainerView fragmentContainerView;
//    private CardView randomMealCard;
//    private SharedPreferences sharedPreferences;
//    private String userId;
//    private ConnectivityManager connectivityManager;
//    private ConnectivityManager.NetworkCallback networkCallback;
//    private boolean isConnected;
//    private View rootView;
//
//    // SharedPreferences keys for Meal of the Day
//    private static final String PREF_MEAL_DATE = "meal_date";
//    private static final String PREF_MEAL_ID = "meal_id";
//    private static final String PREF_MEAL_NAME = "meal_name";
//    private static final String PREF_MEAL_IMAGE = "meal_image";
//    private static final String PREF_MEAL_CATEGORY = "meal_category";
//    private static final String PREF_MEAL_AREA = "meal_area";
//    private static final String PREF_MEAL_INSTRUCTIONS = "meal_instructions";
//    private static final String PREF_MEAL_YOUTUBE = "meal_youtube";
//
//    private void saveMealOfTheDay(Meal meal) {
//        if (sharedPreferences == null || meal == null) return;
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
//
//        editor.putString(PREF_MEAL_DATE, currentDate);
//        editor.putString(PREF_MEAL_ID, meal.getIdMeal());
//        editor.putString(PREF_MEAL_NAME, meal.getStrMeal());
//        editor.putString(PREF_MEAL_IMAGE, meal.getStrMealThumb());
//        editor.putString(PREF_MEAL_CATEGORY, meal.getStrCategory());
//        editor.putString(PREF_MEAL_AREA, meal.getStrArea());
//        editor.putString(PREF_MEAL_INSTRUCTIONS, meal.getStrInstructions());
//        editor.putString(PREF_MEAL_YOUTUBE, meal.getStrYoutube());
//
//        // Save ingredients and measures as JSON strings
//        Gson gson = new Gson();
//        String ingredientsJson = gson.toJson(meal.getIngredients());
//        String measuresJson = gson.toJson(meal.getMeasures());
//        editor.putString("meal_ingredients", ingredientsJson);
//        editor.putString("meal_measures", measuresJson);
//
//        editor.apply();
//    }
//
//    private Meal loadMealOfTheDay() {
//        if (sharedPreferences == null) return null;
//        String savedDate = sharedPreferences.getString(PREF_MEAL_DATE, "");
//        String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
//
//        // If dates don't match, return null (meal is from a previous day)
//        if (!savedDate.equals(currentDate)) {
//            return null;
//        }
//
//        Meal meal = new Meal();
//        meal.setIdMeal(sharedPreferences.getString(PREF_MEAL_ID, ""));
//        meal.setStrMeal(sharedPreferences.getString(PREF_MEAL_NAME, ""));
//        meal.setStrMealThumb(sharedPreferences.getString(PREF_MEAL_IMAGE, ""));
//        meal.setStrCategory(sharedPreferences.getString(PREF_MEAL_CATEGORY, ""));
//        meal.setStrArea(sharedPreferences.getString(PREF_MEAL_AREA, ""));
//        meal.setStrInstructions(sharedPreferences.getString(PREF_MEAL_INSTRUCTIONS, ""));
//        meal.setStrYoutube(sharedPreferences.getString(PREF_MEAL_YOUTUBE, ""));
//
//        // Load ingredients and measures from JSON
//        Gson gson = new Gson();
//        String ingredientsJson = sharedPreferences.getString("meal_ingredients", "");
//        String measuresJson = sharedPreferences.getString("meal_measures", "");
//        if (!ingredientsJson.isEmpty()) {
//            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
//            meal.setIngredients(gson.fromJson(ingredientsJson, listType));
//        }
//        if (!measuresJson.isEmpty()) {
//            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
//            meal.setMeasures(gson.fromJson(measuresJson, listType));
//        }
//
//        return meal.getIdMeal().isEmpty() ? null : meal;
//    }
//
//    public boolean isInternetConnected(Context context) {
//        ConnectivityManager connectivityManager =
//                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        if (connectivityManager == null) {
//            return false;
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Network network = connectivityManager.getActiveNetwork();
//            if (network == null) {
//                return false;
//            }
//            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
//            return capabilities != null &&
//                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
//                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
//                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
//        } else {
//            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//            return networkInfo != null && networkInfo.isConnected();
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Initialize connectivity manager
//        connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        isConnected = isInternetConnected(requireContext());
//
//        // Inflate layout based on connectivity
//        rootView = inflater.inflate(isConnected ? R.layout.fragment_home : R.layout.disconnected, container, false);
//
//        // Initialize SharedPreferences
//        sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
//        userId = sharedPreferences.getString("userId", "guest");
//
//        // Initialize presenter
//        presenter = new HomePresenterImpl(
//                RepositoryImpl.getInstance(MealLocalDataSourceImpl.getInstance(getContext()),
//                        MealRemoteDataSourceImpl.getInstance()),
//                this
//        );
//
//        // Load saved Meal of the Day
//        currentMeal = loadMealOfTheDay();
//        if (currentMeal == null && isConnected) {
//            presenter.getMealOfTheDay(); // Fetch new meal if none saved or outdated
//        }
//
//        // Set up network callback for real-time monitoring
//        setupNetworkCallback();
//
//        return rootView;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // Initialize UI elements
//        mealImage = view.findViewById(R.id.itemImg);
//        mealName = view.findViewById(R.id.itemName);
//        greetingText = view.findViewById(R.id.greeting_text);
//        rv = view.findViewById(R.id.recentMealRV);
//        fragmentContainerView = view.findViewById(R.id.homeFragmentContainer);
//        randomMealCard = view.findViewById(R.id.randomMealCard);
//        fragmentManager = getParentFragmentManager();
//
//        // Initialize RecyclerView if present
//        if (rv != null) {
//            rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//            adapter = new HomeAdapter(new ArrayList<>(), this);
//            rv.setAdapter(adapter);
//        }
//
//        // Display saved or fetched Meal of the Day
//        if (currentMeal != null && mealName != null && mealImage != null && randomMealCard != null) {
//            mealName.setText(currentMeal.getStrMeal());
//            Glide.with(this)
//                    .load(currentMeal.getStrMealThumb())
//                    .into(mealImage);
//            randomMealCard.setVisibility(View.VISIBLE);
//        } else if (!isConnected && mealName != null && mealImage != null && randomMealCard != null) {
//            // Show placeholder when offline and no saved meal
//            mealName.setText("No meal available offline");
//            mealImage.setImageResource(R.drawable.bg_google_button);
//            randomMealCard.setVisibility(View.GONE);
//        }
//
//        // Set click listener for Meal of the Day
//        if (randomMealCard != null) {
//            randomMealCard.setOnClickListener(v -> {
//                if (currentMeal != null) {
//                    onMealClick(currentMeal);
//                }
//            });
//        }
//
//        // Fetch recent meals if connected and RecyclerView exists
//        if (isConnected && presenter != null && rv != null) {
//            presenter.getAllRecentMeals(userId, 10).observe(getViewLifecycleOwner(), recentMeals -> {
//                if (recentMeals != null && !recentMeals.isEmpty()) {
//                    adapter.updateMeals(recentMeals);
//                    rv.setVisibility(View.VISIBLE);
//                } else {
//                    rv.setVisibility(View.GONE);
//                }
//            });
//        } else if (rv != null) {
//            rv.setVisibility(View.GONE);
//        }
//
//        // Handle user greeting
//        if (greetingText != null) {
//            boolean isGuest = sharedPreferences.getBoolean("isGuest", false);
//            boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);
//
//            if (isGuest) {
//                greetingText.setText("Hello Guest");
//            } else if (isSignedIn) {
//                // Fetch user from Room via presenter
//                presenter.getUserById(userId).observe(getViewLifecycleOwner(), user -> {
//                    String name = user != null && user.getName() != null ? user.getName() : "User";
//                    greetingText.setText("Hello " + name);
//                });
//            } else {
//                greetingText.setText("Hello User");
//            }
//        }
//    }
//
//    private void setupNetworkCallback() {
//        networkCallback = new ConnectivityManager.NetworkCallback() {
//            @Override
//            public void onAvailable(@NonNull Network network) {
//                requireActivity().runOnUiThread(() -> {
//                    if (!isConnected) {
//                        isConnected = true;
//                        reloadFragment();
//                    }
//                });
//            }
//
//            @Override
//            public void onLost(@NonNull Network network) {
//                requireActivity().runOnUiThread(() -> {
//                    if (!isConnected) {
//                        isConnected = false;
//                        reloadFragment();
//                    }
//                });
//            }
//        };
//
//        NetworkRequest networkRequest = new NetworkRequest.Builder()
//                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//                .build();
//        if (connectivityManager != null) {
//            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
//        }
//    }
//
//    private void reloadFragment() {
//        if (getParentFragmentManager() != null) {
//            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//            transaction.detach(this).attach(this).commit();
//        }
//    }
//
//    @Override
//    public void showRandomMeal(Meal meal) {
//        if (meal == null) return;
//        currentMeal = meal;
//        saveMealOfTheDay(meal);
//        if (mealName != null && mealImage != null && randomMealCard != null) {
//            mealName.setText(meal.getStrMeal());
//            Glide.with(this)
//                    .load(meal.getStrMealThumb())
//                    .into(mealImage);
//            randomMealCard.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void showError(String errorMessage) {
//        if (isConnected && getContext() != null) {
//            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onMealClick(Meal meal) {
//        if (meal == null || fragmentManager == null || fragmentContainerView == null) return;
//        mealFrag = new MealFragment();
//        Bundle args = new Bundle();
//        args.putString("id", meal.getIdMeal());
//        args.putString("imageUrl", meal.getStrMealThumb());
//        args.putString("name", meal.getStrMeal());
//        args.putString("category", meal.getStrCategory());
//        args.putString("area", meal.getStrArea());
//        args.putString("instructions", meal.getStrInstructions());
//        args.putString("youtube", meal.getStrYoutube());
//        args.putBoolean("fromFavorites", false);
//        args.putStringArrayList("ingredients", new ArrayList<>(meal.getIngredients()));
//        args.putStringArrayList("measures", new ArrayList<>(meal.getMeasures()));
//
//        mealFrag.setArguments(args);
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.homeFragmentContainer, mealFrag);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//        fragmentContainerView.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (fragmentContainerView != null) {
//            fragmentContainerView.setVisibility(View.GONE);
//        }
//        // Unregister network callback to prevent leaks
//        if (connectivityManager != null && networkCallback != null) {
//            connectivityManager.unregisterNetworkCallback(networkCallback);
//            networkCallback = null;
//        }
//    }
//
//    public boolean handleBackPress() {
//        FragmentContainerView fragmentContainer = rootView.findViewById(R.id.homeFragmentContainer);
//        LinearLayout mainContent = rootView.findViewById(R.id.mainContentLayout);
//        if (fragmentContainer != null && fragmentContainer.getVisibility() == View.VISIBLE) {
//            fragmentContainer.setVisibility(View.GONE);
//            mainContent.setVisibility(View.VISIBLE);
//            getChildFragmentManager().popBackStack();
//            return true; // Back press handled
//        }
//        return false; // Back press not handled, terminate app
//    }
//}

package com.example.tabkhtech.ui.home.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tabkhtech.R;
import com.example.tabkhtech.model.local.MealLocalDataSourceImpl;
import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.remote.retrofit.MealRemoteDataSourceImpl;
import com.example.tabkhtech.model.repository.RepositoryImpl;
import com.example.tabkhtech.ui.detailed_meal.view.MealFragment;
import com.example.tabkhtech.ui.home.presenter.HomePresenter;
import com.example.tabkhtech.ui.home.presenter.HomePresenterImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeViewImpl extends Fragment implements HomeView, OnMealClickListener {

    private ImageView mealImage;
    private TextView mealName;
    private TextView greetingText;
    private RecyclerView rv;
    private HomeAdapter adapter;
    private HomePresenter presenter;
    private Meal currentMeal;
    private MealFragment mealFrag;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragmentContainerView fragmentContainerView;
    private CardView randomMealCard;
    private SharedPreferences sharedPreferences;
    private String userId;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private boolean isConnected;
    private View rootView;
    private ImageView guestViewOverlay;
    private boolean isGuest;
    // SharedPreferences keys for Meal of the Day
    private static final String PREF_MEAL_DATE = "meal_date";
    private static final String PREF_MEAL_ID = "meal_id";
    private static final String PREF_MEAL_NAME = "meal_name";
    private static final String PREF_MEAL_IMAGE = "meal_image";
    private static final String PREF_MEAL_CATEGORY = "meal_category";
    private static final String PREF_MEAL_AREA = "meal_area";
    private static final String PREF_MEAL_INSTRUCTIONS = "meal_instructions";
    private static final String PREF_MEAL_YOUTUBE = "meal_youtube";

    private void saveMealOfTheDay(Meal meal) {
        if (sharedPreferences == null || meal == null) return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());

        editor.putString(PREF_MEAL_DATE, currentDate);
        editor.putString(PREF_MEAL_ID, meal.getIdMeal());
        editor.putString(PREF_MEAL_NAME, meal.getStrMeal());
        editor.putString(PREF_MEAL_IMAGE, meal.getStrMealThumb());
        editor.putString(PREF_MEAL_CATEGORY, meal.getStrCategory());
        editor.putString(PREF_MEAL_AREA, meal.getStrArea());
        editor.putString(PREF_MEAL_INSTRUCTIONS, meal.getStrInstructions());
        editor.putString(PREF_MEAL_YOUTUBE, meal.getStrYoutube());

        // Save ingredients and measures as JSON strings
        Gson gson = new Gson();
        String ingredientsJson = gson.toJson(meal.getIngredients());
        String measuresJson = gson.toJson(meal.getMeasures());
        editor.putString("meal_ingredients", ingredientsJson);
        editor.putString("meal_measures", measuresJson);

        editor.apply();
    }

    private Meal loadMealOfTheDay() {
        if (sharedPreferences == null) return null;
        String savedDate = sharedPreferences.getString(PREF_MEAL_DATE, "");
        String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());

        // If dates don't match, return null (meal is from a previous day)
        if (!savedDate.equals(currentDate)) {
            return null;
        }

        Meal meal = new Meal();
        meal.setIdMeal(sharedPreferences.getString(PREF_MEAL_ID, ""));
        meal.setStrMeal(sharedPreferences.getString(PREF_MEAL_NAME, ""));
        meal.setStrMealThumb(sharedPreferences.getString(PREF_MEAL_IMAGE, ""));
        meal.setStrCategory(sharedPreferences.getString(PREF_MEAL_CATEGORY, ""));
        meal.setStrArea(sharedPreferences.getString(PREF_MEAL_AREA, ""));
        meal.setStrInstructions(sharedPreferences.getString(PREF_MEAL_INSTRUCTIONS, ""));
        meal.setStrYoutube(sharedPreferences.getString(PREF_MEAL_YOUTUBE, ""));

        // Load ingredients and measures from JSON
        Gson gson = new Gson();
        String ingredientsJson = sharedPreferences.getString("meal_ingredients", "");
        String measuresJson = sharedPreferences.getString("meal_measures", "");
        if (!ingredientsJson.isEmpty()) {
            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
            meal.setIngredients(gson.fromJson(ingredientsJson, listType));
        }
        if (!measuresJson.isEmpty()) {
            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
            meal.setMeasures(gson.fromJson(measuresJson, listType));
        }

        return meal.getIdMeal().isEmpty() ? null : meal;
    }

    public boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize connectivity manager
        connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        isConnected = isInternetConnected(requireContext());

        // Inflate layout based on connectivity
        rootView = inflater.inflate(isConnected ? R.layout.fragment_home : R.layout.disconnected, container, false);

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "guest");
        isGuest = sharedPreferences.getBoolean("isGuest", false);

        // Initialize presenter
        presenter = new HomePresenterImpl(
                RepositoryImpl.getInstance(MealLocalDataSourceImpl.getInstance(getContext()),
                        MealRemoteDataSourceImpl.getInstance()),
                this
        );

        // Load saved Meal of the Day
        currentMeal = loadMealOfTheDay();
        if (currentMeal == null && isConnected) {
            presenter.getMealOfTheDay(); // Fetch new meal if none saved or outdated
        }

        // Set up network callback for real-time monitoring
        setupNetworkCallback();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable

    Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        mealImage = view.findViewById(R.id.itemImg);
        mealName = view.findViewById(R.id.itemName);
        greetingText = view.findViewById(R.id.greeting_text);
        rv = view.findViewById(R.id.recentMealRV);
        fragmentContainerView = view.findViewById(R.id.homeFragmentContainer);
        randomMealCard = view.findViewById(R.id.randomMealCard);
        fragmentManager = getParentFragmentManager();
        guestViewOverlay = view.findViewById(R.id.guestViewOverlay);
        // Initialize RecyclerView if present
        if (rv != null) {
            rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            adapter = new HomeAdapter(new ArrayList<>(), this);
            rv.setAdapter(adapter);
        }

        // Display saved or fetched Meal of the Day
        if (currentMeal != null && mealName != null && mealImage != null && randomMealCard != null) {
            mealName.setText(currentMeal.getStrMeal());
            Glide.with(this)
                    .load(currentMeal.getStrMealThumb())
                    .placeholder(R.drawable.meal)
                    .error(R.drawable.meal)
                    .into(mealImage);
            randomMealCard.setVisibility(View.VISIBLE);
        } else if (!isConnected && mealName != null && mealImage != null && randomMealCard != null) {
            // Show placeholder when offline and no saved meal
            mealName.setText("No meal available offline");
            mealImage.setImageResource(R.drawable.bg_google_button);
            randomMealCard.setVisibility(View.GONE);
        }

        // Set click listener for Meal of the Day
        if (randomMealCard != null) {
            randomMealCard.setOnClickListener(v -> {
                if (currentMeal != null) {
                    onMealClick(currentMeal);
                }
            });
        }

//        // Fetch recent meals if connected and RecyclerView exists
//        if (isConnected && presenter != null && rv != null && !isGuest) {
//            presenter.getAllRecentMeals(userId, 10).observe(getViewLifecycleOwner(), recentMeals -> {
//                if (recentMeals != null && !recentMeals.isEmpty()) {
//                    adapter.updateMeals(recentMeals);
//                    rv.setVisibility(View.VISIBLE);
//                } else {
//                    rv.setVisibility(View.GONE);
//                }
//            });
//        } else if (rv != null) {
//            rv.setVisibility(View.GONE);
//        }
        if (isGuest && rv != null && guestViewOverlay != null) {
            rv.setVisibility(View.GONE); // Hide RecyclerView for guests
            guestViewOverlay.setVisibility(View.VISIBLE); // Show guest overlay
        } else if (isConnected && presenter != null && rv != null) {
            presenter.getAllRecentMeals(userId, 10).observe(getViewLifecycleOwner(), recentMeals -> {
                if (recentMeals != null && !recentMeals.isEmpty()) {
                    adapter.updateMeals(recentMeals);
                    rv.setVisibility(View.VISIBLE);
                } else {
                    rv.setVisibility(View.GONE);
                }
            });
            if (guestViewOverlay != null) {
                guestViewOverlay.setVisibility(View.GONE); // Ensure overlay is hidden for non-guests
            }
        } else if (rv != null) {
            rv.setVisibility(View.GONE);
            if (guestViewOverlay != null) {
                guestViewOverlay.setVisibility(View.GONE); // Hide overlay when offline
            }
        }
        // Handle user greeting
        if (greetingText != null) {
            String name = sharedPreferences.getString("name", "User");
            if (isGuest) {
                greetingText.setText("Hello Guest");
            } else {
                greetingText.setText("Hello " + name);
            }
        }
    }

    private void setupNetworkCallback() {
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                requireActivity().runOnUiThread(() -> {
                    if (!isConnected) {
                        isConnected = true;
                        reloadFragment();
                    }
                });
            }

            @Override
            public void onLost(@NonNull Network network) {
                requireActivity().runOnUiThread(() -> {
                    if (!isConnected) {
                        isConnected = false;
                        reloadFragment();
                    }
                });
            }
        };

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
        if (connectivityManager != null) {
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
        }
    }

    private void reloadFragment() {
        if (getParentFragmentManager() != null) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.detach(this).attach(this).commit();
        }
    }

    @Override
    public void showRandomMeal(Meal meal) {
        if (meal == null) return;
        currentMeal = meal;
        saveMealOfTheDay(meal);
        if (mealName != null && mealImage != null && randomMealCard != null) {
            mealName.setText(meal.getStrMeal());
            Glide.with(this)
                    .load(meal.getStrMealThumb())
                    .placeholder(R.drawable.meal)
                    .error(R.drawable.meal)
                    .into(mealImage);
            randomMealCard.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError(String errorMessage) {
        if (isConnected && getContext() != null) {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMealClick(Meal meal) {
        if (meal == null || fragmentManager == null || fragmentContainerView == null) return;
        mealFrag = new MealFragment();
        Bundle args = new Bundle();
        args.putString("id", meal.getIdMeal());
        args.putString("imageUrl", meal.getStrMealThumb());
        args.putString("name", meal.getStrMeal());
        args.putString("category", meal.getStrCategory());
        args.putString("area", meal.getStrArea());
        args.putString("instructions", meal.getStrInstructions());
        args.putString("youtube", meal.getStrYoutube());
        args.putBoolean("fromFavorites", false);
        args.putStringArrayList("ingredients", new ArrayList<>(meal.getIngredients()));
        args.putStringArrayList("measures", new ArrayList<>(meal.getMeasures()));

        mealFrag.setArguments(args);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFragmentContainer, mealFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentContainerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (fragmentContainerView != null) {
            fragmentContainerView.setVisibility(View.GONE);
        }
        // Unregister network callback to prevent leaks
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
            networkCallback = null;
        }
    }

    public boolean handleBackPress() {
        FragmentContainerView fragmentContainer = rootView.findViewById(R.id.homeFragmentContainer);
        LinearLayout mainContent = rootView.findViewById(R.id.mainContentLayout);
        if (fragmentContainer != null && fragmentContainer.getVisibility() == View.VISIBLE) {
            fragmentContainer.setVisibility(View.GONE);
            mainContent.setVisibility(View.VISIBLE);
            getChildFragmentManager().popBackStack();
            return true; // Back press handled
        }
        return false; // Back press not handled, terminate app
    }
}