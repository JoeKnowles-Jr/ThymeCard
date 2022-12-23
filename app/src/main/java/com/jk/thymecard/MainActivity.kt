package com.jk.thymecard

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jk.thymecard.ui.main.CalendarFragment
import com.jk.thymecard.ui.main.InputFragment
import com.jk.thymecard.ui.main.MainFragment

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .replace(R.id.container, InputFragment.newInstance())
            .commitNow()
    }

    fun showList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment.newInstance())
            .commitNow()
    }

    fun showCalendar() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CalendarFragment())
            .commitNow()
    }
}
