package com.hakif.StoryApp.ui.opening

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hakif.StoryApp.R
import com.hakif.StoryApp.databinding.ActivitySplashScreenBinding
import com.hakif.StoryApp.ui.MainActivity
import com.hakif.StoryApp.ui.auth.signIn.SignInActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashScreenViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()


        moveToNextScreen()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgLogo, View.TRANSLATION_Y, -30f, 30f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun moveToNextScreen() {
        lifecycleScope.launch {
            delay(3000)
            viewModel.checkLoginStatus { isLoggedIn ->
                binding.progressBar.visibility = View.GONE
                if (isLoggedIn) {
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashScreenActivity, SignInActivity::class.java))
                }
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}