package com.example.tzfloodfill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maximot.floodfill.floodfill.ui.fragment.FloodfillFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


    private fun setupFloodfillFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, FloodfillFragment.create())
                .commitNow()
        }
    }
}