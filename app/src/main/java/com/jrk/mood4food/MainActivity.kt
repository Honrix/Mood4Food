package com.jrk.mood4food

import android.os.Bundle

class MainActivity : NavBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_home)
        super.onCreate(savedInstanceState)
    }
}