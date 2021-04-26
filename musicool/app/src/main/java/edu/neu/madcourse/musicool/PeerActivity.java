package edu.neu.madcourse.musicool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class PeerActivity extends AppCompatActivity {

    TextView u1, u2, u3, u4, u5, u6, u7, u8, u9, u10, u11, u12, u13, u14, u15, u16, u17, u18, u19, u20;
    TextView s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20;
    Button nearby, rankingBoard;
    TextView name, slogan;
    User user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18, user19, user20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peer);

        nearby =  findViewById(R.id.nearby);
        rankingBoard =  findViewById(R.id.ranking);
        name = findViewById(R.id.name);
        slogan = findViewById(R.id.slogan);

        u1 =  findViewById(R.id.u1);
        u2 =  findViewById(R.id.u2);
        u3 =  findViewById(R.id.u3);
        u4 =  findViewById(R.id.u4);
        u5 =  findViewById(R.id.u5);
        u6 =  findViewById(R.id.u6);
        u7 =  findViewById(R.id.u7);
        u8 =  findViewById(R.id.u8);
        u9 =  findViewById(R.id.u9);
        u10 =  findViewById(R.id.u10);
        u11 =  findViewById(R.id.u11);
        u12 =  findViewById(R.id.u12);
        u13 =  findViewById(R.id.u13);
        u14 =  findViewById(R.id.u14);
        u15 =  findViewById(R.id.u15);
        u16 =  findViewById(R.id.u16);
        u17 =  findViewById(R.id.u17);
        u18 =  findViewById(R.id.u18);
        u19 =  findViewById(R.id.u19);
        u20 =  findViewById(R.id.u20);

        s1 =  findViewById(R.id.s1);
        s2 =  findViewById(R.id.s2);
        s3 =  findViewById(R.id.s3);
        s4 =  findViewById(R.id.s4);
        s5 =  findViewById(R.id.s5);
        s6 =  findViewById(R.id.s6);
        s7 =  findViewById(R.id.s7);
        s8 =  findViewById(R.id.s8);
        s9 =  findViewById(R.id.s9);
        s10 =  findViewById(R.id.s10);
        s11 =  findViewById(R.id.s11);
        s12 =  findViewById(R.id.s12);
        s13 =  findViewById(R.id.s13);
        s14 =  findViewById(R.id.s14);
        s15 =  findViewById(R.id.s15);
        s16 =  findViewById(R.id.s16);
        s17 =  findViewById(R.id.s17);
        s18 =  findViewById(R.id.s18);
        s19 =  findViewById(R.id.s19);
        s20 =  findViewById(R.id.s20);

        user1 = new User("Cool Guy", "", "", "Fearless 😉", "", "");
        user2 = new User("Oreo", "", "", "Piano 🎹 is my fav", "", "");
        user3 = new User("Lemonade", "", "", "Take another step", "", "");
        user4 = new User("Luna", "", "", "I hate sight-reading", "", "");
        user5 = new User("Olivia", "", "", "😊", "", "");
        user6 = new User("Jack", "", "", "I've come too far to quit", "", "");
        user7 = new User("Oliver", "", "", "Mom's always right", "", "");
        user8 = new User("Grace", "", "", "🤔", "", "");
        user9 = new User("Leo", "", "", "Too much homework", "", "");
        user10 = new User("Poppy", "", "", "Poppy is not Poppy", "", "");
        user11 = new User("Mia", "", "", "i have fish today", "", "");
        user12 = new User("Ella", "", "", "My cat loves me", "", "");
        user13 = new User("Lucas", "", "", "I can actually do this", "", "");
        user14 = new User("Boba Tea", "", "", "Failure teaches me", "", "");
        user15 = new User("Aiden", "", "", "This is my moment", "", "");
        user16 = new User("Spider Man", "", "", "🐱", "", "");
        user17 = new User("Cherry", "", "", "🍦", "", "");
        user18 = new User("Niko", "", "", "Summer Summer", "", "");
        user19 = new User("Donut", "", "", "Sweeeeet", "", "");
        user20 = new User("Hunter", "", "", "Hunting for fun", "", "");

        // initialize bottom navigation bar
        BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);
        //set home selected
        bottom_navigation.setSelectedItemId(R.id.ic_groups);
        // perform itemselected listener;
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ic_grading:
                        if(Helper.loggedIn == true) {
                            startActivity(new Intent(getApplicationContext(), ScoreActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        } else {
                            startActivity(new Intent(getApplicationContext(), Empty_scoreActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        }
                    case R.id.ic_groups:
                        return true;

                    case R.id.ic_face:
                        // xinglu: tmp commented. ContactsContract.Profile.class haven't been initialized
                        // startActivity(new Intent(getApplicationContext(), ContactsContract.Profile.class));
                        if(Helper.loggedIn == true) {
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        } else {
                            startActivity(new Intent(getApplicationContext(), Empty_profileActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        }
                }
                return false;
            }
        });
    }

    public void nearby(View view){

        nearby.setBackgroundColor(Color.BLUE);
        rankingBoard.setBackgroundColor(Color.GRAY);
        name.setText("Name");
        slogan.setText("Slogan");

        u1.setText(user1.username);
        s1.setText(user1.slogan);

        u2.setText(user2.username);
        s2.setText(user2.slogan);

        u3.setText(user3.username);
        s3.setText(user3.slogan);

        u4.setText(user4.username);
        s4.setText(user4.slogan);

        u5.setText(user5.username);
        s5.setText(user5.slogan);

        u6.setText(user6.username);
        s6.setText(user6.slogan);

        u7.setText(user7.username);
        s7.setText(user7.slogan);

        u8.setText(user8.username);
        s8.setText(user8.slogan);

        u9.setText(user9.username);
        s9.setText(user9.slogan);

        u10.setText(user10.username);
        s10.setText(user10.slogan);

        u11.setText(user11.username);
        s11.setText(user11.slogan);

        u12.setText(user12.username);
        s12.setText(user12.slogan);

        u13.setText(user13.username);
        s13.setText(user13.slogan);

        u14.setText(user14.username);
        s14.setText(user14.slogan);

        u15.setText(user15.username);
        s15.setText(user15.slogan);

        u16.setText(user16.username);
        s16.setText(user16.slogan);

        u17.setText(user17.username);
        s17.setText(user17.slogan);

        u18.setText(user18.username);
        s18.setText(user18.slogan);

        u19.setText(user19.username);
        s19.setText(user19.slogan);

        u20.setText(user20.username);
        s20.setText(user20.slogan);

    }

    public void rankingBoard(View view){

        rankingBoard.setBackgroundColor(Color.BLUE);
        nearby.setBackgroundColor(Color.GRAY);
        name.setText("Name");
        slogan.setText("Score");

        List<String> names = new ArrayList<>();

        names.add("coconut");
        names.add("Sophia");
        names.add("AAA");
        names.add("Liam");
        names.add("Ava");
        names.add("James");
        names.add("Jo Jo");
        names.add("Emily");
        names.add("Lila");
        names.add("Spring");
        names.add("Teddy Bear");
        names.add("Piano Lover");
        names.add("Speedy");
        names.add("Secret");
        names.add("lazy lazy");
        names.add("Sunny");
        names.add("Cooooool");
        names.add("Ryan");
        names.add("Rainy Day");
        names.add("Cherry");

        List<String> score = new ArrayList<>();

        score.add("12900");
        score.add("11799");
        score.add("9970");
        score.add("9840");
        score.add("9711");
        score.add("8535");
        score.add("8520");
        score.add("8314");
        score.add("8301");
        score.add("8290");
        score.add("8100");
        score.add("8037");
        score.add("7892");
        score.add("7804");
        score.add("7756");
        score.add("7390");
        score.add("7299");
        score.add("7122");
        score.add("7123");
        score.add("7087");

        int i = 0;

        u1.setText(names.get(i));
        s1.setText(score.get(i++));

        u2.setText(names.get(i));
        s2.setText(score.get(i++));

        u3.setText(names.get(i));
        s3.setText(score.get(i++));

        u4.setText(names.get(i));
        s4.setText(score.get(i++));

        u5.setText(names.get(i));
        s5.setText(score.get(i++));

        u6.setText(names.get(i));
        s6.setText(score.get(i++));

        u7.setText(names.get(i));
        s7.setText(score.get(i++));

        u8.setText(names.get(i));
        s8.setText(score.get(i++));

        u9.setText(names.get(i));
        s9.setText(score.get(i++));

        u10.setText(names.get(i));
        s10.setText(score.get(i++));

        u11.setText(names.get(i));
        s11.setText(score.get(i++));

        u12.setText(names.get(i));
        s12.setText(score.get(i++));

        u13.setText(names.get(i));
        s13.setText(score.get(i++));

        u14.setText(names.get(i));
        s14.setText(score.get(i++));

        u15.setText(names.get(i));
        s15.setText(score.get(i++));

        u16.setText(names.get(i));
        s16.setText(score.get(i++));

        u17.setText(names.get(i));
        s17.setText(score.get(i++));

        u18.setText(names.get(i));
        s18.setText(score.get(i++));

        u19.setText(names.get(i));
        s19.setText(score.get(i++));

        u20.setText(names.get(i));
        s20.setText(score.get(i));
    }
}