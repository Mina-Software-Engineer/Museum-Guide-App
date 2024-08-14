package com.example.musuemguide.userSection.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.musuemguide.R
import com.example.musuemguide.databinding.FragmentRobotControlBinding
import com.example.musuemguide.userSection.viewmodels.RobotControlViewModel
import com.example.musuemguide.utils.LanguageManager
import com.example.musuemguide.utils.TextToSpeechUtil
import org.koin.android.ext.android.inject
import java.io.File


class RobotControlFragment : BaseFragment() {

    private var _binding: FragmentRobotControlBinding? = null
    private val binding get() = _binding

    lateinit var ttsHelper: TextToSpeechUtil

    override val _viewModel: RobotControlViewModel by inject()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Register for activity result
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.entries.all { it.value }
            if (allGranted) {
                _viewModel.permissionGranted.value = true
                _viewModel.setupBluetooth()
            } else {
                // Handle permission denied case
                // You might want to show a message to the user indicating why the permissions are necessary
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRobotControlBinding.inflate(inflater)
        // Inflate the layout for this fragment

        startAnimation()
        _viewModel.isButtonClickable(true)
        _viewModel.showLoading(true)

        binding!!.viewModel = _viewModel


        _viewModel.btnVisibility.observe(viewLifecycleOwner) {
                binding!!.btnStartTour.visibility = it
                binding!!.btnPlayAudio.visibility = it
        }

        _viewModel.mediaFlag.observe(viewLifecycleOwner) {  isConfirmed ->
            if (isConfirmed) {
                if(_viewModel.mediaPlayerB?.isPlaying == true || _viewModel.mediaPlayerA?.isPlaying == true){
                    _viewModel.stopAudioB()
                    _viewModel.stopAudioA()
                }
                _viewModel.startTour(_viewModel.pos.toString())
                _viewModel.setFlag(false)
            }
        }

        /*_viewModel.showBottomSheet.observe(viewLifecycleOwner) {
            if (it) {
                showDialog()
                _viewModel.setButtonSheetBool(false)
            }
        }*/

        _viewModel.receivedMessage.observe(viewLifecycleOwner, Observer { message ->
            //_viewModel.showSnackBarTxt(message)
            _viewModel.showSnackBarTxt(message)
            Log.d("kjasbdkjassd", "bt receive")
            when (message){

                /** 3.Receiving position confirmation from robot
                 *  4.Make Speech*/

                /**
                 * 3  ----  4
                 * 2  ----  5
                 * 1  ----  6
                 * */
                "1" -> {
                    _viewModel.makeSpeech(getString(R.string.welcome_message) + getString(R.string.akhenaten_history))
                }
                "2" -> {
                    _viewModel.makeSpeech(getString(R.string.thuthmose_iii_history))
                }
                "3" -> {
                    _viewModel.makeSpeech(getString(R.string.amenhotep_i_history))
                }
                "4" -> {
                    _viewModel.makeSpeech(getString(R.string.thuthmose_i_history))
                }
                "5" -> {
                    _viewModel.makeSpeech(getString(R.string.nefertiti_history))
                }
                "6" -> {
                    _viewModel.makeSpeech(getString(R.string.amenhotep_ii_history))
                }
            }
        })

        _viewModel.welcomeMsg.observe(viewLifecycleOwner) {
            if (it != null) {
                val preferredLanguage = LanguageManager.loadLanguagePreference(requireContext())
                ttsHelper = TextToSpeechUtil(requireContext()) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        _viewModel.audioFileA = File(requireContext().filesDir, "audio.mp3")
                        ttsHelper.convertTextToAudio(it, _viewModel.audioFileA!!, preferredLanguage?: "en") {
                            if(_viewModel.mediaPlayerA?.isPlaying == true){
                                _viewModel.stopAudioA()
                                _viewModel.playAudioA()
                            }else{
                                _viewModel.playAudioA()
                            }
                        }
                    }
                }
            }
        }

        _viewModel.buttonClickable.observe(viewLifecycleOwner) {
            binding!!.btnBluetoothConnect.isClickable = it
        }


        _viewModel.robotLocation.observe(viewLifecycleOwner) { location ->

            if (location != null) {
                when (location) {
                    "1" -> {
                        /** 2.Sending location to robot */
                        _viewModel.sendPosition(location)
                        binding!!.robotControlFragmentBackground.setBackgroundResource(R.drawable.bk1) //btn1
                    }

                    "2" -> {
                        _viewModel.sendPosition(location)
                        binding!!.robotControlFragmentBackground.setBackgroundResource(R.drawable.bk2) //btn2
                    }

                    "3" -> {
                        _viewModel.sendPosition(location)
                        binding!!.robotControlFragmentBackground.setBackgroundResource(R.drawable.bk3) //btn3
                    }

                    "4" -> {
                        _viewModel.sendPosition(location)
                        binding!!.robotControlFragmentBackground.setBackgroundResource(R.drawable.bk4) //btn4
                    }

                    "5" -> {
                        _viewModel.sendPosition(location)
                        binding!!.robotControlFragmentBackground.setBackgroundResource(R.drawable.bk5) //btn5
                    }

                    "6" -> {
                        _viewModel.sendPosition(location)
                        binding!!.robotControlFragmentBackground.setBackgroundResource(R.drawable.bk6) //btn6
                    }
                }
            }

        }

        _viewModel.btnText.observe(viewLifecycleOwner) {
            if (it != null) {
                binding!!.btnBluetoothConnect.text = it
            }
        }

        //btn 3
        binding!!.btnNum1.setOnClickListener {
            showBottomSheet()
            _viewModel.pos = 3
            _viewModel.btnTextChange("Go To Amenhotep")

            //_viewModel.sendPosition("1")
            binding!!.robotControlFragmentBackground.setBackgroundResource(R.drawable.bk_btn_1)
        }

        //btn 2
        binding!!.btnNum2.setOnClickListener {
            _viewModel.pos = 2
            _viewModel.btnTextChange("Go To Thuthmose III")
            showBottomSheet()
        }

        //btn 1
        binding!!.btnNum3.setOnClickListener {
            _viewModel.pos = 1
            showBottomSheet()
            //_viewModel.sendPosition("3")
            //binding!!.robotControlFragmentBackground.setBackgroundResource(R.drawable.bk_btn_3)
        }

        //btn 4
        binding!!.btnNum4.setOnClickListener {
            _viewModel.pos = 4
            _viewModel.btnTextChange("Go To Thutmose I")
            showBottomSheet()
            //_viewModel.sendPosition("4")

            //binding!!.robotControlFragmentBackground.setBackgroundResource(R.drawable.bk_btn_4)
        }

        //btn 5
        binding!!.btnNum5.setOnClickListener {
            _viewModel.pos = 5
            _viewModel.btnTextChange("Go To Nefertiti")
            showBottomSheet()
            //_viewModel.sendPosition("5")
            //binding!!.robotControlFragmentBackground.setBackgroundResource(R.drawable.bk_btn_5)
        }

        //btn 6
        binding!!.btnNum6.setOnClickListener {
            _viewModel.pos = 6
            _viewModel.btnTextChange("Go To Amenhotep II")
            showBottomSheet()
            //_viewModel.sendPosition("6")

            //binding!!.robotControlFragmentBackground.setBackgroundResource(R.drawable.bk_btn_6)
        }

        binding!!.btnPlayAudio.setOnClickListener {
            /*if (binding!!.btnPlayAudio.tag == resources.getDrawable(R.drawable.btn_pause)) {
                Log.d("checkBTN", "Stop pressed")
                resumeAudio()
                binding!!.btnPlayAudio.setImageResource(R.drawable.btn_play)
            }else{
                Log.d("checkBTN", "Play pressed")
                //stopAudio()
                pauseAudio()
                binding!!.btnPlayAudio.setImageResource(R.drawable.btn_pause)
            }*/

            if (_viewModel.mediaPlayerA?.isPlaying == true) {
                Log.d("checkBTN", "Stop pressed")
                _viewModel.pauseAudioA()
                binding!!.btnPlayAudio.setImageResource(R.drawable.btn_play)
            } else {
                Log.d("checkBTN", "Play pressed")
                _viewModel.resumeAudioA()
                binding!!.btnPlayAudio.setImageResource(R.drawable.btn_pause)
            }
        }




        binding!!.btnBluetoothConnect.setOnClickListener {

            checkBluetoothPermission()
            /*if (binding!!.btnBluetoothConnect.text == getString(R.string.connect)) {
                _viewModel.showLoading(true)

                checkBluetoothPermission()

            } else {
                *//**1. Setting robot position to 1 *//*

                //_viewModel.makeSpeech("Hello, I am tour guide robot. I am ready to help you today!")
                //_viewModel.receiveFromBluetooth()
            }*/
        }

        binding!!.btnStartTour.setOnClickListener {
            _viewModel.startTour("1")
        }
        return binding!!.root
    }


    private fun checkBluetoothPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT)
            == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN)
            == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {

            _viewModel.permissionGranted.value = true
            _viewModel.setupBluetooth()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }


    /*private fun playAudio() {
        if (_viewModel.audioFile != null && !_viewModel.audioFile!!.exists()) {
            return
        }
        _viewModel.mediaPlayer = MediaPlayer().apply {
            setDataSource(_viewModel.audioFile!!.absolutePath)
            prepare()
            start()
        }
    }*/

    override fun startAnimation() {

        val leftRightBtnAnim =
            AnimationUtils.loadAnimation(requireContext(), R.anim.left_right_btn_anim)
        val rightLeftBtnAnim =
            AnimationUtils.loadAnimation(requireContext(), R.anim.right_left_btn_anim)
        val downUpBtnConnect =
            AnimationUtils.loadAnimation(requireContext(), R.anim.btn_connect_anim)

        binding!!.btnNum1.startAnimation(leftRightBtnAnim)
        binding!!.btnNum2.startAnimation(leftRightBtnAnim)
        binding!!.btnNum3.startAnimation(leftRightBtnAnim)
        binding!!.btnNum4.startAnimation(rightLeftBtnAnim)
        binding!!.btnNum5.startAnimation(rightLeftBtnAnim)
        binding!!.btnNum6.startAnimation(rightLeftBtnAnim)

        binding!!.txtNum1.startAnimation(leftRightBtnAnim)
        binding!!.txtNum2.startAnimation(leftRightBtnAnim)
        binding!!.txtNum3.startAnimation(leftRightBtnAnim)
        binding!!.txtNum4.startAnimation(rightLeftBtnAnim)
        binding!!.txtNum5.startAnimation(rightLeftBtnAnim)
        binding!!.txtNum6.startAnimation(rightLeftBtnAnim)

        binding!!.btnBluetoothConnect.startAnimation(downUpBtnConnect)

    }

    /*private fun showDialog() {
        // Create an AlertDialog builder
        val builder = android.app.AlertDialog.Builder(requireContext().applicationContext)
        builder.setTitle("Any Question")
        builder.setMessage("Do you have any question?")

        // Add a positive button
        builder.setPositiveButton("Yes") { dialog, which ->
            // Handle the positive button click
            showBottomSheet()
        }

        // Add a negative button
        builder.setNegativeButton("No") { dialog, which ->
            // Handle the negative button click
            _viewModel.onAudioPlayCompleted()
        }

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }*/

    private fun showBottomSheet() {
        val dialog = RobotControlBottomSheetFragment()
        dialog.show(requireActivity().supportFragmentManager, "RobotControlBottomSheetFragment")
    }

}