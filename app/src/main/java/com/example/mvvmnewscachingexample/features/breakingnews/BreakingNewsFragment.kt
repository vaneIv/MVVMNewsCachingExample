package com.example.mvvmnewscachingexample.features.breakingnews

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmnewscachingexample.R
import com.example.mvvmnewscachingexample.databinding.FragmentBreakingNewsBinding
import com.example.mvvmnewscachingexample.shared.NewsArticleListAdapter
import com.example.mvvmnewscachingexample.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private val viewModel: BreakingNewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentBreakingNewsBinding.bind(view)

        val newsArticleAdapter = NewsArticleListAdapter()

        binding.apply {
            recyclerViewBreakingNews.apply {
                adapter = newsArticleAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.breakingNews.collect {
                    val result = it ?: return@collect

                    swipeRefreshLayoutBreakingNews.isRefreshing = result is Resource.Loading
                    recyclerViewBreakingNews.isVisible = !result.data.isNullOrEmpty()
                    textViewError.isVisible = result.error != null && result.data.isNullOrEmpty()
                    buttonRetry.isVisible = result.error != null && result.data.isNullOrEmpty()
                    textViewError.text = getString(
                        R.string.could_not_refresh,
                        result.error?.localizedMessage ?: getString(R.string.unknown_error_occurred)
                    )

                    newsArticleAdapter.submitList(result.data)
                }
            }
        }
    }
}
