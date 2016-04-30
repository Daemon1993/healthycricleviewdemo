package com.example.administrator.healthycricleviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon1993.dhealthyprogressview.DHealthyProgressView;


public class MainActivity extends AppCompatActivity {

    private DHealthyProgressView healthyProgressView;
    private EditText et_input;
    private Button bt_show;
    private TextView tv_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_input = (EditText) findViewById(R.id.et_input);
        bt_show = (Button) findViewById(R.id.bt_show);
        tv_value = (TextView) findViewById(R.id.tv_value);

        healthyProgressView = (DHealthyProgressView) findViewById(R.id.simple);

        //healthyProgressView.beginContinue(true);

        healthyProgressView.setInterpolator(new AccelerateInterpolator());

        healthyProgressView.setmValue(85);


        bt_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    float value = Float.valueOf(et_input.getText().toString().trim());
                    if(value>100){
                        throw new NumberFormatException();
                    }
                    healthyProgressView.setmValue(value);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "请输入有效进度", Toast.LENGTH_SHORT).show();
                }


            }
        });

        healthyProgressView.setOnValueChangeListener(new DHealthyProgressView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                tv_value.setText((int)value+"");
            }
        });

//        AroundCircleView acv_icon = (AroundCircleView) findViewById(R.id.acv_icon);
//
//
//        acv_icon.setProgress(66);
    }
}
