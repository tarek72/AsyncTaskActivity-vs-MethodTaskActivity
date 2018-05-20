package com.example.sawara.tarek.ex3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button asyncButton;
    Button threadButton;

    public static String CREATE_MSG = "Created";
    public static String DONE_MSG = "Done";
    public static String CANCEL_MSG = "Cancelled";
    public static String START_ERROR = "In order to start first press Create";
    public static String CANCEL_ERROR = "In order to cancel first press Create";
    public static String COUNTING_ERROR = "In middle of counting!!";
    public static final String THREAD = "thread";
    public static final String ASYNC_TASK = "asyncTask";
    public static String ARG1 = "methodType";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        asyncButton = (Button)findViewById(R.id.buttonAsync);
        threadButton = (Button)findViewById(R.id.buttonThread);


        asyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,MethodTaskActivity.class);
                myIntent.putExtra(ARG1,ASYNC_TASK);
                startActivity(myIntent);
            }
        });

        threadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,MethodTaskActivity.class);
                myIntent.putExtra(ARG1,THREAD);
                startActivity(myIntent);
            }
        });

    }
}
