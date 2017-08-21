package com.example.tswords.floatview;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tswords.floatview.Manager.FloatViewManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button= (Button) findViewById(R.id.button);
//        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//        startActivity(intent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FloatViewManager fm=FloatViewManager.getInstance(MainActivity.this);
                fm.showFloatBallView();
            }
        });
    }

}
