package com.ablanco.delayeddismissview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ablanco.library.DelayedDismissView;

public class MainActivity extends AppCompatActivity implements DelayedDismissView.DismissListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DelayedDismissView delayedDismissView = (DelayedDismissView) findViewById(R.id.delayed);
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delayedDismissView.dismiss(MainActivity.this);
            }
        });
    }

    @Override
    public void onDismissed() {
        Toast.makeText(this, "Dismissed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCanceled() {
        Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();

    }
}
