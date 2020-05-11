package com.example.warehouse.fragments

import android.content.Context
import android.content.SharedPreferences
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
import com.example.warehouse.bind.LoginForm
import com.example.warehouse.databinding.FragmentLoginBinding
import com.example.warehouse.utils.Constants.Companion.TOKEN
import com.example.warehouse.utils.Constants.Companion.USERNAME
import com.example.warehouse.utils.Result
import com.example.warehouse.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment() , View.OnClickListener {


    val handler = Handler(Looper.getMainLooper())

    interface LoginFragmentCallback {
        fun onSuccessfulLogin(username : String, token : String)
        fun switchToRegister()
    }

    var loginFragmentCallback : LoginFragmentCallback? = null

    lateinit var viewModel : LoginViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        loginFragmentCallback = context as LoginFragmentCallback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(activity!!, ViewModelProvider.AndroidViewModelFactory(activity!!.application)).
            get(LoginViewModel::class.java)
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.loginForm = viewModel.loginForm
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_login.setOnClickListener(this)

        val switchText = "New User? Register"
        val clickable = object : ClickableSpan() {
            override fun onClick(widget: View) {
                loginFragmentCallback?.switchToRegister()
            }
        }
        val spannable = SpannableString(switchText)
        spannable.setSpan(clickable, 10, 18, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        label_switchtoregister.movementMethod = LinkMovementMethod()
        label_switchtoregister.text = spannable
    }

    override fun onClick(v: View?) {
        v?.let {
            when(it) {
                button_login -> {
                    if(validateForm(viewModel.loginForm)) {
                        GlobalScope.launch {
                            val result = viewModel.login()
                            when (result) {
                                is Result.Success -> {
                                    val pref = PreferenceManager.getDefaultSharedPreferences(context!!)
                                    val editor = pref.edit()
                                    editor.putString(USERNAME, result.data!!.username)
                                    editor.putString(TOKEN, result.data!!.token)
                                    editor.apply()
                                    loginFragmentCallback?.onSuccessfulLogin(result.data!!.username, result.data!!.token)
                                }
                                is Result.Failure -> {
                                    handler.post{
                                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun validateForm(form : LoginForm) : Boolean {
        val usernameBlank = form.username.isBlank()
        val passwordBlank = form.password.isBlank()
        if(usernameBlank) {
            textedit_login_username.setError("Field cannot be blank!")
        }
        if(passwordBlank) {
            textedit_login_password.setError("Field cannot be blank!")
        }
        return !(usernameBlank or passwordBlank)
    }
}