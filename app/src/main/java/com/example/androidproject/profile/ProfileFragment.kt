package com.example.androidproject.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidproject.R
import com.example.androidproject.data.User
import com.example.androidproject.data.UserDatabase
import com.example.androidproject.data.UserRepository
import com.example.androidproject.data.UserViewModel
import com.example.androidproject.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentProfileBinding
    private val mUserViewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }
    private val args by navArgs<ProfileFragmentArgs>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater)

        binding.lifecycleOwner = this

        mUserViewModel.getUser().observe(viewLifecycleOwner, Observer<User> {
            showCredentials()
        })

        mUserViewModel.viewModelScope.launch(Dispatchers.IO) {
            getCredentials()
        }

        binding.buttonLogout.setOnClickListener {
            onLogoutClicked()
        }

        return binding.root
    }

    private fun showCredentials() {

        binding.textViewUsernameProfile.text = mUserViewModel.getUser().value?.username
        binding.textViewEmailProfile.text = mUserViewModel.getUser().value?.email

    }

    suspend fun getCredentials() {
        mUserViewModel.readOneData(args.email, args.password)
    }

    private fun onLogoutClicked() {
        val updateUser = User(mUserViewModel.getUser().value!!.id, mUserViewModel.getUser().value!!.username, mUserViewModel.getUser().value!!.password, mUserViewModel.getUser().value!!.email, false)
        mUserViewModel.updateUser(updateUser)
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = parentFragmentManager?.findFragmentById(R.id.nav_host_fragment)
        navController = navHostFragment!!.findNavController()

//        (activity as AppCompatActivity?)!!.setupActionBarWithNavController(navController)

        bottom_nav.setupWithNavController(navController)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}