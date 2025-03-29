package posmoveis.sparzianello.fuelcheck

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import posmoveis.sparzianello.fuelcheck.databinding.ActivityMainBinding
import androidx.core.graphics.drawable.toDrawable
import posmoveis.sparzianello.fuelcheck.databinding.CustomDialogBinding
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var fuel1: String = ""
    private var fuel2: String = ""
    private var hintFuel1Consumption: String = ""
    private var hintFuel1Price: String = ""
    private var hintFuel2Consumption: String = ""
    private var hintFuel2Price: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            fuel1 = savedInstanceState.getString("fuel1", "")
            fuel2 = savedInstanceState.getString("fuel2", "")
            hintFuel1Consumption = savedInstanceState.getString("hintFuel1Consumption", "")
            hintFuel1Price = savedInstanceState.getString("hintFuel1Price", "")
            hintFuel2Consumption = savedInstanceState.getString("hintFuel2Consumption", "")
            hintFuel2Price = savedInstanceState.getString("hintFuel2Price", "")

            binding.editFuelType1Consumption.setText(savedInstanceState.getString("fuelType1Consumption", ""))
            binding.editFuelType1Price.setText(savedInstanceState.getString("fuelType1Price", ""))
            binding.editFuelType2Consumption.setText(savedInstanceState.getString("fuelType2Consumption", ""))
            binding.editFuelType2Price.setText(savedInstanceState.getString("fuelType2Price", ""))

            binding.inputFuelType1Consumption.hint = hintFuel1Consumption
            binding.inputFuelType1Price.hint = hintFuel1Price
            binding.inputFuelType2Consumption.hint = hintFuel2Consumption
            binding.inputFuelType2Price.hint = hintFuel2Price
        }

        binding.buttonSearchFuelType1.setOnClickListener { getFuelType.launch(Intent(this,FuelTypeListActivity::class.java).apply { putExtra("fuelType", 1) }) }
        binding.buttonSearchFuelType2.setOnClickListener { getFuelType.launch(Intent(this,FuelTypeListActivity::class.java).apply { putExtra("fuelType", 2) }) }
        binding.buttonCalc.setOnClickListener { calcConsumption() }
    }

    private val getFuelType = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val fuelSelected = it.data?.getStringExtra("fuelDescription")
            val fuelType = it.data?.getIntExtra("fuelTypeRequested", 0)
            val fuelConsumption = it.data?.getDoubleExtra("fuelConsumption", 0.0)

            when (fuelType) {
                1 -> {
                    fuel1 = fuelSelected.toString()
                    hintFuel1Consumption = getString(R.string.consumption).plus(" ").plus(fuelSelected)
                    hintFuel1Price = getString(R.string.fuel_price).plus(" ").plus(fuelSelected)

                    binding.inputFuelType1Consumption.hint = hintFuel1Consumption
                    if (binding.editFuelType1Consumption.text.isNullOrBlank()) {
                        binding.editFuelType1Consumption.setText(fuelConsumption.toString())
                    }
                    binding.inputFuelType1Price.hint = hintFuel1Price
                }
                else -> {
                    fuel2 = fuelSelected.toString()
                    hintFuel2Consumption = getString(R.string.consumption).plus(" ").plus(fuelSelected)
                    hintFuel2Price = getString(R.string.fuel_price).plus(" ").plus(fuelSelected)

                    binding.inputFuelType2Consumption.hint = hintFuel2Consumption
                    if (binding.editFuelType2Consumption.text.isNullOrBlank()) {
                        binding.editFuelType2Consumption.setText(fuelConsumption.toString())
                    }
                    binding.inputFuelType2Price.hint = hintFuel2Price
                }
            }
        }
    }

    private fun calcConsumption() {
        val priceFuel1 = binding.editFuelType1Price.text.toString().toDoubleOrNull()
        val consumptionFuel1 = binding.editFuelType1Consumption.text.toString().toDoubleOrNull()

        val priceFuel2 = binding.editFuelType2Price.text.toString().toDoubleOrNull()
        val consumptionFuel2 = binding.editFuelType2Consumption.text.toString().toDoubleOrNull()

        if (priceFuel1 == null || priceFuel1 <= 0.0 ||
            priceFuel2 == null || priceFuel2 <= 0.0 ||
            consumptionFuel1 == null || consumptionFuel1 <= 0.0 ||
            consumptionFuel2 == null || consumptionFuel2 <= 0.0
        ) {
            Toast.makeText(this, getString(R.string.input_error), Toast.LENGTH_SHORT).show()
            return
        }

        val fuel1value = priceFuel1 / consumptionFuel1
        val fuel2value = priceFuel2 / consumptionFuel2

        fuel1 = fuel1.ifEmpty { getString(R.string.first_fuel) }
        fuel2 = fuel2.ifEmpty { getString(R.string.second_fuel) }

        if (fuel1value < fuel2value) {
            showCustomDialog(fuel1, fuel1value)
        } else {
            showCustomDialog(fuel2, fuel2value)
        }
    }

    private fun showCustomDialog(fuel: String, fuelValue: Double) {
        val dialogBinding = CustomDialogBinding.inflate(layoutInflater)
        dialogBinding.fuelFinalResult.text = getString(R.string.fuel_final_result).plus(" ").plus(fuel)
        dialogBinding.fuelPriceFinalResult.text = getString(R.string.fuel_price_final_result).plus(" ").plus(formatCurrencyGlobally(fuelValue))

        val dialog = Dialog(this)

        dialog.apply {
            setContentView(dialogBinding.root)
            window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

            dialogBinding.closeButton.setOnClickListener { dismiss() }

            show()
        }
    }

    private fun formatCurrencyGlobally(amount: Double): String {
        val defaultLocale = Locale.getDefault()
        val currencyFormatter = NumberFormat.getCurrencyInstance(defaultLocale)
        return currencyFormatter.format(amount)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("fuel1", fuel1)
        outState.putString("fuel2", fuel2)
        outState.putString("hintFuel1Consumption", hintFuel1Consumption)
        outState.putString("hintFuel1Price", hintFuel1Price)
        outState.putString("hintFuel2Consumption", hintFuel2Consumption)
        outState.putString("hintFuel2Price", hintFuel2Price)
        outState.putString("fuelType1Consumption", binding.editFuelType1Consumption.text.toString())
        outState.putString("fuelType1Price", binding.editFuelType1Price.text.toString())
        outState.putString("fuelType2Consumption", binding.editFuelType2Consumption.text.toString())
        outState.putString("fuelType2Price", binding.editFuelType2Price.text.toString())
    }
}
