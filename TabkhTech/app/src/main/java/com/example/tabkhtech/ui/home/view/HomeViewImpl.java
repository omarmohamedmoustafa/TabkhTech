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
import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.ui.home.presenter.HomePresenter;
import com.example.tabkhtech.ui.home.presenter.HomePresenterImpl;
import com.example.tabkhtech.model.local.MealLocalDataSourceImpl;
import com.example.tabkhtech.model.remote.MealRemoteDataSourceImpl;
import com.example.tabkhtech.model.repository.RepositoryImpl;
import com.example.tabkhtech.ui.single_meal.view.mealFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeViewImpl extends Fragment implements HomeView, OnMealClickListener {

    private ImageView mealImage;
    private TextView mealName;
    private TextView greetingText;
    private RecyclerView rv;
    private HomeAdapter adapter;
    private HomePresenter presenter;
    private Meal currentMeal;
    private mealFragment mealFrag;
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

        // Inflate layout based on internet status
        if (isConnected) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
        } else {
            rootView = inflater.inflate(R.layout.disconnected, container, false);
        }

        // Initialize presenter only if connected
        if (isConnected) {
            presenter = new HomePresenterImpl(
                    RepositoryImpl.getInstance(MealLocalDataSourceImpl.getInstance(getContext()), MealRemoteDataSourceImpl.getInstance()),
                    this
            );
            sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
            userId = sharedPreferences.getString("userId", "guest");
            presenter.getRandomMeal();
        }

        // Set up network callback for real-time monitoring
        setupNetworkCallback();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Only initialize UI elements if connected
        if (isConnected) {
            mealImage = view.findViewById(R.id.itemImg);
            mealName = view.findViewById(R.id.itemName);
            greetingText = view.findViewById(R.id.greeting_text);
            rv = view.findViewById(R.id.recentMealRV);
            fragmentContainerView = view.findViewById(R.id.homeFragmentContainer);
            randomMealCard = view.findViewById(R.id.randomMealCard);
            fragmentManager = getParentFragmentManager();

            rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            adapter = new HomeAdapter(new ArrayList<>(), this);
            rv.setAdapter(adapter);

            presenter.getAllRecentMeals(userId, 10).observe(getViewLifecycleOwner(), recentMeals -> {
                if (recentMeals != null && !recentMeals.isEmpty()) {
                    adapter.updateMeals(recentMeals);
                    rv.setVisibility(View.VISIBLE);
                } else {
                    rv.setVisibility(View.GONE);
                }
            });

            randomMealCard.setOnClickListener(v -> {
                if (currentMeal != null) {
                    onMealClick(currentMeal);
                }
            });

            boolean isGuest = sharedPreferences.getBoolean("isGuest", false);
            boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);

            if (isGuest) {
                greetingText.setText("Hello Guest");
            } else if (isSignedIn && FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String firebaseUserId = currentUser.getUid();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseUserId);

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String name = dataSnapshot.child("name").getValue(String.class);
                            if (name == null || name.isEmpty()) {
                                name = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "User";
                            }
                            greetingText.setText("Hello " + name);
                        } else {
                            String name = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "User";
                            greetingText.setText("Hello " + name);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        greetingText.setText("Hello User");
                        Toast.makeText(getContext(), "Failed to load user name", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                greetingText.setText("Hello User");
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
                if (isConnected) {
                    isConnected = false;
                    reloadFragment();
                }
            }
        };

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    private void reloadFragment() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }

    @Override
    public void showRandomMeal(Meal meal) {
        if (isConnected) {
            currentMeal = meal;
            mealName.setText(meal.getStrMeal());
            Glide.with(this)
                    .load(meal.getStrMealThumb())
                    .into(mealImage);
        }
    }

    @Override
    public void showError(String errorMessage) {
        if (isConnected) {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMealClick(Meal meal) {
        if (isConnected) {
            mealFrag = new mealFragment();
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
        }
    }
}