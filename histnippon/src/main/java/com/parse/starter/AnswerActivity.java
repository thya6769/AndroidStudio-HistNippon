package com.parse.starter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class AnswerActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView content;
    private TextView title;
    private Button nextBtn;
    private TextView userAnswerText;
    private CheckBox checkBox;

//    private Toolbar toolbar;
//    private TextView toolbarTitle;

    public static int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        content = (TextView) findViewById(R.id.content);
        userAnswerText = (TextView) findViewById(R.id.userAnswerText);
        title = (TextView) findViewById(R.id.questionNum);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
//        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
//        toolbar = (Toolbar) findViewById(R.id.tool_bar);


        Intent i = getIntent();
        String userAnswer = i.getStringExtra("userAnswer");
        final String jidaiName = SelectActivity.jidaiName;

        if(QuestionActivity.isCorrect) {
            title.setText("正解！！！");
            score++;
        } else {
            title.setText("残念！！！");
        }
        userAnswerText.setText("<あなたの答え>　" + userAnswer);
        content.setText("<答え>　" + SelectActivity.answers.get(QuestionActivity.index));

        nextBtn.setOnClickListener(this);

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(null);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        final SharedPreferences pref = getApplicationContext().getSharedPreferences("com.caqmei.histnippon", MODE_PRIVATE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            Set<String> set = pref.getStringSet("全時代_a", null);
            // if the set contains then give a check if not do nothing
            if (set.contains(SelectActivity.answers.get(QuestionActivity.index))) {
                checkBox.setChecked(true);
            }
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = pref.edit();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    Set<String> all_a_set = pref.getStringSet("全時代_a", null);
                    Set<String> all_q_set = pref.getStringSet("全時代_q", null);
                    Set<String> q_set;
                    Set<String> a_set;
                    String jidai = null;
                    if(jidaiName.equals("全時代")) {
                        try {
                            Cursor c = StartActivity.myDatabase.rawQuery("SELECT * FROM Question WHERE q_description = ?"
                                    , new String[]{SelectActivity.questions.get(QuestionActivity.index)});
                            int jidaiIndex = c.getColumnIndex("jidai");
                            c.moveToFirst();
                            jidai = c.getString(jidaiIndex);
                            Log.i("AppInfo", jidai);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        q_set = pref.getStringSet(jidai + "_q", null);
                        a_set = pref.getStringSet(jidai + "_a", null);
                    } else {
                        q_set = pref.getStringSet(jidaiName + "_q", null);
                        a_set = pref.getStringSet(jidaiName + "_a", null);
                    }

                    if(q_set == null){
                        q_set = new HashSet<>();
                        a_set = new HashSet<>();
                    }
                    if(all_q_set == null) {
                        all_q_set = new HashSet<>();
                        all_a_set = new HashSet<>();
                    }
                    if (isChecked) {
                        q_set.add(SelectActivity.questions.get(QuestionActivity.index));
                        a_set.add(SelectActivity.answers.get(QuestionActivity.index));
                        all_q_set.add(SelectActivity.questions.get(QuestionActivity.index));
                        all_a_set.add(SelectActivity.answers.get(QuestionActivity.index));
                    } else {
                        q_set.remove(SelectActivity.questions.get(QuestionActivity.index));
                        a_set.remove(SelectActivity.answers.get(QuestionActivity.index));
                        all_q_set.remove(SelectActivity.questions.get(QuestionActivity.index));
                        all_a_set.remove(SelectActivity.answers.get(QuestionActivity.index));
                    }
                    if(jidaiName.equals("全時代")) {
                        editor.putStringSet(jidai + "_q", q_set);
                        editor.putStringSet(jidai + "_a", a_set);
                    } else {
                        editor.putStringSet(jidaiName + "_q", q_set);
                        editor.putStringSet(jidaiName + "_a", a_set);
                    }
                    editor.putStringSet("全時代_q", q_set);
                    editor.putStringSet("全時代_a", a_set);
                } else {
                    Toast.makeText(getApplicationContext(), "OSのバージョンをアップデートしてください。", Toast.LENGTH_LONG).show();
                }
                editor.commit();
                Log.i("AppInfo", "Saved!");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.nextBtn:
                Intent i;
                if(QuestionActivity.index != SelectActivity.answers.size() - 1) {
                    i = new Intent(getApplicationContext(), QuestionActivity.class);
                    QuestionActivity.index++;
                    startActivity(i);
                } else {
                    i = new Intent(getApplicationContext(), ScoreActivity.class);
                    startActivity(i);
                }
                break;
        }
    }
}
