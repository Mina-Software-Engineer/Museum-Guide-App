package com.example.musuemguide.utils

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import java.io.File
import java.util.Locale

class TextToSpeechUtil(
    context: Context,
    private val listener: OnInitListener
) : OnInitListener {

    private val tts: TextToSpeech = TextToSpeech(context, this)
    private var isInitialized = false

    /*init {
        tts.language = Locale.US
    }
*/
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            listener.onInit(status)
        } else {
            listener.onInit(status)
        }
    }

    fun convertTextToAudio(text: String, outputFile: File, language: String, onCompletion: () -> Unit) {
        val local = Locale(language)
        val result = tts.setLanguage(local)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Handle the error case
            return
        }
        if (isInitialized) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val utteranceId = "TextToAudio"
                tts.synthesizeToFile(text, null, outputFile, utteranceId)
                tts.setOnUtteranceProgressListener(object :
                    android.speech.tts.UtteranceProgressListener() {
                    override fun onStart(utteranceId: String) {}

                    override fun onDone(utteranceId: String) {
                        onCompletion()
                    }

                    override fun onError(utteranceId: String) {}
                })
            }
        }
    }

    fun shutDown() {
        tts.stop()
        tts.shutdown()
    }
}


/*(private val context: Context,
                       private val outputFilePath: String,
                       private val onInitComplete: (Boolean) -> Unit
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech = TextToSpeech(context, this)


    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
            onInitComplete(true)
        } else {
           onInitComplete(false)
        }
    }



        tts.synthesizeToFile(text, params, File(outputFilePath), "tts1")
    }

    fun convertTextToPcm(text: String, callback: (Boolean, ByteArray?) -> Unit) {
        val tempFile = File("${File(outputFilePath).parent}/tts_temp.wav")
        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "tts1")
        }

        tts.synthesizeToFile(text, params, tempFile, "tts1")

        tts.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
            override fun onStart(utteranceId: String) {
                // No implementation needed
            }

            override fun onDone(utteranceId: String) {
                // Convert the temporary WAV file to PCM format
                val pcmBytes = convertWavToPcm(tempFile)
                pcmBytes.forEach {
                    Log.d("TtsUtil", "PCM Byte Array: $it")
                }
                Log.d("TtsUtil", "PCM Byte Array: ${pcmBytes.joinToString()}}")
                callback(true, pcmBytes)
            }

            override fun onError(utteranceId: String) {
                callback(false, null)
            }
        })
    }

    private fun convertWavToPcm(inputFile: File): ByteArray {
        try {
            val wavBytes = inputFile.readBytes()
            return extractPcmData(wavBytes)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ByteArray(0)
    }

    private fun extractPcmData(wavData: ByteArray): ByteArray {
        val headerSize = 44 // WAV header is typically 44 bytes
        return wavData.copyOfRange(headerSize, wavData.size)
    }

    fun shutdown() {
        tts.shutdown()
    }
}*/