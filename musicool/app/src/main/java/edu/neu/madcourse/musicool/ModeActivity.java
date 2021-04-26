package edu.neu.madcourse.musicool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ModeActivity extends AppCompatActivity {

    private Button back;
    private String duration = "10 Seconds";
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        radioGroup = findViewById(R.id.radio_group_mode);

        // Uncheck or reset the radio buttons initially
        radioGroup.clearCheck();

        // Add the Listener to the RadioGroup
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId)
                    {

                        // Get the selected Radio Button
                        RadioButton radioButton
                                = (RadioButton)group
                                .findViewById(checkedId);
                    }
                });


        back = findViewById(R.id.back_button_mode);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(ModeActivity.this,
                    "No duration has been selected",
                    Toast.LENGTH_SHORT)
                    .show();
        }
        else {

            RadioButton radioButton
                    = (RadioButton)radioGroup
                    .findViewById(selectedId);

            // Now display the value of selected item
            // by the Toast message
            Toast.makeText(ModeActivity.this,
                    radioButton.getText()+ " Selected",
                    Toast.LENGTH_SHORT)
                    .show();
            duration = radioButton.getText().toString();
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("duration", duration);
        setResult(Activity.RESULT_OK,returnIntent);
        super.onBackPressed();
        finish();
    }
}