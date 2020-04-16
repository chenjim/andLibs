package com.chenjim.andlibs.demo.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.chenjim.andlibs.demo.R;


/**
 * @author jim.chen
 */
public class ActivityBase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
