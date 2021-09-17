package com.example.mobiusdemoapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobiusdemoapplication.ui.scooter.ScooterFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.flMainHolder, ScooterFragment())
            .commit()
    }
}