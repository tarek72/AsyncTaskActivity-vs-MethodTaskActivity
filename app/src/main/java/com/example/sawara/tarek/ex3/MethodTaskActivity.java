package com.example.sawara.tarek.ex3;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MethodTaskActivity extends AppCompatActivity {
    TextView counterTV;
    Button createButton;
    Button startButton;
    Button cancelButton;
    MyAsyncTask myAsyncTask;
    MyThread myThread;
    boolean createClicked;
    boolean isCounting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_layout);
        counterTV = findViewById(R.id.textViewCounter);
        createButton = findViewById(R.id.buttonCreate);
        startButton = findViewById(R.id.buttonStart);
        cancelButton = findViewById(R.id.buttonCancel);
        createClicked = false;
        isCounting = false;

        Intent myIntent = getIntent(); // gets the previously created intent
        final String methodType = myIntent.getStringExtra(MainActivity.ARG1);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMethod(methodType);
                createClicked = true;
            }

            private void createMethod(String methodType) {
                switch(methodType){
                    case MainActivity.ASYNC_TASK:
                        myAsyncTask = new MyAsyncTask();
                        myAsyncTask.onPreExecute();
                    case MainActivity.THREAD:
                        Log.e("methodType", methodType);
                        myThread = new MyThread();
                        myThread.setup();
                    default:
                        break;
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startMethod() == false)
                {
                    Log.e("startMethod",String.valueOf(false));
                    if(isCounting == true){
                        Toast toast = Toast.makeText(getApplicationContext(), MainActivity.COUNTING_ERROR, Toast.LENGTH_SHORT);
                        toast.show();
                    }else {

                        counterTV.setText(MainActivity.START_ERROR);
                        myAsyncTask = null;
                        myThread = null;
                    }
                }
            }

            private boolean startMethod() {
                switch(methodType){
                    case MainActivity.ASYNC_TASK:
                        if (myAsyncTask != null && createClicked == true && isCounting == false) {
                            Log.e("startMethod",String.valueOf(createClicked));
                            myAsyncTask.execute();
                            return true;
                        }else{
                            return false;
                        }
                    case MainActivity.THREAD:
                        if (myThread != null && createClicked == true && isCounting == false) {
                            myThread.start();
                            return true;
                        }else{
                            return false;
                        }
                    default:
                        return false;
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelMethod() == false){
                counterTV.setText(MainActivity.CANCEL_ERROR);
                }
            }

            private boolean cancelMethod() {
                switch(methodType){
                    case MainActivity.ASYNC_TASK:
                        if (myAsyncTask != null && createClicked == true) {
                            myAsyncTask.cancel(true);
                            createClicked = false;
                            return true;
                        } else {
                            return false;
                        }
                    case MainActivity.THREAD:
                        if (myThread != null && createClicked == true) {
                            myThread.onCancelProgress();
                            return true;
                        }else{
                            return false;
                        }
                    default:
                        return false;
                }

            }
        });
    }

    /**
     * The asynced task class
     */
    public class MyAsyncTask extends AsyncTask<Void, Integer, String> {
        int counter;

        @Override
        protected void onPreExecute() {
            counter = 1;
            counterTV.setText(MainActivity.CREATE_MSG);
        }

        @Override
        protected String doInBackground(Void... voids) {

            isCounting = true;
            synchronized (this) {
                while (counter <= 10) {
                    try {
                        publishProgress(counter);
                        wait(500);
                        counter++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("is couting", String.valueOf(isCounting));
                isCounting = false;

                createClicked = false;
                return MainActivity.DONE_MSG;
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate();
            counterTV.setText(String.valueOf(values[0]));
        }

        @Override
        public void onPostExecute(String s) {
            counterTV.setText(s);
        }

        @Override
        public void onCancelled() {
            super.onCancelled();
            counterTV.setText(MainActivity.CANCEL_MSG);
        }


    }

    /**
     * The thread class
     */
    class MyThread {
        int counter;
        Thread thread;


        protected void setup() {
            counter = 1;
            counterTV.setText(MainActivity.CREATE_MSG);
        }

        void start() {
            thread = new Thread() {
                @Override
                public void run() {
                    isCounting = true;
                    while (counter <= 10) {
                        try {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onUpdateProgress(counter);
                                }
                            });
                            sleep(500);
                        } catch (InterruptedException e) {
                            onCancelProgress();
                            e.printStackTrace();
                        }
                    }
                    if (!this.isInterrupted()) {

                        onPostProgress();
                    }
                    isCounting = false;
                    createClicked = false;
                }
            };
            thread.start();

        }

        private void onPostProgress() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    counterTV.setText(MainActivity.DONE_MSG);
                }
            });
        }

        void onUpdateProgress(Integer integer) {
            counterTV.setText(String.valueOf(integer));
            counter++;

        }

        void onCancelProgress() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    thread.interrupt();
                    counterTV.setText(MainActivity.CANCEL_MSG);
                }
            });
        }


    }

}