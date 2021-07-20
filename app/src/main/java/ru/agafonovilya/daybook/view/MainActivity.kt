package ru.agafonovilya.daybook.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.agafonovilya.daybook.R
import ru.agafonovilya.daybook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(Screens.HomeScreen().getFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item) {
                R.id.home -> replaceFragment(Screens.HomeScreen().getFragment())
                R.id.schedule -> replaceFragment(Screens.ScheduleScreen().getFragment())
                R.id.notes -> showToast("Selected fragment not implemented")
                R.id.selected -> showToast("Selected fragment not implemented")
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
                supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}