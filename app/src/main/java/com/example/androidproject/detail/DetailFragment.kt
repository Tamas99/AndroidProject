
package com.example.androidproject.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.androidproject.R
import com.example.androidproject.data.FavoriteRestaurant
import com.example.androidproject.data.User
import com.example.androidproject.data.UserViewModel
import com.example.androidproject.databinding.FragmentDetailBinding
import com.example.androidproject.network.Restaurant
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var restaurant: Restaurant

    private var isActive: Boolean = false

    private val mUserViewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        @Suppress("UNUSED_VARIABLE")
        val application = requireNotNull(activity).application
        binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        restaurant = DetailFragmentArgs.fromBundle(arguments!!).selectedProperty

        val viewModelFactory = DetailViewModelFactory(restaurant, application)

        binding.viewModel = ViewModelProviders.of(
                this, viewModelFactory).get(DetailViewModel::class.java
        )

        mUserViewModel.getActive()

        binding.favoriteIv.setOnClickListener {
            mUserViewModel.onFavoriteClicked(restaurant.name, mUserViewModel.getUser().value!!.status)
        }

        binding.buttonGoogleMap.setOnClickListener {
            onGoogleMapClicked()
        }

        binding.phoneText.setOnClickListener {
            onPhoneNumberClicked()
        }

        mUserViewModel.getFavRest().observe(viewLifecycleOwner, Observer<FavoriteRestaurant> {
            if (mUserViewModel.getUser().value!!.status == true) {
                if(it.id != 0) {
                    mUserViewModel.deleteFav(it)
                    Toast.makeText(requireContext(), "Restaurant removed from favorites", Toast.LENGTH_SHORT).show()
                }
                else {
                    val favRest = FavoriteRestaurant(0, restaurant.name, mUserViewModel.getUser().value!!.id)
                    mUserViewModel.addFav(favRest)
                    Toast.makeText(requireContext(), "Restaurant added to favorites", Toast.LENGTH_SHORT).show()
                }
            }
        })
        return binding.root
    }

    private fun onPhoneNumberClicked() {
        var number = binding.viewModel?.selectedProperty?.value?.phone
        // Clean the number from other characters
        number = number?.replace("[^0-9]", "")
        // Cut 'x' from the end
        var uri = "tel:" + number?.trim()?.substring(0,10)
        val intent = Intent(Intent.ACTION_DIAL)
        intent.setData(Uri.parse(uri))
        startActivity(intent)
    }

    private fun onGoogleMapClicked() {
        val action = DetailFragmentDirections.actionDetailFragmentToGoogleMapFragment(binding.viewModel?.selectedProperty?.value!!.lng, binding.viewModel?.selectedProperty?.value!!.lat )
        findNavController().navigate(action)
    }
}
