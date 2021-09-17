package com.example.mobiusdemoapplication.ui.gpslocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.mobiusdemoapplication.R
import com.example.mobiusdemoapplication.databinding.FragmentGpsLocationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GpsLocationBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: GpsLocationViewModel by viewModels()

    private var _binding: FragmentGpsLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scooterId = requireArguments().getString(SCOOTER_ID) ?: return
        viewModel.locateScooter(scooterId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_gps_location, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGpsLocationBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()
        viewModel.locationState.observe(viewLifecycleOwner) { onLocationStateUpdate(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onLocationStateUpdate(state: LocationState) {
        with(binding) {
            locatingGroup.isVisible = state is LocationState.Locating
            tvOffline.isVisible = state is LocationState.Offline
            foundGroup.isVisible = state is LocationState.Located

            if (state is LocationState.Located) {
                tvScooterPosition.text = "${state.latitude}° N, ${state.longitude}° E"
            }
        }
    }

    companion object {
        private const val SCOOTER_ID = "scooter_id"

        fun instance(scooterId: String) = GpsLocationBottomSheet().apply {
            arguments = bundleOf(SCOOTER_ID to scooterId)
        }
    }
}