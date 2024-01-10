package com.example.passportphotocomparisonthesis.Settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.Utils.Settings
import com.example.passportphotocomparisonthesis.databinding.FragmentSettingsBinding
import java.util.Locale


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var languages: Array<String>
    private lateinit var languagesCode: Array<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val languageImages = intArrayOf(R.drawable.gb, R.drawable.tr, R.drawable.de, R.drawable.fr, R.drawable.es)
        languages = arrayOf("English", "Turkish", "German", "French", "Spanish")
        languagesCode = arrayOf("en", "tr", "de", "fr", "es")
        binding.languageSpinner.adapter = SpinnerAdapter(requireContext(), languageImages, languages)

        val themeImages = intArrayOf(R.drawable.ic_light, R.drawable.ic_dark, R.drawable.ic_system)
        val themes = arrayOf("Light", "Dark", "System")
        binding.themeSpinner.adapter = SpinnerAdapter(requireContext(), themeImages, themes)

        val modelImages = intArrayOf(R.drawable.ic_facenet, R.drawable.ic_mobile_facenet)
        val models = arrayOf("FaceNet", "MobileFaceNet")
        binding.modelSpinner.adapter = SpinnerAdapter(requireContext(), modelImages, models)

        setSpinners()

        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    view?.let {
                        val language = parent.getItemAtPosition(position).toString()
                        when (language) {
                            "English" -> applyLanguage("en")
                            "French" -> applyLanguage("fr")
                            "Turkish" -> applyLanguage("tr")
                            "German" -> applyLanguage("de")
                            "Spanish" -> applyLanguage("es")
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback
                }
            }


        binding.themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                view?.let {
                    val theme = parent.getItemAtPosition(position).toString()
                    when (theme) {
                        "Light" -> applyDarkMode(AppCompatDelegate.MODE_NIGHT_NO)
                        "Dark" -> applyDarkMode(AppCompatDelegate.MODE_NIGHT_YES)
                        "System" -> applyDarkMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.modelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                view?.let {
                    val model = parent.getItemAtPosition(position).toString()
                    when (model) {
                        "FaceNet" -> applyModel("FaceNet")
                        "MobileFaceNet" -> applyModel("MobileFaceNet")
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    fun applyDarkMode(mode: Int) {
        Settings.applyDarkMode(mode)
        // Save the user's dark mode preference
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(getString(R.string.saved_dark_mode_key), mode)
            commit()
        }
    }

    fun applyLanguage(language: String) {
        Settings.setLocale(language, resources)

        // Save the user's language preference
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.saved_language_key), language)
            commit()
        }
    }

    fun applyModel(model: String) {
        // Save the user's language preference
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.saved_model_key), model)
            commit()
        }
    }

    fun setSpinners(){
        // Load the user's language preference
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val defaultLanguage = Locale.getDefault().language
        val savedLanguage = sharedPref.getString(getString(R.string.saved_language_key), defaultLanguage)

        val defaultMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        val savedMode = sharedPref.getInt(getString(R.string.saved_dark_mode_key), defaultMode)

        val defaultModel = "FaceNet"
        val savedModel = sharedPref.getString(getString(R.string.saved_model_key), defaultModel)

        // Set the spinners to the saved preferences
        binding.languageSpinner.setSelection(languagesCode.indexOf(savedLanguage))

        when (savedMode) {
            1 -> binding.themeSpinner.setSelection(0)
            2 -> binding.themeSpinner.setSelection(1)
            -1 -> binding.themeSpinner.setSelection(2)
        }

        when (savedModel) {
            "FaceNet" -> binding.modelSpinner.setSelection(0)
            "MobileFaceNet" -> binding.modelSpinner.setSelection(1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.languageSpinner.onItemSelectedListener = null
        binding.themeSpinner.onItemSelectedListener = null
    }

}