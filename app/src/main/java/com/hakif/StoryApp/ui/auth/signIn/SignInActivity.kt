package com.hakif.StoryApp.ui.auth.signIn

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hakif.StoryApp.ui.MainActivity
import com.hakif.StoryApp.R
import com.hakif.StoryApp.data.state.AuthState
import com.hakif.StoryApp.databinding.ActivitySignInBinding
import com.hakif.StoryApp.ui.auth.signUp.SignUpActivity
import com.hakif.StoryApp.ui.auth.signUp.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private var _binding: ActivitySignInBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnLogin()
        observeLoginState()
        setupClickableSignUp()
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect { state ->
                    when(state) {
                        is AuthState.Idle -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnSignIn.isEnabled = true
                        }
                        is AuthState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.btnSignIn.isEnabled = false
                        }
                        is AuthState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnSignIn.isEnabled = false
                            val intent = Intent(this@SignInActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            Toast.makeText(this@SignInActivity, "Login successful", Toast.LENGTH_SHORT).show()
                        }
                        is AuthState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnSignIn.isEnabled = true
                            Toast.makeText(this@SignInActivity, state.message , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun btnLogin()
    {
        binding.btnSignIn.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    viewModel.login(email, password)
                } else {
                    binding.edtEmail.error = "Invalid email format"
                }
            } else {
                if (email.isEmpty()) binding.edtEmail.error = "Email cannot be empty"
                if (password.isEmpty()) binding.edtPassword.error = "Password cannot be empty"
            }
        }
    }

    private fun setupClickableSignUp() {
        val fullText = "Don’t have an account? Sign Up"
        val signUpText = "Sign Up"
        val spannable = SpannableString(fullText)
        val startIndex = fullText.indexOf(signUpText)
        val endIndex = startIndex + signUpText.length


        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.bgColor = android.graphics.Color.TRANSPARENT
                ds.color = ContextCompat.getColor(this@SignInActivity, R.color.green)
            }
        }

        spannable.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvSignUp.text = spannable
        binding.tvSignUp.movementMethod = LinkMovementMethod.getInstance()
        binding.tvSignUp.highlightColor = android.graphics.Color.TRANSPARENT
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}