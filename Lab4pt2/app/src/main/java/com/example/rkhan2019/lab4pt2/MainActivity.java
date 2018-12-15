package com.example.rkhan2019.lab4pt2;

import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button mButton;
    EditText mEditText;
    TextView mTextView;
    SeekBar mSeekbar;
    int onColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button)findViewById(R.id.button);
        mButton.setOnClickListener(this);

//        mEditText = (EditText)findViewById(R.id.editText);
//        mEditText.setOnClickListener(this);

        mTextView = (TextView)findViewById(R.id.textView);

        mSeekbar = (SeekBar)findViewById(R.id.seekBar);

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mTextView.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        mTextView.setText(mEditText.getText().toString());
    }

    public void editTextClick(View v){
        onColor++;
        int mod = onColor%4;
        String[] colors = {"#103749", "#AEFCFE", "#F389ED", "#F9E2FD"};
        v.setBackgroundColor(Color.parseColor(colors[mod]));
    }
}
