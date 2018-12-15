package com.example.rkhan2019.lab5p1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView buttonCreate;
    TextView buttonStart;
    TextView buttonResume;
    TextView buttonPause;
    TextView buttonStop;
    TextView buttonRestart;
    TextView buttonDestroy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        counter.increment("create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonCreate = findViewById(R.id.textView);
        update(buttonCreate,counter.getCreateClick());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Log.i("abc", "created");
    }

    public void update(TextView t, int s){
        t.setText(t.getTag().toString() + s);
    }

    @Override
    protected void onStart(){
        buttonStart = findViewById(R.id.textView2);
        counter.increment("start");
        update(buttonStart,counter.getStartClick());
        super.onStart();
    }
    @Override
    protected void onResume(){
        buttonResume = findViewById(R.id.textView3);
        counter.increment("resume");
        update(buttonResume,counter.getResumeClick());
        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
        buttonPause=findViewById(R.id.textView4);
        counter.increment("pause");
        update(buttonPause,counter.getPauseClick());
    }
    @Override
    protected void onStop(){
        super.onStop();
        buttonStop=findViewById(R.id.textView5);
        counter.increment("stop");
        update(buttonStop,counter.getStopClick());
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        buttonRestart=findViewById(R.id.textView6);
        counter.increment("restart");
        update(buttonRestart,counter.getRestartClick());
    }
    @Override
    protected void onDestroy(){
        buttonDestroy=findViewById(R.id.textView7);
        counter.increment("destroy");
        update(buttonDestroy,counter.getDestroyClick());
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
}
