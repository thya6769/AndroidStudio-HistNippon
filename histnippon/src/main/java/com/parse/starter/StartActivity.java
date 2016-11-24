package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView settingBtn;
    private Button jidaiBtn;
    private TextView moneyText;
    private TextView mibunText;

    public static SQLiteDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        jidaiBtn = (Button) findViewById(R.id.jidaiBtn);
        settingBtn = (ImageView) findViewById(R.id.settingBtn);
        moneyText = (TextView) findViewById(R.id.moneyText);
        mibunText = (TextView) findViewById(R.id.mibunText);

        jidaiBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);


        SharedPreferences pref = getSharedPreferences("com.caqmei.histnippon", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putInt("money", 193042);
//        editor.commit();

        int money = pref.getInt("money", 0);
        int mon = money % 4000;
        int monme = (money / 4000) % 60;
        int ryo = (money / 240000);
        moneyText.setText(ryo + "両" + monme + "匁" + mon +"文");

        if(ryo >= 3 && ryo < 50){
            mibunText.setText("商人");
        } else if(ryo >= 50 && ryo < 200){
            mibunText.setText("武士");
        } else if(ryo >= 200) {
            mibunText.setText("大富豪");
        }

        try {

            Log.i("AppInfo", "Doing database query...");
            myDatabase = openOrCreateDatabase("HistNippon", MODE_PRIVATE, null);

            myDatabase.beginTransaction();
            myDatabase.execSQL("DROP TABLE IF EXISTS Question");
            myDatabase.execSQL("DROP TABLE IF EXISTS Answer");
            myDatabase.setTransactionSuccessful();
            myDatabase.endTransaction();

            myDatabase.beginTransaction();

            myDatabase.execSQL("CREATE TABLE Question (" +
                    "q_id INTEGER PRIMARY KEY," +
                    "jidai TEXT NOT NULL," +
                    "q_description TEXT UNIQUE" +
                    ");");
            myDatabase.execSQL("CREATE TABLE Answer (" +
                    "answer_id INTEGER PRIMARY KEY," +
                    "a_description TEXT UNIQUE," +
                    "q_id INT REFERENCES Question(q_id)" +
                    ");");
            myDatabase.setTransactionSuccessful();
            myDatabase.endTransaction();

            myDatabase.beginTransaction();
            myDatabase.execSQL("DELETE FROM Question");
            myDatabase.execSQL("DELETE FROM Answer");
            myDatabase.setTransactionSuccessful();
            myDatabase.endTransaction();

            myDatabase.beginTransaction();

            insertFromFile(this, R.raw.insert_hist_nippon);
            myDatabase.setTransactionSuccessful();
            myDatabase.endTransaction();

//            SharedPreferences preferences = getSharedPreferences("com.caqmei.histnippon", MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.clear();
//            editor.commit();

        } catch(Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * This reads a file from the given Resource-Id and calls every line of it as a SQL-Statement
     *
     * @param context
     *
     * @param resourceId
     *  e.g. R.raw.food_db
     *
     * @return Number of SQL-Statements run
     * @throws IOException
     */
    public void insertFromFile(Context context, int resourceId) throws IOException {


        // Open the resource
        InputStream insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        while (insertReader.ready()) {
            String insertStmt = insertReader.readLine();
            myDatabase.execSQL(insertStmt);
        }
        insertReader.close();

    }


    @Override
    public void onClick(View view) {
        Intent i;
        switch(view.getId()){
            case R.id.jidaiBtn:
                i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                break;
            case R.id.settingBtn:
                i = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(i);
                break;
        }
    }
}
