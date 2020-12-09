package com.example.androidproject.profile

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
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
import com.example.androidproject.data.*
import com.example.androidproject.databinding.FragmentProfileBinding
import com.example.androidproject.overview.OverviewViewModel
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.lang.Exception

class ProfileFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentProfileBinding
    private val mUserViewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }
    private val args by navArgs<ProfileFragmentArgs>()
    private lateinit var popupMenu: PopupMenu

    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this).get(OverviewViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater)

        binding.lifecycleOwner = this



        mUserViewModel.getUser().observe(viewLifecycleOwner, Observer<User> {
            if (it.status == true) {
                mUserViewModel.getFavsByUser(it.id)
                showCredentials()
            }
        })

        mUserViewModel.viewModelScope.launch(Dispatchers.IO) {
            getCredentials()
        }

        binding.informationIcon.setOnClickListener {
            onInformationClicked()
        }

        return binding.root
    }

    private fun onInformationClicked() {
        popupMenu = PopupMenu(requireContext(), binding.informationIcon)
        popupMenu.inflate(R.menu.information_menu)

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.delete_account -> { showDeleteDialog() }
                R.id.log_out -> { onLogoutClicked() }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
//        Toast.makeText(requireContext(), "show", Toast.LENGTH_SHORT).show()
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Account")
        builder.setMessage("Are you sure you want to delete your account?")
        builder.setPositiveButton("Delete") { dialog, which ->
            mUserViewModel.deleteUser(mUserViewModel.getUser().value!!)
            mUserViewModel.viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Account has been deleted", Toast.LENGTH_SHORT).show()
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
            }
        }
        builder.setNegativeButton("Cancel") {
            dialog, which ->  dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showCredentials() {

        binding.textViewUsernameProfile.text = mUserViewModel.getUser().value?.username
        binding.textViewEmailProfile.text = mUserViewModel.getUser().value?.email

        mUserViewModel.getFavsByUser().observe(viewLifecycleOwner, Observer<List<FavoriteRestaurant>> {
            if (it != null) {
                val rest_name: TextView = TextView(requireContext())
                rest_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
                rest_name.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                for (i in it) {
                    rest_name.append(i.restaurantName)
                    rest_name.append("\n\n")
                }

                binding.favRestaurantsLayout.addView(rest_name)
            }
        })

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