package com.example.tabkhtech.ui.calendar.presenter;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.SchedMeal;

import java.util.List;

public interface CalendarPresenter {
    LiveData<List<SchedMeal>> getAllSchedMeals(String date, String userId);
    void deleteScheduledMeal(SchedMeal meal);
}
