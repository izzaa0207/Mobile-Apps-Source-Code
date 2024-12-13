package com.capstone.diabesafe.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.diabesafe.DetailActivity
import com.capstone.diabesafe.adapter.NewsPagingAdapter
import com.capstone.diabesafe.api.retrofit.NewsApiConfig
import com.capstone.diabesafe.data.NewsRepository
import com.capstone.diabesafe.databinding.FragmentHomeBinding
import java.io.Serializable

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: NewsPagingAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root // Safe access to binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val query = "health" // Sesuaikan query dan category
        val category = "health"
        val factory = HomeViewModelFactory(NewsRepository(NewsApiConfig.getApiService()), query, category)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setupRecyclerView()
        observeData()
        handleBackPress()
    }

    private fun setupRecyclerView() {
        adapter = NewsPagingAdapter { article ->
            // handle
        }
        binding?.recyclerViewNews?.layoutManager = LinearLayoutManager(requireContext())
        binding?.recyclerViewNews?.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            binding?.progressBar?.isVisible = loadState.source.refresh is LoadState.Loading
            binding?.recyclerViewNews?.isVisible = loadState.source.refresh is LoadState.NotLoading
        }
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        )
    }

    private fun observeData() {
        viewModel.articles.observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Make sure binding is null when view is destroyed
    }
}
