package com.example.musuemguide.userSection.ui

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.musuemguide.userSection.viewmodels.BaseViewModel
import com.example.musuemguide.utils.NavigationCommand
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {
    /**
     * Every fragment has to have an instance of a view model that extends from the BaseViewModel
     */

    abstract val _viewModel: BaseViewModel
    abstract fun startAnimation()

    override fun onStart() {
        super.onStart()

        _viewModel.navigationCommand.observe(this) { command ->
            when (command) {
                is NavigationCommand.To -> findNavController().navigate(command.directions)
                is NavigationCommand.Back -> findNavController().popBackStack()
                is NavigationCommand.BackTo -> findNavController().popBackStack(
                    command.destinationId,
                    false
                )
            }
        }

        _viewModel.showSnackBar.observe(this) {
            Snackbar.make(this.requireView(), it, Snackbar.LENGTH_LONG).show()
        }


    }


}