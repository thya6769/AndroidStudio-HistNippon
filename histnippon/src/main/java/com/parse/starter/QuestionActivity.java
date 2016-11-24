package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {

    private TextView content;
    private EditText inputField;
    private TextView questionText;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    public static boolean isCorrect = false;

    public static int index = 0;


    public void checkAnswer(View view){
        String userAnswer = inputField.getText().toString();
        if(userAnswer.equals(SelectActivity.answers.get(index))){
            isCorrect = true;
        } else {
            isCorrect = false;
        }

        Intent i = new Intent(getApplicationContext(), AnswerActivity.class);
        i.putExtra("userAnswer", userAnswer);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        content = (TextView) findViewById(R.id.content);
        inputField = (EditText) findViewById(R.id.inputField);
        questionText = (TextView) findViewById(R.id.questionNum);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);


        questionText.setText(getString(R.string.question_num, index + 1, SelectActivity.questions.size()));
        content.setText(SelectActivity.questions.get(index));

        String jidaiName = SelectActivity.jidaiName;

        toolbarTitle.setText(jidaiName);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.home){
            finish();
            Log.i("AppInfo", "Button tapped!");
        }
        return super.onOptionsItemSelected(item);
    }
}
