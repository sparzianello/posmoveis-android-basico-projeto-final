package posmoveis.sparzianello.fuelcheck

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import posmoveis.sparzianello.fuelcheck.databinding.ActivityFuelTypeListBinding
import kotlin.math.round
import kotlin.random.Random

class FuelTypeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFuelTypeListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFuelTypeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fuelType = intent.getIntExtra("fuelType", 0)

        binding.lvFuelTypes.setOnItemClickListener { _, _, position, _ ->

            val fuelTypes = resources.getStringArray(R.array.fuel)
            val selectedFuelDescription = fuelTypes[position]
            val selectedFuelConsumption = getFuelConsumption(selectedFuelDescription)


            intent.putExtra("fuelTypeRequested", fuelType)
            intent.putExtra("fuelDescription", selectedFuelDescription)
            intent.putExtra("fuelConsumption", selectedFuelConsumption)

            setResult(RESULT_OK, intent)

            finish()
        }
    }

    private fun getFuelConsumption(selectedItemDescription: String): Double {
        fun formatToTwoDecimalPlaces(value: Double): Double {
            return round(value * 100) / 100
        }

        val consumption = when (selectedItemDescription) {
            "Gasolina" -> Random.nextDouble(8.0, 15.0)
            "Etanol (Álcool)" -> Random.nextDouble(6.0, 12.0)
            "Diesel" -> Random.nextDouble(10.0, 18.0)
            "Gás Natural Veicular (GNV)" -> Random.nextDouble(15.0, 25.0)
            "Biodiesel" -> Random.nextDouble(8.0, 14.0)
            "Bioetanol" -> Random.nextDouble(5.0, 10.0)
            else -> 0.0
        }
        return formatToTwoDecimalPlaces(consumption)
    }
}