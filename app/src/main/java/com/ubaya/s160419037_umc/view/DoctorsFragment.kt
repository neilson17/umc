package com.ubaya.s160419037_umc.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.s160419037_umc.R
import com.ubaya.s160419037_umc.viewmodel.DoctorListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_doctors.*

class DoctorsFragment : Fragment() {
    private lateinit var viewModel: DoctorListViewModel
    private val doctorListAdapter = DoctorListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).bottomNav.visibility = View.GONE

        viewModel = ViewModelProvider(this).get(DoctorListViewModel::class.java)
        viewModel.refresh()

        recViewDoctors.layoutManager = LinearLayoutManager(context)
        recViewDoctors.adapter = doctorListAdapter

        observeViewModel()

        doctorsRefreshLayout.setOnRefreshListener {
            recViewDoctors.visibility = View.GONE
            textErrorDoctors.visibility = View.GONE
            progressLoadDoctors.visibility = View.VISIBLE
            viewModel.refresh()
            doctorsRefreshLayout.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewModel.doctorsLiveData.observe(viewLifecycleOwner){
            doctorListAdapter.updateDoctorList(it)
        }
        viewModel.doctorsLoadErrorLiveData.observe(viewLifecycleOwner){
            textErrorDoctors.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.loadingLiveData.observe(viewLifecycleOwner){
            if (it){
                progressLoadDoctors.visibility = View.VISIBLE
                recViewDoctors.visibility = View.GONE
            }
            else {
                progressLoadDoctors.visibility = View.GONE
                recViewDoctors.visibility = View.VISIBLE
            }
        }
    }
}