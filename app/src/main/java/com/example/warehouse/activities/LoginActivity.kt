package com.example.warehouse.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.warehouse.R
import com.example.warehouse.fragments.LoginFragment
import com.example.warehouse.fragments.RegisterFragment
import com.example.warehouse.fragments.SuccessFragment
import com.example.warehouse.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.runBlocking

class LoginActivity : AppCompatActivity() , LoginFragment.LoginFragmentCallback, RegisterFragment.RegisterFragmentCallback {

    lateinit var loginFragment : LoginFragment
    lateinit var registerFragment: RegisterFragment
    lateinit var successFragment : SuccessFragment
    lateinit var viewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar_login)

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory(application)).get(LoginViewModel::class.java)
        loginFragment = LoginFragment()
        registerFragment = RegisterFragment()
        successFragment = SuccessFragment()

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_main_login, loginFragment)
                .commit()
        }
    }

    override fun onSuccessfulLogin(username : String, token : String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_main_login, successFragment)
            .commit()
        runBlocking {
            viewModel.loadData(username, token)
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun switchToRegister() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_main_login, registerFragment)
            .commit()
    }

    override fun onSuccessfulRegister(username : String, token : String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_main_login, successFragment)
            .commit()
        runBlocking {
            viewModel.loadData(username, token)
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun switchToLogin() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_main_login, loginFragment)
            .commit()
    }
}
