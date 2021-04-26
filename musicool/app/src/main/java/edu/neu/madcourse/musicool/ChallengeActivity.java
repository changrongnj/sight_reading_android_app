package edu.neu.madcourse.musicool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ChallengeActivity extends AppCompatActivity {

    private List<Question> questionList;
    private List<Boolean> resultList;
    private List<Button> buttons, keyboard_buttons;
    private int questionIndex;
    private ImageView imageView;
    private int correctCount;
    private int wrongCount;
    private long duration;
    private static final int GENERATE_SIZE = 5;
    private CountDownTimer countDownTimer;
    private TextView accuracyTextView;

    private TextView countdown_tv;
    private Switch sw_keyboard;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userID;
    private Long scoreCount = 1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        imageView = findViewById(R.id.questionImage);

        //initial questions
        questionIndex = 0;
        correctCount = 0;
        questionList = new ArrayList<>();
        resultList = new ArrayList<>();
        questionList = addQuestions();
        nextQuestion();

        //get user info
        getUserInfo();

        buttons = new ArrayList<>();
        //add all buttons
        for(int i = 0; i< 21; i++){
            int id = getResources().getIdentifier("button"+(i+1), "id", getPackageName());
            buttons.add(findViewById(id));
            //set onclick listener
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = findViewById(view.getId());
                    String selection = button.getText().toString();
                    checkAnswer(selection);
                    questionIndex++;
                    nextQuestion();
                }
            });
        }


        keyboard_buttons = new ArrayList<>();
        //add all buttons
        for(int i = 0; i< 12; i++){
            int id = getResources().getIdentifier("btn"+(i+1), "id", getPackageName());
            keyboard_buttons.add(findViewById(id));
            //set onclick listener
            keyboard_buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selection = view.getResources().getResourceName(view.getId());
                    int selection_index = Integer.parseInt(selection.substring(33)) - 1;
                    List<String> selection_ans = KeyboardNotesBank.getKbNotes().get(selection_index);
                    keyboard_checkAnswer(selection_ans);
                    questionIndex++;
                    nextQuestion();
                }
            });
        }



        accuracyTextView =findViewById(R.id.currentAccuracy);


        countdown_tv = findViewById(R.id.countdown_tv);
        // initalize timer duration --> change to 1 min
        //duration = TimeUnit.MINUTES.toMillis(1);
        Intent intent = getIntent();
        String durationStr = "10 Seconds";
        Bundle extras = intent.getExtras();
        if(extras != null)
            durationStr = extras.getString("duration");

        if(durationStr.equals("10 Seconds")){
            duration = 10000;
        }else if(durationStr.equals("1 Minute")){
            duration = 1000*60;
        }else{
            duration = 1000*60*3;
        }

        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                // when tick convert millisecond to minute and second
                String sDuration = String.format(Locale.ENGLISH,"%02d:%02d"
                                    ,TimeUnit.MILLISECONDS.toMinutes(l)
                                    ,TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))
                                    );
                // set converted string on text view
                countdown_tv.setText((sDuration));
            }

            @Override
            public void onFinish() {
                // when finish hide textview
                //countdown_tv.setVisibility(View.GONE);
                showResult();
                uploadScore();
                finish();
            }
        }.start();


        sw_keyboard = (Switch) findViewById(R.id.sw_keyboard);
        sw_keyboard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // toggle is enabled
                    findViewById(R.id.ans_keyboardwhite).setVisibility(View.VISIBLE);
                    findViewById(R.id.ans_keyboardblack).setVisibility(View.VISIBLE);
                    findViewById(R.id.ans_button).setVisibility(View.GONE);
                } else {
                    // toggle is disabled
                    findViewById(R.id.ans_keyboardwhite).setVisibility(View.GONE);
                    findViewById(R.id.ans_keyboardblack).setVisibility(View.GONE);
                    findViewById(R.id.ans_button).setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void keyboard_checkAnswer(List<String> selection_ans) {
        if (selection_ans.contains(questionList.get(questionIndex).getAnswer())) {
            resultList.set(questionIndex, true);
            correctCount++;
        } else {
            resultList.set(questionIndex, false);
            wrongCount++;
        }
        accuracyTextView.setText(correctCount + "/" + wrongCount);
    }


    private void checkAnswer(String answer){
        if(questionList.get(questionIndex).getAnswer().equals(answer)){
            resultList.set(questionIndex, true);
            correctCount++;
        }else{
            resultList.set(questionIndex, false);
            wrongCount++;
        }
        accuracyTextView.setText(correctCount + "/" + wrongCount);
    }

    private void nextQuestion(){
        if(questionIndex >= questionList.size()){
            addQuestions();
        }
        int id = questionList.get(questionIndex).getImageId();
        imageView.setImageResource(id);
    }

    private void showResult() {
        Intent resultIntent = new Intent(ChallengeActivity.this, ResultActivity.class);

        Result.duration = (int)duration;
        Result.correctCount = correctCount;
        Result.questionList = new ArrayList<>(questionList.subList(0, questionIndex));
        Result.wrongCount = wrongCount;
        Result.resultList = new ArrayList<>(resultList);
        startActivity(resultIntent);
        finish();
    }


    private List<Question> addQuestions(){
        Random rand = new Random();
        for(int i = 0; i < ChallengeActivity.GENERATE_SIZE; i++) resultList.add(false);
        for(int i = 0; i< ChallengeActivity.GENERATE_SIZE; i++){
            int bankIndex = rand.nextInt(QuestionBank.getQuestionBank().size());
            questionList.add(QuestionBank.getQuestionBank().get(bankIndex));
        }
        return questionList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
    }
    private void getUserInfo(){
        if(!Helper.loggedIn) return;
        // get user information to display
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        if(firebaseUser != null) {
            userID = firebaseUser.getUid();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                    // ...
                    if(userID != null){
                        Long count = dataSnapshot.child(userID).child("scoreCount").getValue(Long.class);
                        if(count != null) scoreCount = count;
                    }

                }

                @Override
                public void onCancelled(@NotNull DatabaseError databaseError) {
                    // ...
                }
            });
        }
    }
    private void uploadScore(){
        if(Helper.loggedIn && userID != null){
            int score = Result.correctCount * (int)(((double)Result.correctCount/(double)Result.questionList.size())*100);
            databaseReference.child(userID).child("scores").child(scoreCount.toString()).setValue(score);
            databaseReference.child(userID).child("scoreCount").setValue(scoreCount+1);
        }
    }

}