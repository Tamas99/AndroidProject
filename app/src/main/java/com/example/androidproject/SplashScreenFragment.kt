package com.example.androidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import gr.net.maroulis.library.EasySplashScreen
import kotlinx.coroutines.withTimeout
import okio.Timeout
import java.util.concurrent.TimeUnit


class SplashScreenFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timeout().timeout(4,TimeUnit.SECONDS)
        findNavController().navigate(R.id.action_splashscreen_to_overviewFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SplashScreenFragment()
    }
}