package com.example.musuemguide.userSection.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.musuemguide.R
import com.example.musuemguide.databinding.FragmentHome2Binding


class HomeFragment2 : Fragment() {

    private var _binding: FragmentHome2Binding? = null
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHome2Binding.inflate(inflater)

        /*binding!!.btnRobotControl.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToRobotControlFragment())
        }*/

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding!!.toolbar)

        val toggle = ActionBarDrawerToggle(
            requireActivity(), binding!!.drawerLayout, binding!!.toolbar,
            R.string.open, R.string.close
        )
        binding!!.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding!!.navView.bringToFront()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            handleBackPress()
        }


        /*binding!!.fab.setOnClickListener {
            //val navController = findNavController(R.id.fragmentContainerView)
            findNavController().navigate(R.id.action_homeFragment_to_robotControlFragment)
        }*/

        binding!!.toolbar.setNavigationIcon(R.drawable.ic_menu)

        binding!!.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.hatshepsut -> {
                    binding!!.textHeader.text = getString(R.string.amenhotep_i)
                    binding!!.detailsBody.text = getString(R.string.hatshepsut_history_text)
                    binding!!.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nefertiti -> {
                    binding!!.textHeader.text = getString(R.string.nefertiti)
                    binding!!.detailsBody.text = getString(R.string.akhenaten_history)
                    binding!!.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.tutankhamun -> {
                    binding!!.textHeader.text = getString(R.string.akhenaten)
                    binding!!.detailsBody.text = getString(R.string.thuthmose_iii_history)
                    binding!!.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                else -> false
            }
        }


        // Inflate the layout for this fragment
        return binding!!.root
    }

    private fun handleBackPress() {
        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            requireActivity().onBackPressedDispatcher
        }
    }
}