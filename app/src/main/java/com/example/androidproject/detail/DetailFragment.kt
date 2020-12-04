
package com.example.androidproject.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.androidproject.databinding.FragmentDetailBinding

/**
 * This [Fragment] will show the detailed information about a selected piece of Mars real estate.
 */
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        @Suppress("UNUSED_VARIABLE")
        val application = requireNotNull(activity).application
        binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val restaurant = DetailFragmentArgs.fromBundle(arguments!!).selectedProperty

        val viewModelFactory = DetailViewModelFactory(restaurant, application)

        binding.viewModel = ViewModelProviders.of(
                this, viewModelFactory).get(DetailViewModel::class.java
        )

        binding.buttonGoogleMap.setOnClickListener {
            onGoogleMapClicked()
        }

        binding.phoneText.setOnClickListener {
            onPhoneNumberClicked()
        }

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
