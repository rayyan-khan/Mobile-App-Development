package com.example.rkhan2019.lab5p2;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    counter ctr = new counter();
    TextView create;
    TextView start;
    TextView resume;
    TextView restart;
    TextView pause;
    TextView stop;
    TextView destroy;
    SharedPreferences sp;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        create = findViewById(R.id.textView);
        start = findViewById(R.id.textView2);
        resume = findViewById(R.id.textView3);
        pause = findViewById(R.id.textView4);
        stop = findViewById(R.id.textView5);
        restart = findViewById(R.id.textView7);
        destroy = findViewById(R.id.textView8);

        gson = new Gson();

        sp = getSharedPreferences("com.example.rkhan2019.lab5p2", 0);
        SharedPreferences.Editor spe = sp.edit();
        spe.putString("K","V");
        spe.commit();
        String str = sp.getString("K","");
        Log.i("Key1",str);
        String json = sp.getString("counter","");
        Log.i("Counter",sp.getString("counter",""));
        Log.i("tag", json);
        if(json.equals("")){
            ctr=new counter();
        }
        else{
        ctr = gson.fromJson(json,ctr.getClass());
        }

        ctr.increment("create");
        update();
    }

    public void update(){
        //sp = getSharedPreferences("com.example.rkhan2019.lab5p2", 0);
        String createText = "Create: " + ctr.getCreateClick();
        String startText = "start: " + ctr.getStartClick();
        String resumeText = "Resume:" + ctr.getResumeClick();
        String restartText = "Restart:" + ctr.getRestartClick();
        String pauseText = "Pause: " + ctr.getPauseClick();
        String stopText = "Stop: " + ctr.getStopClick();
        String destroyText = "Destroy: " + ctr.getDestroyClick();
        create.setText(createText);
        start.setText(startText);
        resume.setText(resumeText);
        restart.setText(restartText);
        pause.setText(pauseText);
        stop.setText(stopText);
        destroy.setText(destroyText);
        sp.edit().putString("counter",gson.toJson(ctr)).commit();
        Log.i("update", sp.getString("counter", ""));
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView start = findViewById(R.id.textView2);
        ctr.increment("start");
        update();
    }

    protected void onResume(){
        super.onResume();
        TextView resume=findViewById(R.id.textView3);
        ctr.increment("resume");
        update();
    }

    protected void onPause(){
        super.onPause();
        TextView pause=findViewById(R.id.textView4);
        ctr.increment("pause");
        update();
    }

    protected void onStop(){
        super.onStop();
        TextView stop=findViewById(R.id.textView5);
        ctr.increment("stop");
        update();
    }

    protected void onRestart(){
        TextView restart=findViewById(R.id.textView7);
        ctr.increment("restart");
        update();
        super.onRestart();
    }

    protected void onDestroy(){
        //sp = getSharedPreferences("com.example.rkhan2019.lab5p2",0);
        ctr.increment("destroy");
        Gson gson = new Gson();
        sp.edit().putString("counter",gson.toJson(ctr)).commit();
        super.onDestroy();
    }

}
