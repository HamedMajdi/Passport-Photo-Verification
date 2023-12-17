package com.example.passportphotocomparisonthesis.NFCAndChipAuthentication

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.databinding.FragmentNFCVerificationBinding
import com.google.android.material.snackbar.Snackbar

class NFCVerificationFragment : DialogFragment() {

    private lateinit var binding: FragmentNFCVerificationBinding
    private val args: NFCVerificationFragmentArgs by navArgs()
    private lateinit var tipsString: Array<String>
    private var tipsStringsIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNFCVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDocumentNumber.text = args.documentID
        binding.tvBirthDate.text = args.birthDate
        binding.tvExpirationDate.text = args.expirationDate
        animateColor(binding.tvCaution, 2000)

        tipsString = arrayOf(getString(R.string.nfc_read_tip1),
            getString(R.string.nfc_read_tip2), getString(R.string.nfc_read_tip3))

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                animateText(binding.tvTips, tipsString[tipsStringsIndex])
                tipsStringsIndex = (tipsStringsIndex + 1) % tipsString.size
                handler.postDelayed(this, 5000) // changes every 3 seconds
            }
        })
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = (resources.displayMetrics.widthPixels * 0.85).toInt() // 85% of screen width
            val height =
                (resources.displayMetrics.heightPixels * 0.85).toInt() // 85% of screen height
            dialog.window?.setLayout(width, height)
        }
    }

    private fun animateColor(textView: TextView, duration: Long) {
        val colorFrom = textView.currentTextColor
        val colorTo = Color.parseColor("#450101")
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo, colorFrom)
        colorAnimation.duration = duration
        colorAnimation.repeatCount = ValueAnimator.INFINITE
        colorAnimation.addUpdateListener { animator ->
            textView.setTextColor(animator.animatedValue as Int)
        }
        colorAnimation.start()
    }

    private fun animateText(textView: TextView, textToShow: String) {
        textView.animate()
            .alpha(0f)
            .setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    // Change the text when the animation ends
                    textView.text = textToShow
                    // Fade in animation
                    textView.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .setListener(null)
                }
            })
    }




}