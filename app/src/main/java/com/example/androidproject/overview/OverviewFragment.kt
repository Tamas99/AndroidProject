package com.example.androidproject.overview

import android.content.Context
import android.content.Context.MODE_WORLD_READABLE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidproject.R
import com.example.androidproject.data.UserViewModel
import com.example.androidproject.databinding.FragmentOverviewBinding
import com.example.androidproject.network.Restaurant
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.grid_view_item.*
import java.util.*

/**
 * This fragment shows the the status of the opentable web services transaction.
 */
class OverviewFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentOverviewBinding
    private var itemPos = -1
    var cur_page_var: Int = 1

    /**
     * Lazily initialize our [OverviewViewModel].
     */
    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this).get(OverviewViewModel::class.java)
    }

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentOverviewBinding.inflate(inflater)


        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })



        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(OverviewFragmentDirections.actionShowDetail(it))
                viewModel.displayPropertyDetailsComplete()
            }
        })

        viewModel.cityProperties.observe(viewLifecycleOwner, Observer<List<String>> {
            binding.spinnerCity.adapter = ArrayAdapter(requireContext(), R.layout.spinner_text_view, it)
            if (itemPos != -1) {
                binding.spinnerCity.setSelection(itemPos)
            }
            binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    onCityClicked(parent!!)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        })

        binding.nextButton.setOnClickListener {
            onNextClicked()
        }

        binding.prevButton.setOnClickListener {
            onPrevClicked()
        }

        return binding.root
    }

    private fun onPrevClicked() {
        if (cur_page_var == 1) return
        Timer().schedule(object : TimerTask() {
            override fun run() {
                binding.nextButton.isClickable = false
                cur_page_var -= 1
                viewModel.updatePageFilter(binding.spinnerCity.selectedItem.toString(), cur_page_var.toString())
                binding.nextButton.isClickable = true
            }
        }, 200)
    }

    private fun onNextClicked() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                binding.nextButton.isClickable = false
                cur_page_var += 1
                viewModel.updatePageFilter(binding.spinnerCity.selectedItem.toString(), cur_page_var.toString())
                binding.nextButton.isClickable = true
            }
        }, 200)
    }

    private fun onCityClicked(parent: AdapterView<*>) {
        viewModel.updateFilter(parent?.selectedItem.toString())
        if(parent?.selectedItemPosition != 0) {
            itemPos = parent?.selectedItemPosition
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = parentFragmentManager?.findFragmentById(R.id.nav_host_fragment)
        navController = navHostFragment!!.findNavController()
        bottom_nav.setupWithNavController(navController)
    }
}
