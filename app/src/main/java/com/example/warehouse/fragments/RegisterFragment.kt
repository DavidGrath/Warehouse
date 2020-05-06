package com.example.warehouse.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.warehouse.bind.RegisterForm
import com.example.warehouse.databinding.FragmentRegisterBinding
import com.example.warehouse.utils.Constants
import com.example.warehouse.utils.Result
import com.example.warehouse.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() , View.OnClickListener {

    interface RegisterFragmentCallback {
        fun switchToLogin()
        fun onSuccessfulRegister()
    }

    lateinit var viewModel : LoginViewModel
    var registerFragmentCallback : RegisterFragmentCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        registerFragmentCallback = context as RegisterFragmentCallback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(activity!!, ViewModelProvider.AndroidViewModelFactory(activity!!.application))
            .get(LoginViewModel::class.java)
        val binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.registerForm = viewModel.registerForm
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_register.setOnClickListener(this)

        val switchText = "Already have an account? Log in"
        val clickable = object : ClickableSpan() {
            override fun onClick(widget: View) {
                registerFragmentCallback?.switchToLogin()
            }
        }
        val spannable = SpannableString(switchText)
        spannable.setSpan(clickable, 25, 31, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        label_switchtologin.movementMethod = LinkMovementMethod()
        label_switchtologin.text = spannable
    }

    override fun onClick(v: View?) {
        v?.let {
            when(it) {
                button_register -> {
                    if(validateForm(viewModel.registerForm)) {
                        GlobalScope.launch {
                            val result = viewModel.register()
                            when(result) {
                                is Result.Success -> {
                                    val pref = PreferenceManager.getDefaultSharedPreferences(context!!)
                                    val editor = pref.edit()
                                    editor.putString(Constants.USERNAME, result.data!!.username)
                                    editor.apply()
                                    registerFragmentCallback?.onSuccessfulRegister()
                                }
                                is Result.Failure -> {
                                    Toast.makeText(context!!, "Error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun validateForm(form : RegisterForm) : Boolean {
        val usernameBlank = form.username.isBlank()
        val passwordBlank = form.password.isBlank()
        val confirmBlank = form.confirmPassword.isBlank()
        val passwordsMatch = form.password.equals(form.confirmPassword)
        if(usernameBlank) {
            textedit_register_username.setError("Field must not be blank!")
        }
        if(passwordBlank) {
            textedit_register_password.setError("Field must not be blank!")
        }
        if(confirmBlank) {
            textedit_register_confirm.setError("Field must not be blank!")
        }
        if(!passwordsMatch) {
            textedit_register_confirm.setError("Passwords do not match!")
        }
        return !(usernameBlank or passwordBlank or confirmBlank or !passwordsMatch)
    }
}