package com.fergodev.savebiyuyos


import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fergodev.savebiyuyos.databinding.ActivityEstadoFinancieroBinding
import kotlinx.coroutines.launch


class EstadoFinancieroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEstadoFinancieroBinding
    private lateinit var db: AppDatabase
    private lateinit var ingresosDao: IngresoDao
    private lateinit var gastosDao: GastoDao
    private lateinit var ahorroDao: AhorroDao
    private lateinit var deudaDao: DeudaDao
    private lateinit var ingresosList: List<Ingreso>
    private lateinit var gastosListFijo: List<Gasto>
    private lateinit var gastosListExtra: List<Gasto>
    private lateinit var ahorrosList: List<Ahorro>
    private lateinit var deudasList: List<Deuda>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstadoFinancieroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar base de datos
        db = AppDatabase.getInstance(this)
        ingresosDao = db.ingresoDao()
        gastosDao = db.gastoDao()
        ahorroDao = db.ahorroDao()
        deudaDao = db.deudaDao()

        // Cargar los datos y actualizar la UI
        cargarResumenFinanciero()

        binding.btnVerDetalles.setOnClickListener {
            Toast.makeText(this@EstadoFinancieroActivity, "No hay más meses por mostrar", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun cargarResumenFinanciero() {
        lifecycleScope.launch {
            try {
                // Obtener ingresos, egresos y ahorros desde la base de datos
                ingresosList = ingresosDao.obtenerIngresos()                            // Método para obtener ingresos
                gastosListFijo = gastosDao.obtenerGastosPorTipo("Fijo")        // Método para obtener gastos
                gastosListExtra = gastosDao.obtenerGastosPorTipo("Extra")
                ahorrosList = ahorroDao.obtenerAhorros()                                // Método para obtener ahorros
                deudasList = deudaDao.obtenerDeudas()                                   //Método para obtener deudas

                // Calcular el total de ingresos, egresos, y el balance
                val totalIngresos = ingresosList.sumOf { it.monto }
                val totalGastosFijos = gastosListFijo.sumOf { it.monto }
                val totalGastosExtras = gastosListExtra.sumOf { it.monto }
                val totalEgresos = totalGastosFijos + totalGastosExtras
                val totalAhorros = ahorrosList.sumOf { it.abonado }
                val totalDeudas = deudasList.sumOf { it.abonado }

                // Calcular el balance
                val balance = totalIngresos - totalEgresos - totalAhorros - totalDeudas

                // Mostrar los datos en los TextViews
                binding.textIngresos.text = "Ingresos: $${"%.2f".format(totalIngresos)}"
                binding.textEgresos.text = "Egresos: $${"%.2f".format(totalEgresos)}"
                binding.textAhorros.text = "Abonado a deudas: $${"%.2f".format(totalDeudas)}"
                binding.textDeudas.text = "Ahorros (de mes): $${"%.2f".format(totalAhorros)}"

                // Configurar el texto y color del balance
                binding.textBalance.text = when {
                    balance > 0 -> {
                        binding.textBalance.setTextColor(getColor(R.color.green)) // Cambia "R.color.green" por un color definido en tus recursos
                        "Balance: +$${"%.2f".format(balance)}"
                    }
                    balance < 0 -> {
                        binding.textBalance.setTextColor(getColor(R.color.red)) // Cambia "R.color.red" por un color definido en tus recursos
                        "Balance: -$${"%.2f".format(-balance)}" // Se muestra en formato positivo pero con signo negativo
                    }
                    else -> {
                        binding.textBalance.setTextColor(getColor(R.color.neutral)) // Cambia "R.color.neutral" según un color que definas
                        "Balance: $0.00"
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@EstadoFinancieroActivity, "Error al cargar los datos", Toast.LENGTH_LONG).show()
            }
        }
    }


}
