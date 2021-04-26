package edu.neu.madcourse.musicool;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReviewResultActivity extends AppCompatActivity {

    private List<Question> questionList;
    private List<Boolean> resultList;
    private HashMap<String, Button> buttonMap;
    private int questionIndex;
    private ImageView imageView;
    private Button nextButton;
    private TextView txtTime,txtResult, txtRightAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_result);

        txtTime = findViewById(R.id.txt_time_review);
        txtResult = findViewById(R.id.txt_result_review);
        txtRightAnswer = findViewById(R.id.txt_right_answer_review);
        txtTime.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(Result.duration),
                TimeUnit.MILLISECONDS.toSeconds(Result.duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Result.duration))
        ));

        txtRightAnswer.setText(new StringBuilder("").append(Result.correctCount).append("/").append(Result.questionList.size()));

        int score = Result.correctCount * (int)(((double)Result.correctCount/(double)Result.questionList.size())*100);
        txtResult.setText("Score: " + String.valueOf(score));

        imageView = findViewById(R.id.questionImageReview);
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });

        //initial questions
        questionIndex = 0;
        questionList = Result.questionList;
        resultList = Result.resultList;
        buttonMap = new HashMap<>();
        //add all buttons
        for(int i = 0; i< 21; i++){
            int id = getResources().getIdentifier("button"+(i+1)+"r", "id", getPackageName());
            Button cur = findViewById(id);
            buttonMap.put((String) cur.getText(),cur);
        }
        nextQuestion();
    }

    private void nextQuestion(){
        if(questionIndex != 0){

            buttonMap.get(questionList.get(questionIndex - 1).getAnswer()).setBackgroundColor(Color.parseColor("#444444"));
        }
        if(questionIndex >= questionList.size()){
            onBackPressed();
            finish();
            return;
        }
        int id = questionList.get(questionIndex).getImageId();
        imageView.setImageResource(id);
        buttonMap.get(questionList.get(questionIndex).getAnswer()).setBackgroundColor(Color.parseColor("#03a9f4"));
        questionIndex++;
    }


}