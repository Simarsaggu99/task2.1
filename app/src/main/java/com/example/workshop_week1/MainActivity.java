package com.example.workshop_week1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner conversionTypeSpinner, sourceUnitSpinner, destinationUnitSpinner;
    private EditText valueEditText;
    private Button convertButton;
    private TextView resultTextView;

    private String[] conversionTypes = {"Length", "Weight", "Temperature"};
    private String[][] units = {
            {"Inch", "Foot", "Yard", "Mile"},
            {"Pound", "Ounce", "Ton"},
            {"Celsius", "Fahrenheit", "Kelvin"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conversionTypeSpinner = findViewById(R.id.conversionTypeSpinner);
        sourceUnitSpinner = findViewById(R.id.sourceUnitSpinner);
        destinationUnitSpinner = findViewById(R.id.destinationUnitSpinner);
        valueEditText = findViewById(R.id.valueEditText);
        convertButton = findViewById(R.id.convertButton);
        resultTextView = findViewById(R.id.resultTextView);
        //For setting initial value
        String resultText = getString(R.string.result_placeholder, String.valueOf(0));
        resultTextView.setText(resultText);

        ArrayAdapter<String> conversionTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, conversionTypes);
        conversionTypeSpinner.setAdapter(conversionTypeAdapter);

        conversionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnitsSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convert();
            }
        });
    }

    private void updateUnitsSpinner(int position) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units[position]);
        sourceUnitSpinner.setAdapter(adapter);

        if (position == 0) { // Length
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"cm", "km"});
        }
       else if (position == 1) { // Length
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"g", "kg"});
        }

        destinationUnitSpinner.setAdapter(adapter);
    }

    private void convert() {
        double value = Double.parseDouble(valueEditText.getText().toString());
        String sourceUnit = (String) sourceUnitSpinner.getSelectedItem();
        String destinationUnit = (String) destinationUnitSpinner.getSelectedItem();

        double result = 0;

        // Conversion logic
        if (conversionTypeSpinner.getSelectedItemPosition() == 0) { // Length
            result = lengthConversion(value, sourceUnit, destinationUnit);
        } else if (conversionTypeSpinner.getSelectedItemPosition() == 1) { // Weight
            result = weightConversion(value, sourceUnit, destinationUnit);
        } else if (conversionTypeSpinner.getSelectedItemPosition() == 2) { // Temperature
            result = temperatureConversion(value, sourceUnit, destinationUnit);
        }
        String resultText = getString(R.string.result_placeholder, String.valueOf(result));
        resultTextView.setText(resultText);

//        resultTextView.setText(String.valueOf(result));
    }

    private double lengthConversion(double value, String sourceUnit, String destinationUnit) {
        // Conversion factors
        final double INCH_TO_CM = 2.54;
        final double FOOT_TO_CM = 30.48;
        final double YARD_TO_CM = 91.44;
        final double MILE_TO_KM = 1.60934;

        // Convert to centimeters if source unit is not cm
        if (sourceUnit.equals("Inch")) {
            value *= INCH_TO_CM;
        } else if (sourceUnit.equals("Foot")) {
            value *= FOOT_TO_CM;
        } else if (sourceUnit.equals("Yard")) {
            value *= YARD_TO_CM;
        } else if (sourceUnit.equals("Mile")) {
            value *= MILE_TO_KM * 1000; // Convert miles to meters first
        }

        // Convert to destination unit
        if (destinationUnit.equals("cm")) {
            return value;
        } else if (destinationUnit.equals("km")) {
            return value / 100000; // Convert centimeters to kilometers
        }

        return 0; // Default return
    }

    private double weightConversion(double value, String sourceUnit, String destinationUnit) {
        // Conversion factors
        final double POUND_TO_KG = 0.453592;
        final double OUNCE_TO_G = 28.3495;
        final double TON_TO_KG = 907.185;

        // Convert to grams if source unit is not g
        if (sourceUnit.equals("Pound")) {
            value *= POUND_TO_KG * 1000; // Convert pounds to grams
        } else if (sourceUnit.equals("Ounce")) {
            value *= OUNCE_TO_G;
        } else if (sourceUnit.equals("Ton")) {
            value *= TON_TO_KG * 1000; // Convert tons to grams
        }

        // Convert to destination unit
        if (destinationUnit.equals("g")) {
            return value;
        } else if (destinationUnit.equals("kg")) {
            return value / 1000; // Convert grams to kilograms
        }

        return 0; // Default return
    }
    private double temperatureConversion(double value, String sourceUnit, String destinationUnit) {
        // Convert to Celsius for uniformity
        if (sourceUnit.equals("Fahrenheit")) {
            value = (value - 32) / 1.8;
        } else if (sourceUnit.equals("Kelvin")) {
            value -= 273.15;
        }

        // Convert to destination unit
        if (destinationUnit.equals("Celsius")) {
            return value;
        } else if (destinationUnit.equals("Fahrenheit")) {
            return (value * 1.8) + 32;
        } else if (destinationUnit.equals("Kelvin")) {
            return value + 273.15;
        }

        return 0; // Default return
    }

    // Implement weightConversion and temperatureConversion methods similarly
}
