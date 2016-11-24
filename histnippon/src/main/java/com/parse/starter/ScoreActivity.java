package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private Button homeBtn;
    private TextView scoreText;
    private TextView moneyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        homeBtn = (Button) findViewById(R.id.homeBtn);
        scoreText = (TextView) findViewById(R.id.scoreText);
        moneyText = (TextView) findViewById(R.id.moneyText);

        scoreText.setText(AnswerActivity.score + "/" + SelectActivity.answers.size());
        int mon = AnswerActivity.score * 100;
        moneyText.setText(mon + "文");

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), StartActivity.class);
                QuestionActivity.index = 0;
                startActivity(i);
            }
        });
    }

}
