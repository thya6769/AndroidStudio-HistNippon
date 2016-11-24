/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseAnalytics;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

  private ListView jidaiList;
  private ArrayAdapter arrayAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    jidaiList = (ListView) findViewById(R.id.listView);

    final ArrayList<String> jidais = new ArrayList<>();

    jidais.add("全時代");
    jidais.add("旧石器・縄文時代");
    jidais.add("弥生時代");
    jidais.add("古墳時代");
    jidais.add("飛鳥時代");
    jidais.add("奈良時代");
    jidais.add("平安時代");
    jidais.add("鎌倉時代");
    jidais.add("室町時代");
    jidais.add("戦国・安土桃山時代");
    jidais.add("江戸時代");
    jidais.add("明治時代");
    jidais.add("大正時代");
    jidais.add("昭和・現代");

    arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, jidais);

    jidaiList.setAdapter(arrayAdapter);

    jidaiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent i = new Intent(getApplicationContext(), SelectActivity.class);
        i.putExtra("jidai", jidais.get(position));
        startActivity(i);
      }
    });

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

}
