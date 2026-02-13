package com.example.playlistmaker.ui.root.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    private lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.activityRoot) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bottomNav = binding.bottomNavigationView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.audioPlayerFragment -> {
                    hideBottomNavigationView()
                }

                else -> {
                    showBottomNavigationView()
                }
            }

            bottomNav.setupWithNavController(navController)
        }
    }

    private fun hideBottomNavigationView() {
        bottomNav.apply {
            if (!isVisible) {
                return
            }

            animate()
                .translationY(bottomNav.height.toFloat())
                .setDuration(BOTTOM_NAV_ANIMATION_DURATION)
                .withEndAction {
                    bottomNav.isVisible = false
                }
                .start()
        }
    }

    private fun showBottomNavigationView() {
        bottomNav.apply {
            if (isVisible) {
                return
            }
            isVisible = true
            animate()
                .translationY(0f)
                .setDuration(BOTTOM_NAV_ANIMATION_DURATION)
                .start()
        }
    }

    companion object {
        const val BOTTOM_NAV_ANIMATION_DURATION = 300L
    }
}