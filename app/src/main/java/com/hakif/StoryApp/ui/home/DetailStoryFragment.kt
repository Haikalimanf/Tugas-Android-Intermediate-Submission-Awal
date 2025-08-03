package com.hakif.StoryApp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hakif.StoryApp.databinding.FragmentDetailStoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailStoryFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDetailStory()
        closeDetailStory()
    }

    private fun closeDetailStory() {
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun showDetailStory() {
        val name = arguments?.getString(EXTRA_NAME)
        val description = arguments?.getString(EXTRA_DESCRIPTION)
        val photoUrl = arguments?.getString(EXTRA_PHOTO_URL)

        binding.tvUsername.text = name
        binding.tvDescription.text = description
        Glide.with(requireContext())
            .load(photoUrl)
            .into(binding.imgStory)
    }


    companion object {
        const val TAG = "DetailStoryFragment"
        const val EXTRA_NAME = "name"
        const val EXTRA_DESCRIPTION = "description"
        const val EXTRA_PHOTO_URL = "photoUrl"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}