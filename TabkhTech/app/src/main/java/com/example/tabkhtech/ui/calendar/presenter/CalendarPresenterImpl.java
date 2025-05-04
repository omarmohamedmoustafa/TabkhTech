package com.example.tabkhtech.ui.calendar.presenter;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.SchedMeal;
import com.example.tabkhtech.model.repository.Repository;
import com.example.tabkhtech.ui.calendar.view.ICalendarView;

import java.util.List;

public class CalendarPresenterImpl implements CalendarPresenter{
    ICalendarView calendarView;
    Repository repository;
    public CalendarPresenterImpl(ICalendarView calendarView, Repository repository) {
        this.calendarView = calendarView;
        this.repository = repository;
    }


    @Override
    public LiveData<List<SchedMeal>> getAllSchedMeals(String date, String userId) {
        return repository.getAllSchedMeals(date, userId);
    }

    @Override
    public void deleteScheduledMeal(SchedMeal meal) {
        repository.deleteSchedMeal(meal);
    }
}
