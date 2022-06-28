package com.example.mytvdemo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.mytvdemo.R;
import com.example.mytvdemo.fragments.ErrorFragment;

public class ErrorActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
//        FrameLayout frameLayout = findViewById(R.id.error_homepage);
//        ErrorFragment errorFragment = new ErrorFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.error_homepage,errorFragment).commit();
    }
}