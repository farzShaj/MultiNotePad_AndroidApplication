package com.example.MultiNotePad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class AboutPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      setTitle("");
      getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);


    }
}
