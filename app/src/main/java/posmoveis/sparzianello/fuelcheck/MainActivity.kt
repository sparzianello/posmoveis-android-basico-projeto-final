package posmoveis.sparzianello.fuelcheck

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import posmoveis.sparzianello.fuelcheck.databinding.ActivityMainBinding
import androidx.core.graphics.drawable.toDrawable
import posmoveis.sparzianello.fuelcheck.databinding.CustomDialogBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSearchFuelType1.setOnClickListener {
            getFuelType.launch(
                Intent(
                    this,
                    FuelTypeListActivity::class.java
                ).apply { putExtra("fuelType", 1) })
        }
        binding.buttonSearchFuelType2.setOnClickListener {
            getFuelType.launch(
                Intent(
                    this,
                    FuelTypeListActivity::class.java
                ).apply { putExtra("fuelType", 2) })
        }
        binding.buttonCalc.setOnClickListener { showCustomDialog() }
    }

    private val getFuelType =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val fuelSelected = it.data?.getStringExtra("itemDescription")
                val fuelType = it.data?.getIntExtra("fuelTypeRequested", 0)

                when (fuelType) {
                    1 -> {
                        binding.inputFuelType1Consumption.hint =
                            getString(R.string.consumption).plus(" ").plus(fuelSelected)
                        binding.inputFuelType1Price.hint =
                            getString(R.string.fuel_price).plus(" ").plus(fuelSelected)
                    }

                    else -> {
                        binding.inputFuelType2Consumption.hint =
                            getString(R.string.consumption).plus(" ").plus(fuelSelected)
                        binding.inputFuelType2Price.hint =
                            getString(R.string.fuel_price).plus(" ").plus(fuelSelected)
                    }
                }
            }
        }

    private fun showCustomDialog() {
        val dialogBinding = CustomDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(this)

        dialog.apply {
            setContentView(dialogBinding.root)
            window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

            dialogBinding.closeButton.setOnClickListener { dismiss() }

            show()
        }
    }
}