package com.example.androidproject.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.example.androidproject.R
import com.example.androidproject.data.*
import com.example.androidproject.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.lang.Exception

class ProfileFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentProfileBinding
    private val mUserViewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }
    private val args by navArgs<ProfileFragmentArgs>()
    private lateinit var popupMenu: PopupMenu
    private val IMAGE_CHOOSE = 1000;
    private val PERMISSION_CODE = 1001;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this

        mUserViewModel.getUser().observe(viewLifecycleOwner, Observer<User> {
            if (it.status == true) {
                mUserViewModel.getFavsByUser(it.id)
                mUserViewModel.getProfilePicture(it.id)
                showCredentials()
            }
        })

        mUserViewModel.viewModelScope.launch(Dispatchers.IO) {
            getCredentials()
        }

        binding.profilePicture.setOnClickListener {
            onProfilePictureClicked()
        }

        binding.informationIcon.setOnClickListener {
            onInformationClicked()
        }

        return binding.root
    }

    private fun onProfilePictureClicked() {
        if (checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED){
            val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions, PERMISSION_CODE)
        } else{
            chooseImageGallery();
        }
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImageGallery()
                } else {
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK){
            val bmp = MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver,
                data?.data
            )
            binding.profilePicture.setImageBitmap(bmp)
            val byteArray = bitmapToByteArray(bmp)
            val profilePicture = ProfilePicture(0, mUserViewModel.getUser().value!!.id, byteArray)
            try {
                mUserViewModel.deleteProfilePicture(mUserViewModel.getUser().value!!.id)
            } catch (e: Exception) {
                Log.d("TagException", "Probably there is no data to remove. " + e)
            }
            mUserViewModel.addProfilePicture(profilePicture)
//            mUserViewModel.getProfilePicture(mUserViewModel.getUser().value!!.id)
        }
    }

    fun bitmapToByteArray(bmp: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()
        return byteArray
    }

    private fun onInformationClicked() {
        popupMenu = PopupMenu(requireContext(), binding.informationIcon)
        popupMenu.inflate(R.menu.information_menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.delete_account -> {
                    showDeleteDialog()
                }
                R.id.log_out -> {
                    onLogoutClicked()
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
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
        builder.setNegativeButton("Cancel") { dialog, which ->  dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showCredentials() {
        mUserViewModel.getImageUri().observe(viewLifecycleOwner, Observer {
            val bmp = BitmapFactory.decodeByteArray(it.image_uri, 0, it.image_uri.size)
            binding.profilePicture.setImageBitmap(bmp)
        })
        binding.textViewUsernameProfile.text = mUserViewModel.getUser().value?.username
        binding.textViewEmailProfile.text = mUserViewModel.getUser().value?.email
        mUserViewModel.getFavsByUser().observe(
            viewLifecycleOwner,
            Observer<List<FavoriteRestaurant>> {
                if (it != null) {
                    val rest_name = TextView(requireContext())
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

    fun getCredentials() {
        mUserViewModel.readOneData(args.email, args.password)
    }

    private fun onLogoutClicked() {
        val updateUser = User(
            mUserViewModel.getUser().value!!.id,
            mUserViewModel.getUser().value!!.username,
            mUserViewModel.getUser().value!!.password,
            mUserViewModel.getUser().value!!.email,
            false
        )
        mUserViewModel.updateUser(updateUser)
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = parentFragmentManager?.findFragmentById(R.id.nav_host_fragment)
        navController = navHostFragment!!.findNavController()
        bottom_nav.setupWithNavController(navController)
    }
}