package com.example.musuemguide.userSection.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.musuemguide.R
import com.example.musuemguide.databinding.RobotControlBottomSheetBinding
import com.example.musuemguide.userSection.viewmodels.RobotControlViewModel
import com.example.musuemguide.utils.IS_CONTINUES_LISTEN
import com.example.musuemguide.utils.LanguageManager
import com.example.musuemguide.utils.PERMISSIONS_REQUEST_RECORD_AUDIO
import com.example.musuemguide.utils.RESULTS_LIMIT
import com.example.musuemguide.utils.TextToSpeechUtil
import com.example.musuemguide.utils.errorLog
import com.example.musuemguide.utils.getErrorText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import java.io.File


class RobotControlBottomSheetFragment : BottomSheetDialogFragment(), TextToSpeech.OnInitListener {

    val _viewModel: RobotControlViewModel by inject()

    private var _binding: RobotControlBottomSheetBinding? = null
    private val binding get() = _binding
    var speechRecognizer: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null

    //Text To Speech vars
    private lateinit var ttsHelper: TextToSpeechUtil
    private lateinit var sharedPreferences: SharedPreferences

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            _viewModel.audioFileB = File(requireContext().filesDir, "audio.mp3")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RobotControlBottomSheetBinding.inflate(inflater)

        binding!!.viewModel = _viewModel
        setListeners()
        checkPermissions()
        resetSpeechRecognizer()
        setRecogniserIntent()
        ttsHelper = TextToSpeechUtil(requireContext(), this)

        binding!!.btnGoTo.setOnClickListener {
            _viewModel.setFlag(true)
            //_viewModel.stopAudio()
            this@RobotControlBottomSheetFragment.dismiss()
        }

        binding!!.btnAsk.setOnClickListener {

            binding!!.recorderContainer.visibility = View.VISIBLE
            binding!!.recordedText.visibility = View.VISIBLE
            binding!!.btnAsk.isClickable = false
            if (_viewModel.mediaPlayerA?.isPlaying == true) {
                _viewModel.stopAudioA()
            }
        }



        binding!!.btnPlayAudio.setOnClickListener {
            if (binding!!.btnPlayAudio.tag == resources.getDrawable(R.drawable.btn_pause)) {
                _viewModel.pauseAudioB()
                binding!!.btnPlayAudio.setImageResource(R.drawable.btn_play)
            } else {
                _viewModel.pauseAudioB()
                binding!!.btnPlayAudio.setImageResource(R.drawable.btn_pause)
            }
        }

        binding!!.btnStopAudio.setOnClickListener {
            _viewModel.stopAudioB()
        }


        _viewModel.serverResponse.observe(viewLifecycleOwner) {
            if (it != null) {
                _viewModel.stopAudioB()
                val preferredLanguage = LanguageManager.loadLanguagePreference(requireContext())
                ttsHelper = TextToSpeechUtil(requireContext()) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        _viewModel.audioFileB = File(requireContext().filesDir, "audio.mp3")
                        ttsHelper.convertTextToAudio(
                            it,
                            _viewModel.audioFileB!!,
                            preferredLanguage ?: "en"
                        ) {
                            if (_viewModel.mediaPlayerA?.isPlaying == true) {
                                _viewModel.stopAudioA()
                                _viewModel.playAudioB()
                            } else {
                                _viewModel.playAudioB()
                            }
                        }
                    }
                }
            }

        }
        return binding!!.root
    }


    private fun setListeners() {

        binding!!.startButton.setOnClickListener {
            startListening()
        }
    }

    private fun startListening() {
        speechRecognizer!!.startListening(recognizerIntent)
        binding!!.progressBar1.visibility = View.VISIBLE
    }

    private fun checkPermissions() {
        val permissionCheck =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSIONS_REQUEST_RECORD_AUDIO
            )
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening()
            }
        }
    }

    private fun resetSpeechRecognizer() {
        if (speechRecognizer != null) speechRecognizer!!.destroy()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        errorLog(
            "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(requireContext())
        )


        if (SpeechRecognizer.isRecognitionAvailable(this.requireContext())) {
            //_viewModel.showSnackBarTxt("Recognition Available")
            speechRecognizer!!.setRecognitionListener(mRecognitionListener)
        }
    }

    private val mRecognitionListener = object : RecognitionListener {
        override fun onBeginningOfSpeech() {
            errorLog("onBeginningOfSpeech")
            binding!!.progressBar1.isIndeterminate = false
            binding!!.progressBar1.max = 10
        }

        override fun onBufferReceived(buffer: ByteArray) {
            errorLog("onBufferReceived: $buffer")
        }

        override fun onEndOfSpeech() {
            errorLog("onEndOfSpeech")
            binding!!.progressBar1.isIndeterminate = true
            speechRecognizer!!.stopListening()
        }

        override fun onResults(results: Bundle) {
            errorLog("onResults")

            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val preferredLanguage = LanguageManager.loadLanguagePreference(requireContext())
            if (matches != null && matches.isNotEmpty()) {
                val recognizedText = matches[0]
                _viewModel.sendText(recognizedText, preferredLanguage ?: "en")
                binding!!.recordedText.text = recognizedText
            }
        }

        override fun onError(errorCode: Int) {
            val errorMessage = getErrorText(errorCode)
            errorLog("FAILED $errorMessage")
            binding!!.recordedText.text = errorMessage

            //rest voice recogniser
            resetSpeechRecognizer()
            startListening()
        }

        override fun onEvent(arg0: Int, arg1: Bundle) {
            errorLog("onEvent")
        }

        override fun onPartialResults(arg0: Bundle) {
            errorLog("onPartialResults")
        }

        override fun onReadyForSpeech(arg0: Bundle) {
            errorLog("onReadyForSpeech")
        }

        override fun onRmsChanged(rmsdB: Float) {
            binding!!.progressBar1.progress = rmsdB.toInt()
        }
    }

    private fun setRecogniserIntent() {
        val preferredLanguage = LanguageManager.loadLanguagePreference(requireContext())
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent!!.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
            preferredLanguage
        )
        recognizerIntent!!.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            preferredLanguage
        )
        recognizerIntent!!.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, RESULTS_LIMIT)
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer!!.destroy()

    }

    override fun onResume() {
        errorLog("resume")
        super.onResume()
        resetSpeechRecognizer()
        if (IS_CONTINUES_LISTEN) {
            startListening()
        }
    }

    override fun onPause() {
        errorLog("pause")
        super.onPause()
        speechRecognizer!!.stopListening()
    }

    override fun onStop() {
        errorLog("stop")
        super.onStop()
        if (speechRecognizer != null) {
            speechRecognizer!!.destroy()
        }
    }

    private fun pauseAudio() {
        _viewModel.mediaPlayerA?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    private fun stopAudio() {
        _viewModel.mediaPlayerA?.let {
            if (it.isPlaying) {
                it.stop()
                it.release()
                _viewModel.mediaPlayerA = null
            }
        }
    }

    private fun resumeAudio() {
        _viewModel.mediaPlayerA?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }

}