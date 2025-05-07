package com.example.tabkhtech.ui.calendar.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tabkhtech.R;
import com.example.tabkhtech.model.local.MealLocalDataSourceImpl;
import com.example.tabkhtech.model.pojos.SchedMeal;
import com.example.tabkhtech.model.remote.retrofit.MealRemoteDataSourceImpl;
import com.example.tabkhtech.model.repository.RepositoryImpl;
import com.example.tabkhtech.ui.calendar.presenter.CalendarPresenter;
import com.example.tabkhtech.ui.calendar.presenter.CalendarPresenterImpl;
import com.example.tabkhtech.ui.detailed_meal.view.MealFragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class CalendarViewImpl extends Fragment implements ICalendarView, OnMealClickListener, OnMealDeleteListener {

    private static final String TAG = "CalendarViewImpl";
    private RecyclerView recyclerView;
    private CalendarAdapter calendarAdapter;
    private CalendarPresenter calendarPresenter;
    private FrameLayout schedFragmentContainer;
    private CalendarView calendarView;
    private TextView tvEmptyState, tvMealsLabel;
    private ImageView ivNoScheduledMeals;
    private View rootView;
    private String currentSelectedDate;
    private LinearLayoutManager layoutManager;
    private SharedPreferences sharedPreferences;
    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("user_data", getContext().MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "guest");

        calendarPresenter = new CalendarPresenterImpl(
                this,
                RepositoryImpl.getInstance(
                        MealLocalDataSourceImpl.getInstance(requireContext()),
                        MealRemoteDataSourceImpl.getInstance()
                ));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.scheduledMealsRV);
        schedFragmentContainer = view.findViewById(R.id.schedFragmentContainer);
        calendarView = view.findViewById(R.id.calendarView);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        tvMealsLabel = view.findViewById(R.id.tvMealsLabel);
        ivNoScheduledMeals = view.findViewById(R.id.ivNoScheduledMeals);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String selectedDate = dateFormat.format(calendar.getTime());
            currentSelectedDate = selectedDate;

            tvMealsLabel.setText("Meals for " + selectedDate);

            calendarPresenter.getAllSchedMeals(selectedDate, userId).observe(getViewLifecycleOwner(), meals -> {

                calendarAdapter = new CalendarAdapter(meals, this, this);
                recyclerView.setAdapter(calendarAdapter);
                updateVisibility(meals);
            });
        });

        // Initialize with today's date
        Calendar today = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String todayDate = dateFormat.format(today.getTime());
        currentSelectedDate = todayDate;
        tvMealsLabel.setText("Meals for " + todayDate);
        calendarPresenter.getAllSchedMeals(todayDate, userId).observe(getViewLifecycleOwner(), meals -> {
            calendarAdapter = new CalendarAdapter(meals, this, this);
            recyclerView.setAdapter(calendarAdapter);
            updateVisibility(meals);
            Log.d(TAG, meals.toString());
        });
    }

    private void updateVisibility(List<SchedMeal> meals) {
        if (meals != null && !meals.isEmpty()) {
            recyclerView.setVisibility(VISIBLE);
            ivNoScheduledMeals.setVisibility(GONE);
            tvEmptyState.setVisibility(GONE);
        } else {
            recyclerView.setVisibility(GONE);
            ivNoScheduledMeals.setVisibility(VISIBLE);
            tvEmptyState.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onMealClick(SchedMeal meal) {
        MealFragment fragMeal = new MealFragment();
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
        Log.d("+++++++++++++++", args.toString());
        fragMeal.setArguments(args);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.schedFragmentContainer, fragMeal)
                .addToBackStack("meals_detail")
                .commit();
        schedFragmentContainer.setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.schedMainContentLayout).setVisibility(View.GONE);
    }

    @Override
    public void onMealDelete(SchedMeal meal) {
        calendarPresenter.deleteScheduledMeal(meal);
        calendarPresenter.getAllSchedMeals(currentSelectedDate, userId).observe(getViewLifecycleOwner(), meals -> {
            calendarAdapter.updateList(meals);
            updateVisibility(meals);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

//    public void handleBackPress() {
//        FragmentContainerView schedFragmentContainer = rootView.findViewById(R.id.schedFragmentContainer);
//        LinearLayout schedMainContentLayout = rootView.findViewById(R.id.schedMainContentLayout);
//        if (schedFragmentContainer.getVisibility() == View.VISIBLE) {
//            schedFragmentContainer.setVisibility(View.GONE);
//            schedMainContentLayout.setVisibility(View.VISIBLE);
//            getChildFragmentManager().popBackStack();
//        } else {
//            requireActivity().onBackPressed();
//        }
//    }
public boolean handleBackPress() {
    FragmentContainerView schedFragmentContainer = rootView.findViewById(R.id.schedFragmentContainer);
    LinearLayout schedMainContentLayout = rootView.findViewById(R.id.schedMainContentLayout);
    if (schedFragmentContainer.getVisibility() == View.VISIBLE) {
        schedFragmentContainer.setVisibility(View.GONE);
        schedMainContentLayout.setVisibility(View.VISIBLE);
        getChildFragmentManager().popBackStack();
        return true;
    }
    return false;
}
}