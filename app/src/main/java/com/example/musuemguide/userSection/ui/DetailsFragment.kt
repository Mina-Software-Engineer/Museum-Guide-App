package com.example.musuemguide.userSection.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musuemguide.databinding.FragmentDetailsBinding
import com.example.musuemguide.userSection.viewmodels.DetailsViewModel
import com.example.musuemguide.utils.BitmapConverter
import org.koin.android.ext.android.inject

class DetailsFragment : BaseFragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding
    override val _viewModel: DetailsViewModel by inject()

    override fun startAnimation() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater)
        // Inflate the layout for this fragment
        val artifactItem = DetailsFragmentArgs.fromBundle(requireArguments()).artifactModel

        binding!!.artifactIv.setImageBitmap(BitmapConverter.stringToBitmap(artifactItem.image))
        binding!!.artifactNameTv.text = artifactItem.title
        binding!!.artifactDescriptionTv.text = artifactItem.details

        return binding!!.root
    }
}