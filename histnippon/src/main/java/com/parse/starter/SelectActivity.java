package com.parse.starter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class SelectActivity extends AppCompatActivity implements View.OnClickListener{

    private Button quickBtn;
    private Button everyBtn;

    public static ArrayList<String> questions;
    public static ArrayList<String> answers;

    public static String jidaiName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        quickBtn = (Button) findViewById(R.id.quickBtn);
        everyBtn = (Button) findViewById(R.id.everyBtn);

        quickBtn.setOnClickListener(this);
        everyBtn.setOnClickListener(this);

        Intent i = getIntent();
        jidaiName = i.getStringExtra("jidai");
        setTitle(jidaiName);

    }

    // method for calling checked questions
    public void initializeCheck(View view){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("com.caqmei.histnippon", MODE_PRIVATE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            Set<String> q_set = pref.getStringSet(jidaiName + "_q", null);
            Set<String> a_set = pref.getStringSet(jidaiName + "_a", null);
            if(q_set == null || q_set.size() == 0){
                Toast.makeText(getApplicationContext(), "この時代にメモった問題はありません。", Toast.LENGTH_LONG).show();
            } else {
                questions = new ArrayList<>(q_set);
                answers = new ArrayList<>(a_set);

                Intent i = new Intent(getApplicationContext(), QuestionActivity.class);
                startActivity(i);
            }
        }
    }
    @Override
    public void onClick(View view) {

        try {
            Cursor c = null;
            if(jidaiName.equals("全時代")){
                switch (view.getId()) {
                    case R.id.quickBtn:
                        c = StartActivity.myDatabase.rawQuery("SELECT * FROM Question NATURAL JOIN Answer ORDER BY RANDOM() LIMIT 15", null);
                        break;
                    case R.id.everyBtn:
//                        SharedPreferences pref = getApplicationContext().getSharedPreferences("com.caqmei.histnippon", MODE_PRIVATE);
//                        new AlertDialog.Builder(getApplicationContext())
//                                .setTitle("モード選沢")
//                                .setMessage("選んでください")
//                                .setPositiveButton("続きから", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                    }
//                                })
//                                .setNegativeButton("はじめから", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                    }
//                                })
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .show();
                        c = StartActivity.myDatabase.rawQuery("SELECT * FROM Question NATURAL JOIN Answer ORDER BY RANDOM()", null);
                        break;
                }
            } else {
                switch (view.getId()) {
                    case R.id.quickBtn:
                        c = StartActivity.myDatabase.rawQuery("SELECT * FROM Question NATURAL JOIN Answer WHERE jidai = ? ORDER BY RANDOM() LIMIT 15", new String[]{jidaiName});
                        break;
                    case R.id.everyBtn:
                        c = StartActivity.myDatabase.rawQuery("SELECT * FROM Question NATURAL JOIN Answer WHERE jidai = ? ORDER BY RANDOM()", new String[]{jidaiName});
                        break;
                }
            }
            int descriptionIndex = c.getColumnIndex("q_description");
            int answerIndex = c.getColumnIndex("a_description");

            questions = new ArrayList<>();
            answers = new ArrayList<>();

            c.moveToFirst();

            while (c != null) {

                questions.add(c.getString(descriptionIndex));
                answers.add(c.getString(answerIndex));

                c.moveToNext();
            }
            c.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        Intent i = new Intent(getApplicationContext(), QuestionActivity.class);
        i.putExtra("jidai", jidaiName);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
