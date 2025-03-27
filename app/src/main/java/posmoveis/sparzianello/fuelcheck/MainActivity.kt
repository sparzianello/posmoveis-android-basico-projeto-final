package posmoveis.sparzianello.fuelcheck

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import posmoveis.sparzianello.fuelcheck.databinding.ActivityMainBinding


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
        binding.buttonCalc.setOnClickListener { calcConsumption() }
    }

    private val getFuelType =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val fuelSelected = it.data?.getStringExtra("fuelDescription")
                val fuelType = it.data?.getIntExtra("fuelTypeRequested", 0)
                val fuelConsumption = it.data?.getDoubleExtra("fuelConsumption", 0.0)

                when (fuelType) {
                    1 -> {
                        binding.inputFuelType1Consumption.hint =
                            getString(R.string.consumption).plus(" ").plus(fuelSelected)
                        binding.editFuelType1Consumption.setText(fuelConsumption.toString())
                        binding.inputFuelType1Price.hint =
                            getString(R.string.fuel_price).plus(" ").plus(fuelSelected)
                    }

                    else -> {
                        binding.inputFuelType2Consumption.hint =
                            getString(R.string.consumption).plus(" ").plus(fuelSelected)
                        binding.editFuelType2Consumption.setText(fuelConsumption.toString())
                        binding.inputFuelType2Price.hint =
                            getString(R.string.fuel_price).plus(" ").plus(fuelSelected)
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

        val fuel1 = priceFuel1 / consumptionFuel1
        val fuel2 = priceFuel2 / consumptionFuel2

        if (fuel1 < fuel2) {
            Toast.makeText(this, "O Primeiro Combustível é o mais rentável", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "O Segundo Combustível é o mais rentável", Toast.LENGTH_SHORT).show()
        }
    }
}