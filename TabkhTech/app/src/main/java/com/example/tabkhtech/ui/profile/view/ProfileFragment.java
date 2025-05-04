package com.example.tabkhtech.ui.profile.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.tabkhtech.R;
import com.example.tabkhtech.SignInActivity;
import com.example.tabkhtech.authentication.signup.view.SignUpActivity;
import com.example.tabkhtech.ui.profile.presenter.ProfilePresenter;
import com.example.tabkhtech.ui.profile.presenter.ProfilePresenterImpl;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment implements ProfileView {

    private ProfilePresenter presenter;
    private ImageView userImage;
    private TextView userName;
    private TextView userEmail;
    private TextView mealsPlanned;
    private TextView recipesSaved;
    private MaterialButton updatePasswordButton;
    private MaterialButton signOutButton;
    private MaterialButton signUpButton;
    private SharedPreferences sharedPreferences;
    private ConnectivityManager connectivityManager;
    private boolean isConnected;
    private ConnectivityManager.NetworkCallback networkCallback;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences("user_data", getContext().MODE_PRIVATE);
        presenter = new ProfilePresenterImpl(this, requireContext());
        // Initialize connectivity manager
        connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        isConnected = isInternetConnected(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout based on internet status

        if (isConnected) {
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        } else {
            rootView = inflater.inflate(R.layout.disconnected, container, false);
        }
        // Initialize UI elements
        if (isConnected){
            userImage = rootView.findViewById(R.id.userImage);
            userName = rootView.findViewById(R.id.userName);
            userEmail = rootView.findViewById(R.id.userEmail);
            mealsPlanned = rootView.findViewById(R.id.mealsPlanned);
            recipesSaved = rootView.findViewById(R.id.recipesSaved);
            updatePasswordButton = rootView.findViewById(R.id.updatePasswordButton);
            signOutButton = rootView.findViewById(R.id.signOutButton);
            signUpButton = rootView.findViewById(R.id.signUpButton); // New button

            // Set click listeners
            updatePasswordButton.setOnClickListener(v -> presenter.onUpdatePasswordClicked());
            signOutButton.setOnClickListener(v -> presenter.onSignOutClicked());
            if (signUpButton != null) {
                signUpButton.setOnClickListener(v -> {
                    Intent intent = new Intent(requireContext(), SignUpActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                });
            }
            boolean isGuest = sharedPreferences.getBoolean("isGuest", false);
            if (isGuest) {
                updatePasswordButton.setEnabled(false);
                updatePasswordButton.setAlpha(0.5f);
                if (signUpButton != null) {
                    signUpButton.setVisibility(View.VISIBLE);
                }
            } else {
                updatePasswordButton.setEnabled(true);
                updatePasswordButton.setAlpha(1.0f);
                if (signUpButton != null) {
                    signUpButton.setVisibility(View.GONE);
                }
            }
                presenter.loadUserData();
        }
        setupNetworkCallback();
        return rootView;
    }

    @Override
    public void showUserData(String name, String email, String profileImageUrl, String mealsPlanned, String recipesSaved) {
        userName.setText(name);
        userEmail.setText(email);
        this.mealsPlanned.setText(mealsPlanned);
        this.recipesSaved.setText(recipesSaved);
        // Load profile image if URL is available
        if (!profileImageUrl.isEmpty()) {
            Glide.with(this)
                    .load(profileImageUrl)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .circleCrop()
                    .into(userImage);
        } else {
            userImage.setImageResource(R.drawable.ic_profile_placeholder);
        }
    }

    @Override
    public void showUpdatePasswordDialog() {
        // Create a dialog with input field for new password
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Update Password");

        final EditText input = new EditText(requireContext());
        input.setHint("Enter new password");
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newPassword = input.getText().toString();
            if (newPassword.length() < 6) {
                showError("Password must be at least 6 characters");
                return;
            }

            // Update password in Firebase
            FirebaseAuth.getInstance().getCurrentUser()
                    .updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            showError("Failed to update password: " + task.getException().getMessage());
                        }
                    });
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void showSignOutConfirmation() {
        // Show confirmation dialog for sign-out
        new AlertDialog.Builder(requireContext())
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Sign out from Firebase and clear SharedPreferences
                    FirebaseAuth.getInstance().signOut();
                    sharedPreferences.edit()
                            .putBoolean("isSignedIn", false)
                            .putBoolean("isGuest", false)
                            .apply();
                    navigateToLoginScreen();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void navigateToLoginScreen() {
        Intent intent = new Intent(requireContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
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
                    if (isConnected) {
                        isConnected = false;
                        reloadFragment();
                    }
                });
            }
        };

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    private void reloadFragment() {
        if (isAdded() && getActivity() != null) {
            getParentFragmentManager().beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }
}