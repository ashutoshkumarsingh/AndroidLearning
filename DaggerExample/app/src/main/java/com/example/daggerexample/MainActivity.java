package com.example.daggerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    TextView txtEggs;
    @Inject
    String eggs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtEggs = findViewById(R.id.eggs);


        ((EggsApplication) getApplication())
                .getAppComponent()
                .inject(this);

        txtEggs.setText("Cooked: " + eggs);

    }
}
