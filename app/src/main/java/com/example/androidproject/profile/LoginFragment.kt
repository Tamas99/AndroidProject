package com.example.androidproject.profile

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidproject.R
import com.example.androidproject.data.User
import com.example.androidproject.data.UserViewModel
import com.example.androidproject.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.bottom_nav
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.lang.Exception
import java.lang.NullPointerException

class LoginFragment : Fragment() {

    private lateinit var navController: NavController

    private val mUserViewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentLoginBinding.inflate(inflater)

        binding.lifecycleOwner = this

        mUserViewModel.viewModelScope.launch(Dispatchers.Main) {
            checkUser()
        }

        binding.buttonLogin.setOnClickListener {
            mUserViewModel.viewModelScope.launch(Dispatchers.Main) {
                onLoginClicked()
            }
        }

        binding.textViewToRegisterFrag.setOnClickListener {
            onRegisterClicked()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = parentFragmentManager?.findFragmentById(R.id.nav_host_fragment)
        navController = navHostFragment!!.findNavController()

//        (activity as AppCompatActivity?)!!.setupActionBarWithNavController(navController)

        bottom_nav.setupWithNavController(navController)
    }

    suspend fun checkUser() {
        mUserViewModel.getUser().observe(viewLifecycleOwner, Observer<User> { user ->
            if (user?.status == true) {
                val action = LoginFragmentDirections.actionLoginFragmentToProfileFragment(user.email, user.password)
                findNavController().navigate(action)
            }
        })
        mUserViewModel.getActive()
    }

    suspend fun onLoginClicked() {
        if (editTextEmail.text.toString().isEmpty() || editTextPassword.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
            return
        }
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        mUserViewModel.getUser().observe(viewLifecycleOwner, Observer<User> { user ->
            if (user?.status == true) {
                if(findNavController().currentDestination?.id == R.id.loginFragment) {
                    val action = LoginFragmentDirections.actionLoginFragmentToProfileFragment(user.email, user.password)
                    Toast.makeText(requireContext(), "Logged in successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(action)
                }
            }
//            else {
//                Toast.makeText(requireContext(), "Wrong email or password", Toast.LENGTH_SHORT).show()
//            }
        })
        mUserViewModel.readOneData(email, password)
    }

    fun onRegisterClicked() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun inputCheck(username: String, email: String, password: String): Boolean {
        return !(TextUtils.isEmpty(username) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
    }

    companion object {

        @JvmStatic
        fun newInstance() = LoginFragment()
    }


}