package com.google.foods.activity;

import android.app.Activity;
import android.os.Bundle;

import com.google.foods.R;


/**
 * Created by Android on 7/27/2017.
 */

public class FoodActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
    }
}
