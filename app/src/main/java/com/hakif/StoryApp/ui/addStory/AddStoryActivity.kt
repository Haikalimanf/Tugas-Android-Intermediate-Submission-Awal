package com.hakif.StoryApp.ui.addStory

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hakif.StoryApp.R
import com.hakif.StoryApp.data.state.AuthState
import com.hakif.StoryApp.databinding.ActivityAddStoryBinding
import com.hakif.StoryApp.ui.MainActivity
import com.hakif.StoryApp.ui.home.StoryViewModel
import com.hakif.StoryApp.utils.getImageUri
import com.hakif.StoryApp.utils.reduceFileImage
import com.hakif.StoryApp.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {

    private var _binding: ActivityAddStoryBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null
    private val addStoryViewModel: AddStoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBarPage()
        startCamera()
        startGallery()
        uploadImage()
        cancelPost()
    }

    private fun cancelPost() {
        binding.btnCancel.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun uploadImage() {
        binding.btnPost.setOnClickListener {
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                Log.d("Image File", "showImage: ${imageFile.path}")
                val description = binding.edtDescription.text.toString()
                showLoading(true)
                val requestBody = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,
                    requestImageFile
                )
                lifecycleScope.launch {
                    addStoryViewModel.addStory(requestBody, multipartBody)
                    addStoryViewModel.addStoryState.collect { state ->
                        when (state) {
                            is AuthState.Idle -> showLoading(false)
                            is AuthState.Loading -> showLoading(true)
                            is AuthState.Success -> {
                                showLoading(false)
                                showToast(getString(R.string.upload_success))
                                moveToListStory()
                            }
                            is AuthState.Error -> {
                                showLoading(false)
                                showToast(state.message)
                            }
                        }
                    }
                }
            } ?: showToast(getString(R.string.empty_image_warning))
        }
    }

    private fun moveToListStory() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startCamera() {
        binding.btnCamera.setOnClickListener{
            currentImageUri = getImageUri(this)
            launcherIntentCamera.launch(currentImageUri!!)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun startGallery() {
        binding.btnGallery.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPreview.setImageURI(it)
        }
    }

    private fun setSupportActionBarPage() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val pageTitle = getString(R.string.second_fragment_label)
        supportActionBar?.title = pageTitle
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}