package com.example.passportphotocomparisonthesis.NFCAndChipAuthentication.Veiw


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.finalnfcpassport.PassportNFCViewModel
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.Utils.DateParser
import com.example.passportphotocomparisonthesis.databinding.FragmentNFCVerificationNonDialogBinding
import org.jmrtd.BACKey
import org.jmrtd.BACKeySpec


class NFCVerificationFragment : Fragment() {

//    private lateinit var binding: FragmentNFCVerificationBinding
    private lateinit var binding: FragmentNFCVerificationNonDialogBinding
    private val args: com.example.passportphotocomparisonthesis.NFCAndChipAuthentication.Veiw.NFCVerificationFragmentArgs by navArgs()
    private lateinit var tipsString: Array<String>
    private var tipsStringsIndex = 0
    private lateinit var viewModel: PassportNFCViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        binding = FragmentNFCVerificationBinding.inflate(inflater, container, false)
        binding = FragmentNFCVerificationNonDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDocumentNumber.text = args.documentID
        binding.tvBirthDate.text = args.birthDate
        binding.tvExpirationDate.text = args.expirationDate
        animateColor(binding.tvCaution, 2000)

        tipsString = arrayOf(
            getString(R.string.nfc_read_tip1),
            getString(R.string.nfc_read_tip2),
            getString(R.string.nfc_read_tip3)
        )

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                animateText(binding.tvTips, tipsString[tipsStringsIndex])
                tipsStringsIndex = (tipsStringsIndex + 1) % tipsString.size
                handler.postDelayed(this, 5000) // changes every 3 seconds
            }
        })

        viewModel = ViewModelProvider(this)[PassportNFCViewModel::class.java]
        viewModel._passportData.observe(viewLifecycleOwner) { data ->

            viewModel.updateUserWithPassportData(data)

        }

        viewModel._isLoading.observe(viewLifecycleOwner) { data ->
            if (data) {
                changeAnimation(R.raw.card_loading)
                binding.tvTips.visibility = View.GONE


            }
        }

        viewModel._isSuccessful.observe(viewLifecycleOwner) { data ->
            if (data) {
                changeAnimation(R.raw.card_success)
                binding.animationView.loop(false)
                binding.tvTips.visibility = View.GONE

            } else {
                changeAnimation(R.raw.card_failed)
                binding.animationView.loop(false)
                binding.tvTips.visibility = View.GONE
            }
        }

        viewModel._error.observe(viewLifecycleOwner) { data ->
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
        }

    }


    private fun changeAnimation(drawableID: Int) {
        // Create a fade out animation
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = 500 // Duration in milliseconds


        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 500 // Duration in milliseconds

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                // Change the image when the fade out animation ends
                binding.animationView.setAnimation(drawableID)
                binding.animationView.playAnimation()

                binding.animationView.startAnimation(fadeIn)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        // Start the fade out animation
        binding.animationView.startAnimation(fadeOut)
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
        textView.animate().alpha(0f).setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    // Change the text when the animation ends
                    textView.text = textToShow
                    // Fade in animation
                    textView.animate().alpha(1f).setDuration(500).setListener(null)
                }
            })
    }

    fun onNewIntent(intent: Intent?, context: Context) {

        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent?.action) {
            val tag: Tag? = intent.extras?.getParcelable(NfcAdapter.EXTRA_TAG)
            if (tag?.techList?.contains("android.nfc.tech.IsoDep") == true) {

                val bacKey: BACKeySpec = BACKey(args.documentID, DateParser.parseDateFromSlashFormatToRaw(args.birthDate), DateParser.parseDateFromSlashFormatToRaw(args.expirationDate))

                viewModel.readPassportData(IsoDep.get(tag), bacKey)

            } else {
                Toast.makeText(context, "Incorrect Data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val adapter = NfcAdapter.getDefaultAdapter(context)
        if (adapter != null) {
            val intent = Intent(context, context?.javaClass)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            val pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
            val filter = arrayOf(arrayOf("android.nfc.tech.IsoDep"))
            adapter.enableForegroundDispatch(activity, pendingIntent, null, filter)
        }
    }

    override fun onPause() {
        super.onPause()
        val adapter = NfcAdapter.getDefaultAdapter(context)
        adapter?.disableForegroundDispatch(activity)
    }
}