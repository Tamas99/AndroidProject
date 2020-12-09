package com.example.androidproject.map

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.navArgs
import com.example.androidproject.R
import com.example.androidproject.detail.DetailFragmentArgs
import com.example.androidproject.profile.ProfileFragmentArgs
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class GoogleMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var mapFragment: SupportMapFragment? = null
    private val args by navArgs<GoogleMapFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_google_map, container, false)

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return view
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0!!
        val location = LatLng(args.latitude.toDouble(), args.longitude.toDouble())
        googleMap.addMarker(MarkerOptions().position(location).title("Restaurant"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10f), 1000, null)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GoogleMapFragment()
    }
}