package com.example.karakoram.resource;

public class DayRating {
    private MealRating breakfast;
    private MealRating lunch;
    private MealRating dinner;

    public MealRating getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(MealRating breakfast) {
        this.breakfast = breakfast;
    }

    public MealRating getLunch() {
        return lunch;
    }

    public void setLunch(MealRating lunch) {
        this.lunch = lunch;
    }

    public MealRating getDinner() {
        return dinner;
    }

    public void setDinner(MealRating dinner) {
        this.dinner = dinner;
    }
}
