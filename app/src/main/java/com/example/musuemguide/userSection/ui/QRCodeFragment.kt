package com.example.musuemguide.userSection.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.example.musuemguide.R
import com.example.musuemguide.databinding.QrCodeScannerBinding

class QRCodeFragment : Fragment() {

    private var _binding: QrCodeScannerBinding? = null
    private val binding get() = _binding
    private lateinit var codeScanner: CodeScanner
    private val qrcodeString = "https://uqr.to/1ptcv"

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = QrCodeScannerBinding.inflate(inflater)

        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.viewScanner)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback { result ->
            activity.runOnUiThread {
                if (result.text == qrcodeString) {
                    try {
                        findNavController().navigate(R.id.action_authFragment_to_robotControlFragment)
                    }catch (e: Exception) {
                        Log.d("checkkk", e.message.toString())
                    }
                    //Toast.makeText(activity, result.text, Toast.LENGTH_LONG).show()
                } else {
                    Log.d("checkkk", result.text)
                }
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }
}