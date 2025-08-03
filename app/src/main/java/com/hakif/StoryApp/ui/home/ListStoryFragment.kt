package com.hakif.StoryApp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hakif.StoryApp.data.state.AuthState
import com.hakif.StoryApp.databinding.FragmentListStoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListStoryFragment : Fragment() {

    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding!!
    private val storyViewModel: StoryViewModel by viewModels()
    private lateinit var adapter: StoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showStory()
    }

    private fun showStory() {
        adapter = StoryAdapter { storyItem ->
            val bottomSheet = DetailStoryFragment()
            val bundle = Bundle().apply {
                putString(DetailStoryFragment.EXTRA_NAME, storyItem.name)
                putString(DetailStoryFragment.EXTRA_DESCRIPTION, storyItem.description)
                putString(DetailStoryFragment.EXTRA_PHOTO_URL, storyItem.photoUrl)
            }
            bottomSheet.arguments = bundle
            bottomSheet.show(parentFragmentManager, DetailStoryFragment.TAG)
        }
        binding.apply {
            rvStory.adapter = adapter
            rvStory.setHasFixedSize(true)
            rvStory.layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            storyViewModel.getStoryState.collectLatest { state ->
                when (state) {
                    is AuthState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is AuthState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.submitList(state.data.listStory)
                    }
                    is AuthState.Error -> {
                        Log.d("showStory", "showStory: ${state.message}")
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }

                    AuthState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}