package com.example.mvvm_tests.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm_tests.DatabaseVideoViewAdapter
import com.example.mvvm_tests.NetworkVideoViewAdapter
import com.example.mvvm_tests.R
import com.example.mvvm_tests.databinding.FragmentMainBinding
import com.example.mvvm_tests.dialogFragmnet.ErrorDialogFragmnet

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
       binding =  DataBindingUtil.inflate<FragmentMainBinding>(inflater,R.layout.fragment_main,container,false)
       binding.vm = viewModel
       return binding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.SetDao(context?.let { VideosDatabase.getDatabase(it).videoDao })

        binding.recyclerviewNetWork.visibility =View.VISIBLE
        binding.recyclerviewDatabase.visibility = View.INVISIBLE

        binding.NetworkButton.setOnClickListener {
            binding.recyclerviewNetWork.visibility =View.VISIBLE
            binding.recyclerviewDatabase.visibility = View.INVISIBLE
            binding.ViewTitle.text = "Network Videos"
        }
        binding.DatabaseButton.setOnClickListener {
            updatedatabase()
            binding.recyclerviewNetWork.visibility =View.INVISIBLE
            binding.recyclerviewDatabase.visibility = View.VISIBLE
            binding.ViewTitle.text = "Database Videos"
            Log.i("Game","Lost")
        }

        binding.TransferButton.setOnClickListener {
            viewModel.playlist.observe(viewLifecycleOwner) { Videos->
                viewModel.addtoDataBase(Videos)
            }
        }
        binding.recyclerviewNetWork.layoutManager = LinearLayoutManager(context)
        val adapter = NetworkVideoViewAdapter()
        adapter.GetParantManager(this.childFragmentManager)
        binding.recyclerviewNetWork.adapter = adapter
        viewModel.playlist.observe(viewLifecycleOwner) { Videos->
            adapter.SetData(Videos)
        }
        viewModel.eventNetworkError.observe(viewLifecycleOwner) {

            if (it){
                ErrorDialogFragmnet("Network Error").show(childFragmentManager,"")
            }

        }

    }

fun updatedatabase(){
    binding.recyclerviewDatabase.layoutManager = LinearLayoutManager(context)
    val DatabaseAdapter = DatabaseVideoViewAdapter()
    DatabaseAdapter.GetParantManager(this.childFragmentManager)
    binding.recyclerviewDatabase.adapter = DatabaseAdapter
    viewModel.databasePlaylist.observe(this.viewLifecycleOwner){
        if (it != null) {
            DatabaseAdapter.SetData(it)
        }else{
            ErrorDialogFragmnet("No data at database").show(childFragmentManager,"")
        }
    }

}

}