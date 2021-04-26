package edu.neu.madcourse.musicool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView recyclerViewResult;
    private TextView txtTime,txtResult, txtRightAnswer;
    private Button backButton, tryAgainButton;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // xinglu: tmp comment for debug; not sure below lines doing
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
       // Objects.requireNonNull(getSupportActionBar()).setTitle("Result");
        txtTime = findViewById(R.id.txt_time);
        txtResult = findViewById(R.id.txt_result);
        txtRightAnswer = findViewById(R.id.txt_right_answer);

        recyclerViewResult = findViewById(R.id.recycler_result);
        recyclerViewResult.setHasFixedSize(true);
        recyclerViewResult.setLayoutManager(new GridLayoutManager(this, 3));

        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewResult();
            }
        });

        tryAgainButton = findViewById(R.id.btn_new_quiz);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doQuizAgain();
            }
        });

        ResultGridAdapter resultGridAdapter = new ResultGridAdapter(Result.questionList, this);
        recyclerViewResult.setAdapter(resultGridAdapter);

        txtTime.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(Result.duration),
                TimeUnit.MILLISECONDS.toSeconds(Result.duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Result.duration))
                ));

        txtRightAnswer.setText(new StringBuilder("").append(Result.correctCount).append("/").append(Result.questionList.size()));

        int score = Result.correctCount * (int)(((double)Result.correctCount/(double)Result.questionList.size())*100);
        txtResult.setText("Score: " + String.valueOf(score));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //doQuizAgain();
    }

    private void doQuizAgain(){
        new MaterialAlertDialogBuilder(ResultActivity.this)
                .setTitle("Do quiz again?")
                .setMessage("Do you want to start a new quiz?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent returnIntent = new Intent(ResultActivity.this, ChallengeActivity.class);
                        //returnIntent.putExtra("action", "doItAgain");
                        //setResult(Activity.RESULT_OK, returnIntent);
                        startActivity(returnIntent);
                        finish();
                    }
                }).show();
    }

    private void reviewResult(){
        Intent reviewResult = new Intent(ResultActivity.this, ReviewResultActivity.class);
        startActivity(reviewResult);
    }
}