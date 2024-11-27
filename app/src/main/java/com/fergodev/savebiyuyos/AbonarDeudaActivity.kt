package com.fergodev.savebiyuyos

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fergodev.savebiyuyos.databinding.ActivityAbonarDeudaBinding
import kotlinx.coroutines.launch

class AbonarDeudaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAbonarDeudaBinding
    private lateinit var db: AppDatabase
    private lateinit var deudaDao: DeudaDao
    private lateinit var deudasList: List<Deuda>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAbonarDeudaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            db = AppDatabase.getInstance(this)
            deudaDao = db.deudaDao()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Err: ${e.message}", Toast.LENGTH_LONG).show()
        }

        // Configura el spinner para mostrar las deudas existentes
        cargarDeudasASpinner()

        // Configurar botón "Abonar"
        binding.btnAbonar.setOnClickListener {
            val montoAbono = binding.etMontoAbono.text.toString().toDoubleOrNull() ?: 0.0
            if (montoAbono > 0.0) {
                val deudaId = (binding.spinnerDeudas.selectedItem as Deuda).id
                // Llamar al método para abonar
                abonarDeuda(deudaId, montoAbono)
            } else {
                Toast.makeText(this, "Ingrese un monto válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarDeudasASpinner() {
        lifecycleScope.launch {
            deudasList = deudaDao.obtenerDeudasNoLiquidadas()

            // Crear un adaptador para el Spinner con un formato personalizado
            val adapter = object : ArrayAdapter<Deuda>(
                this@AbonarDeudaActivity,
                android.R.layout.simple_spinner_item,
                deudasList
            ) {
                override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                    val view = super.getView(position, convertView, parent)
                    val deuda = getItem(position)

                    // Mostrar solo el nombre de la deuda y monto pendiente
                    val deudaInfo = "${deuda?.id ?: "0"} - ${deuda?.nombre ?: "Desconocido"} - debes: ${"%.2f".format(deuda?.monto?.minus(deuda.abonado) ?: 0.0)} - total: ${"%.2f".format(deuda?.monto ?: 0.0)}"
                    (view as android.widget.TextView).text = deudaInfo

                    return view
                }

                override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val deuda = getItem(position)

                    // Mostrar el mismo formato para el dropdown
                    val deudaInfo = "${deuda?.id ?: "Desconocido"} - ${deuda?.nombre ?: "Desconocido"} - debes: ${"%.2f".format(deuda?.monto?.minus(deuda.abonado) ?: 0.0)} - total: ${"%.2f".format(deuda?.monto ?: 0.0)}"
                    (view as android.widget.TextView).text = deudaInfo

                    return view
                }
            }

            // Establecer el adaptador en el Spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerDeudas.adapter = adapter
        }
    }

    private fun abonarDeuda(id: Int, abono: Double) {
        lifecycleScope.launch {
            try {
                // Obtener la deuda de la base de datos
                val deuda = deudaDao.obtenerDeuda(id)

                // Calcular el monto pendiente
                val montoPendiente = deuda.monto - deuda.abonado

                // Verificar que el abono no exceda el monto pendiente
                if (abono > montoPendiente) {
                    Toast.makeText(this@AbonarDeudaActivity, "No puedes abonar más de lo que queda por pagar", Toast.LENGTH_LONG).show()
                } else {
                    // Si el abono es igual al monto pendiente, eliminar la deuda
                    if (abono == montoPendiente) {
                        deudaDao.eliminarDeuda(deuda)
                        Toast.makeText(this@AbonarDeudaActivity, "Deuda saldada y eliminada", Toast.LENGTH_SHORT).show()
                        finish() // Cerrar
                    } else {
                        // Si no es igual, solo actualizamos el monto abonado
                        deudaDao.abonarDeuda(id, abono)
                        Toast.makeText(this@AbonarDeudaActivity, "Abono realizado correctamente", Toast.LENGTH_SHORT).show()
                        finish() // Cerrar
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@AbonarDeudaActivity, "Error al realizar el abono", Toast.LENGTH_LONG).show()

            }
        }
    }

}
