package com.example.mobiusdemoapplication.ui.scooter

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import com.example.mobiusdemoapplication.R
import com.example.mobiusdemoapplication.data.ScootersRepository
import com.example.mobiusdemoapplication.databinding.FragmentScooterBinding
import com.example.mobiusdemoapplication.domain.Scooter
import com.example.mobiusdemoapplication.ui.gpslocation.GpsLocationBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class ScooterFragment : Fragment(R.layout.fragment_scooter) {

    private var _binding: FragmentScooterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScooterViewModel = ScooterViewModel(ScootersRepository())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScooterBinding.bind(view).apply {
            vConnect.setOnClickListener { viewModel.onBluetoothClicked() }
            vLocateScooter.setOnClickListener { viewModel.onLocateClicked() }
            tvSelectedScooter.setOnClickListener { viewModel.onSelectScooterClicked() }
            vLocateScooter.setOnClickListener { viewModel.onLocateClicked() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        with(viewModel) {
            userScooter.observe(viewLifecycleOwner) { it?.let(::onScooterLoaded) }
            connectingStatus.observe(viewLifecycleOwner) { onConnectionStatusUpdate(it) }
            isLoading.observe(viewLifecycleOwner) { binding.loadingGroup.isVisible = it }
            snackText.observe(viewLifecycleOwner) { showSnack(it) }
            selectionScooters.observe(viewLifecycleOwner) { onSelectionUpdate(it) }
            scooterBottomSheet.observe(viewLifecycleOwner) { openBottomSheet(it) }
        }
    }

    private fun onScooterLoaded(scooter: Scooter) {
        with(binding) {
            tvSelectedScooter.text = scooter.name
            tvRangeValue.text = "${scooter.range} км"
            tvBatteryValue.text = "${scooter.battery}%"
            tvTotalValue.text = "${scooter.totalRange} км"
            ivScooter.load(scooter.imageUrl)
        }
    }

    private fun onConnectionStatusUpdate(status: BluetoothConnectingStatus) {
        with(binding) {
            vConnectionProgress.isVisible = status == BluetoothConnectingStatus.CONNECTING
            val color: Int = when (status) {
                BluetoothConnectingStatus.IDLE -> R.color.black
                BluetoothConnectingStatus.CONNECTING -> R.color.gray
                BluetoothConnectingStatus.FAILED -> R.color.red
                BluetoothConnectingStatus.CONNECTED -> R.color.blue
            }.let { ContextCompat.getColor(requireContext(), it) }
            vConnect.setColorFilter(color)
        }
    }

    private fun showSnack(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
    }

    private fun onSelectionUpdate(scooters: List<Scooter>?) {
        binding.llScooters.apply {
            isVisible = scooters != null

            scooters
                ?.also { removeAllViews() }
                ?.forEach { scooter ->
                    TextView(context).apply {
                        text = scooter.name
                        setTextColor(ContextCompat.getColor(context, R.color.black))
                        setOnClickListener { viewModel.onScooterSelected(scooter.id) }
                        layoutParams = ViewGroup.MarginLayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply { bottomMargin = 20 }
                    }.let(::addView)
                }
        }
    }

    private fun openBottomSheet(bottomSheet: ScooterBottomSheet) {
        when (bottomSheet) {
            is ScooterBottomSheet.GpsLocation -> GpsLocationBottomSheet.instance(bottomSheet.scooterId)
        }.show(childFragmentManager, null)
    }
}