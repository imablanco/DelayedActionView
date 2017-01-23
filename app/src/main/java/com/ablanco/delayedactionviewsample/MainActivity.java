package com.ablanco.delayedactionviewsample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ablanco.delayedactionview.DelayedActionView;


public class MainActivity extends AppCompatActivity implements DelayedActionView.DismissListener {

    private Handler handler = new Handler();
    private Runnable mDismissRunnable = new Runnable() {
        @Override
        public void run() {
            delayedActionView.dismiss(MainActivity.this);
        }
    };

    private DelayedActionView delayedActionView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        delayedActionView = (DelayedActionView) findViewById(R.id.delayed);
        handler.post(mDismissRunnable);
    }

    @Override
    public void onDismissed() {
        Toast.makeText(this, "Dismissed", Toast.LENGTH_SHORT).show();
        handler.removeCallbacks(mDismissRunnable);
        handler.postDelayed(mDismissRunnable, 2500);
    }

    @Override
    public void onCanceled() {
        Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
      /*  handler.removeCallbacks(mDismissRunnable);
        handler.postDelayed(mDismissRunnable, 2500);*/

    }
}
