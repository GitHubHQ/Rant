/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //if no current user cached, start login activity
        if(ParseUser.getCurrentUser().getEmail()==null){
            ParseLoginBuilder builder = new ParseLoginBuilder(this);
            startActivityForResult(builder.build(), 0);
        }else{
            //jank way to call onActivityResult
            onActivityResult(0,RESULT_OK,null);
        }
        ParseAnalytics.trackAppOpenedInBackground(getIntent());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if user successfully logs in, initialize the view, else quit
        if(resultCode == RESULT_OK){
            //init activity_main
            setContentView(R.layout.activity_main);
            //logout button basically restarts MainActivity after logging out
            Button logout = (Button) findViewById(R.id.logout);
            Button start = (Button) findViewById(R.id.message);
            Button buds = (Button) findViewById(R.id.buddies);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseUser.logOut();
                    finish();
                    Intent i = new Intent(MainActivity.this, DispatchActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                }
            });
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });
            buds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
                    final Intent serviceIntent = new Intent(getApplicationContext(), SinchService.class);
                    startActivity(intent);
                    startService(serviceIntent);
                }
            });

            ParseUser user = ParseUser.getCurrentUser();
            showProfile(user);
            user.put("rating",5);
            user.saveInBackground();
        }else{
            finish();
        }
    }
    @Override
    public void onDestroy() {
        stopService(new Intent(this, SinchService.class));
        super.onDestroy();
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

    /**
     * Shows the profile of the given user.
     *
     * @param user
     */
    private void showProfile(ParseUser user) {
        TextView tv = (TextView) findViewById(R.id.user);
        if (user != null) {
            tv.setText("Welcome, " + user.get("name").toString() + "!");
        }
    }
}
