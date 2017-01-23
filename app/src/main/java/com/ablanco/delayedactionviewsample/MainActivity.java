package com.ablanco.delayedactionviewsample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ablanco.delayedactionview.DelayedActionView;


public class MainActivity extends AppCompatActivity implements DelayedActionView.ActionListener {

    private Handler handler = new Handler();
    private Runnable mActionRunnable = new Runnable() {
        @Override
        public void run() {
            delayedActionView.start(MainActivity.this);
        }
    };

    private DelayedActionView delayedActionView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        delayedActionView = (DelayedActionView) findViewById(R.id.delayed);
        handler.post(mActionRunnable);
        delayedActionView.start(new DelayedActionView.ActionListener() {
            @Override
            public void onAction() {

            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @Override
    public void onAction() {
        Toast.makeText(this, "Action!", Toast.LENGTH_SHORT).show();
        handler.removeCallbacks(mActionRunnable);
        handler.postDelayed(mActionRunnable, 2500);
    }

    @Override
    public void onCanceled() {
        Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
    }
}
