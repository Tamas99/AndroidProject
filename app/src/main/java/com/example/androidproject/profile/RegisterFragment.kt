package com.example.androidproject.profile

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidproject.R
import com.example.androidproject.data.User
import com.example.androidproject.data.UserViewModel
import com.example.androidproject.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var mUserViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.buttonRegister.setOnClickListener {
            onRegisterClicked()
        }

        binding.textViewToLoginFrag.setOnClickListener {
            onLoginClicked()
        }

        return binding.root
    }

    private fun onLoginClicked() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    private fun onRegisterClicked() {
        if (insertDataIntoDatabase() == true) {
            Toast.makeText(requireContext(), "Register was successful", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        else {
            Toast.makeText(requireContext(), "Register was not successful", Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertDataIntoDatabase(): Boolean {
        val username = editTextUsernameRegister.text.toString()
        val email = editTextEmailRegister.text.toString()
        val password = editTextPasswordRegister.text.toString()

        if(inputCheck(username, email, password)) {
            // Create User Object
            val user = User(0, username, password, email, false)
            // Add Data to Database
            mUserViewModel.addUser(user)
            return true
        }
        else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun inputCheck(username: String, email: String, password: String): Boolean {
        return !(TextUtils.isEmpty(username) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
    }

    fun isTakenEmail(email: String) : Boolean {
        val user = mUserViewModel.getUserByEmail(email)
        if (user == null) {
            return false
        }
        return true
    }
}