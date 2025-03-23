package posmoveis.sparzianello.fuelcheck

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import posmoveis.sparzianello.fuelcheck.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editFuelType1Consumption.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.inputFuelType1Consumption.hint = getString(R.string.app_name)
                binding.inputFuelType1Price.hint = getString(R.string.fuel_value)
            } else {
                binding.inputFuelType1Price.hint = getString(R.string.app_name)
                binding.inputFuelType1Consumption.hint = getString(R.string.consumption)
            }
        }

    }
}