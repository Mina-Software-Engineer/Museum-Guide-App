package com.example.musuemguide.userSection.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musuemguide.databinding.FragmentHomeBinding
import com.example.musuemguide.userSection.viewmodels.HomeViewModel
import com.example.musuemguide.utils.HomeAdapter
import com.example.musuemguide.utils.HomeItemClickListener
import com.example.musuemguide.utils.NavigationCommand
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    override val _viewModel: HomeViewModel by inject()
    override fun startAnimation() {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)

        binding!!.homeFeedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        GlobalScope.launch {
            _viewModel.getAllArtifacts()
        }

        val adapter = HomeAdapter(HomeItemClickListener { artifactModel ->
            _viewModel.onArtifactItemClicked(artifactModel)
        })
        binding!!.homeFeedRecyclerView.adapter = adapter

        _viewModel.navigationCommand.observe(viewLifecycleOwner) { command ->
            when (command) {
                is NavigationCommand.To -> {
                    findNavController().navigate(command.directions)
                }

                else -> {}
            }
        }

        _viewModel.artifact.observe(viewLifecycleOwner) { artifactList ->
            artifactList?.let {
                adapter.addList(it)
            }
        }
        return binding!!.root
    }
}
