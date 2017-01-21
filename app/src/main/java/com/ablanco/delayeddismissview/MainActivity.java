package com.ablanco.delayeddismissview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements DelayedDismissView.DismissListener {

    private Handler handler = new Handler();
    private Runnable mDismissRunnable = new Runnable() {
        @Override
        public void run() {
            delayedDismissView.dismiss(MainActivity.this);
        }
    };

    private DelayedDismissView delayedDismissView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        delayedDismissView = (DelayedDismissView) findViewById(R.id.delayed);
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
