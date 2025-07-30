package com.hakif.StoryApp.ui.auth.signUp

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hakif.StoryApp.R
import com.hakif.StoryApp.data.state.AuthState
import com.hakif.StoryApp.databinding.ActivitySignUpBinding
import com.hakif.StoryApp.ui.auth.signIn.SignInActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private var _binding: ActivitySignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickableSignUp()
        btnSignUp()
        checkRegisterState()
    }

    private fun checkRegisterState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect { state ->
                    when(state) {
                        is AuthState.Idle -> {
                            // Initial state, do nothing
                        }
                        is AuthState.Loading -> {
                            // Show loading indicator

                        }
                        is AuthState.Success -> {
                            Toast.makeText(this@SignUpActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                        }
                        is AuthState.Error -> {
                            Toast.makeText(this@SignUpActivity, state.message , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun btnSignUp() {
        binding.btnSignUp.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            when {
                username.isEmpty() || email.isEmpty() || password.isEmpty() -> {
                    Toast.makeText(this, "All fields must be filled in.", Toast.LENGTH_SHORT).show()
                }

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show()
                }

//                !isValidPassword(password) -> {
//                    Toast.makeText(this, "Password must be at least 8 characters. include uppercase, lowercase, numbers, and symbols.", Toast.LENGTH_LONG).show()
//                }

                else -> {
                    viewModel.register(username, email, password)
                }
            }
        }
    }

    private fun setupClickableSignUp() {
        val fullText = "Already have an account? Sign In"
        val signUpText = "Sign In"
        val spannable = SpannableString(fullText)
        val startIndex = fullText.indexOf(signUpText)
        val endIndex = startIndex + signUpText.length


        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.bgColor = android.graphics.Color.TRANSPARENT
                ds.color = ContextCompat.getColor(this@SignUpActivity, R.color.green)
            }
        }

        spannable.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvSignIn.text = spannable
        binding.tvSignIn.movementMethod = LinkMovementMethod.getInstance()
        binding.tvSignIn.highlightColor = android.graphics.Color.TRANSPARENT
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}