package posmoveis.sparzianello.fuelcheck

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import posmoveis.sparzianello.fuelcheck.databinding.ActivityFuelTypeListBinding

class FuelTypeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFuelTypeListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFuelTypeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fuelType = intent.getIntExtra("fuelType", 0)

        binding.lvFuelTypes.setOnItemClickListener { _, _, position, _ ->

            val fuelTypes = resources.getStringArray(R.array.fuel)
            val selectedItemDescription = fuelTypes[position]

            intent.putExtra("itemDescription", selectedItemDescription)
            intent.putExtra("fuelTypeRequested", fuelType)

            setResult(RESULT_OK, intent)

            finish()
        }
    }
}