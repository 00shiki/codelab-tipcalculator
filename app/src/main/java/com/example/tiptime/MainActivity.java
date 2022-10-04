package com.example.tiptime;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptime.databinding.ActivityMainBinding;

import java.text.NumberFormat;

/**
* Activity that displays a tip calculator.
*/
public class MainActivity extends AppCompatActivity {

    // Binding object instance with access to the views in the activity_main.xml layout
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout XML file and return a binding object instance
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Set the content view of the Activity to be the root view of the layout
        setContentView(binding.getRoot());

        // Setup a click listener on the calculate button to calculate the tip
        binding.calculateButton.setOnClickListener(view -> calculateTip());

        // Set up a key listener on the EditText field to listen for "enter" button presses
        binding.costOfServiceEditText.setOnKeyListener((view, i, keyEvent) -> handleKeyEvent(view, i));
    }

    /**
     * Calculates the tip based on the user input.
     */
    private void calculateTip() {
        // Get the decimal value from the cost of service EditText field
        String stringInTextField = binding.costOfServiceEditText.getText().toString();
        double cost = parseStringToDouble(stringInTextField);

        // If the cost is null or 0, then display 0 tip and exit this function early.
        if (cost == 0.0) {
            displayTip(0.0);
            return;
        }

        // Get the tip percentage based on which radio button is selected
        double tipPercentage;
        if (binding.tipOptions.getCheckedRadioButtonId() == R.id.option_twenty_percent) {
            tipPercentage = 0.20;
        } else if (binding.tipOptions.getCheckedRadioButtonId() == R.id.option_eighteen_percent) {
            tipPercentage = 0.18;
        } else {
            tipPercentage = 0.15;
        }

        // Calculate the tip
        double tip = tipPercentage * cost;

        // If the switch for rounding up the tip toggled on (isChecked is true), then round up the
        // tip. Otherwise do not change the tip value.
        boolean roundUp = binding.roundUpSwitch.isChecked();
        if (roundUp) {
            // Take the ceiling of the current tip, which rounds up to the next integer, and store
            // the new value in the tip variable.
            tip = Math.ceil(tip);
        }

        // Display the formatted tip value onscreen
        displayTip(tip);
    }

    /**
     * Key listener for hiding the keyboard when the "Enter" button is tapped.
     */
    private void displayTip(double tip) {
        String formattedTip = NumberFormat.getCurrencyInstance().format(tip);
        binding.tipResult.setText(getString(R.string.tip_amount, formattedTip));
    }

    /**
     * Key listener for hiding the keyboard when the "Enter" button is tapped.
     */
    private boolean handleKeyEvent(View view, int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return true;
        }
        return false;
    }

    private static double parseStringToDouble(String value) {
        return value == null || value.isEmpty() ? 0.0 : Double.parseDouble(value);
    }
}
