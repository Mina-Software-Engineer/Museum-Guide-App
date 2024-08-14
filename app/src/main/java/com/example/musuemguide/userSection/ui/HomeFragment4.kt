/*package com.example.musuemguide.userSection.ui


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.musuemguide.R
import com.example.musuemguide.databinding.FragmentHome4Binding
import com.example.musuemguide.utils.ArtifactSelectIconClickListener
import org.koin.android.ext.android.inject

class HomeFragment4 : BaseFragment() {
    private var _binding: FragmentHome4Binding? = null
    private val binding get() = _binding

    private var backdropShown = true
    private val animatorSet = AnimatorSet()
    override val _viewModel: HomeViewModel4 by inject()


    //override val _viewModel: HomeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHome4Binding.inflate(inflater)

        setHasOptionsMenu(true)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding!!.appBar)
        binding!!.appBar.setNavigationOnClickListener(
            ArtifactSelectIconClickListener(
                requireActivity(),
                binding!!.scrollviewContainer,
                AccelerateDecelerateInterpolator(),
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_menu), // Menu open icon
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_close_menu
                ) // Menu close icon
            )
        )

        binding!!.appBar.title = resources.getText(R.string.app_name)
        binding!!.textHeader.text = resources.getText(R.string.nefertiti)
        binding!!.detailsBody.text = resources.getText(R.string.nefertiti_text)

binding!!.fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_robotControlFragment)
        }


        binding!!.dropdownList.btnListNefertiti.setOnClickListener {
            binding!!.imageContainer.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.nefertiti_img
                )
            )
            binding!!.textHeader.text = resources.getText(R.string.nefertiti)
            binding!!.detailsBody.text = resources.getText(R.string.nefertiti_text)

            handleListAnimation()
        }

        binding!!.dropdownList.btnListHatshepsut.setOnClickListener {
            binding!!.imageContainer.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.hatshepsut_img
                )
            )
            binding!!.textHeader.text = resources.getText(R.string.hatshepsut)
            binding!!.detailsBody.text = resources.getText(R.string.hatshepsut_history_text)

            handleListAnimation()
        }

        binding!!.dropdownList.btnListTutankhamun.setOnClickListener {
            binding!!.imageContainer.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.tutankhamun_img
                )
            )
            binding!!.textHeader.text = resources.getText(R.string.tutankhamun)
            binding!!.detailsBody.text = resources.getText(R.string.tutankhamun_text)

            handleListAnimation()
        }

        binding!!.dropdownList.btnListNamrer.setOnClickListener {
            binding!!.imageContainer.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.narmer_img
                )
            )
            binding!!.textHeader.text = resources.getText(R.string.narmer)
            binding!!.detailsBody.text = getString(R.string.narmer_text)

            handleListAnimation()
        }

        binding!!.dropdownList.btnListHatthor.setOnClickListener {
            binding!!.imageContainer.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.hatthor_img
                )
            )
            binding!!.textHeader.text = resources.getText(R.string.hatthor)
            binding!!.detailsBody.text = resources.getText(R.string.hatthor_text)

            handleListAnimation()
        }

        binding!!.btnSettings.setOnClickListener {
            //findNavController().navigate(R.id.action_homeFragment1_to_settingsFragment)
        }

        //_viewModel.getCurrentLanguage()
_viewModel.langCode.observe(viewLifecycleOwner) {
            _viewModel.setLanguage(it)
        }


        return binding!!.root
    }


    override fun startAnimation() {
        val cardViewAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.btn_connect_anim)

        binding!!.detailsBody.startAnimation(cardViewAnim)
    }

    private fun handleListAnimation() {
        backdropShown = !backdropShown

        // Cancel the existing animations
        animatorSet.removeAllListeners()
        animatorSet.end()
        animatorSet.cancel()

        val animator = ObjectAnimator.ofFloat(
            binding!!.scrollviewContainer,
            "translationY",
            (if (backdropShown) 0 else 0).toFloat()
        )
        animator.duration = 500

        animator.interpolator = AccelerateDecelerateInterpolator()

        animatorSet.play(animator)
        animator.start()

        updateIcon()

    }


    private fun updateIcon() {

        if (binding!!.appBar.navigationIcon == ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_menu
            )
        ) {
            binding!!.appBar.navigationIcon =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_close_menu)
        } else {
            binding!!.appBar.navigationIcon =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_menu)
        }

    }


}
*/
