package com.example.pdedio.sendsnap.authorization;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pdedio.sendsnap.R;

import org.androidannotations.annotations.EActivity;

@EActivity
public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }
}
