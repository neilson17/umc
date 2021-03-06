package com.ubaya.s160419037_umc.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.s160419037_umc.R
import com.ubaya.s160419037_umc.viewmodel.NewsListViewModel
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment() {
    private lateinit var viewModel: NewsListViewModel
    private val newsListAdapter = NewsListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(NewsListViewModel::class.java)
        viewModel.refresh()

        recViewNews.layoutManager = LinearLayoutManager(context)
        recViewNews.adapter = newsListAdapter

        observeViewModel()

        newsRefreshLayout.setOnRefreshListener {
            recViewNews.visibility = View.GONE
            textErrorNews.visibility = View.GONE
            progressLoadNews.visibility = View.VISIBLE
            viewModel.refresh()
            newsRefreshLayout.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewModel.newsLiveData.observe(viewLifecycleOwner){
            newsListAdapter.updateNewsList(it)
        }
        viewModel.newsLoadErrorLiveData.observe(viewLifecycleOwner){
            textErrorNews.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.loadingLiveData.observe(viewLifecycleOwner){
            if (it){
                progressLoadNews.visibility = View.VISIBLE
                recViewNews.visibility = View.GONE
            }
            else {
                progressLoadNews.visibility = View.GONE
                recViewNews.visibility = View.VISIBLE
            }
        }
    }
}