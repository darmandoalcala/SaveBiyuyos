package com.fergodev.savebiyuyos

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fergodev.savebiyuyos.databinding.ActivityEliminarDeudaBinding
import kotlinx.coroutines.launch

class EliminarDeudaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEliminarDeudaBinding
    private lateinit var db: AppDatabase
    private lateinit var deudaDao: DeudaDao
    private lateinit var deudasList: List<Deuda>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEliminarDeudaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            db = AppDatabase.getInstance(this)
            deudaDao = db.deudaDao()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Err: ${e.message}", Toast.LENGTH_LONG).show()
        }

        // Cargar las deudas en el spinner
        cargarDeudasASpinner()

        // Configurar el botón "Eliminar"
        binding.btnEliminar.setOnClickListener {
            val deudaId = (binding.spinnerDeudas.selectedItem as Deuda).id
            eliminarDeuda(deudaId)
        }
    }

    private fun cargarDeudasASpinner() {
        lifecycleScope.launch {
            deudasList = deudaDao.obtenerDeudasNoLiquidadas()

            // Crear un adaptador para el Spinner con un formato personalizado
            val adapter = object : ArrayAdapter<Deuda>(
                this@EliminarDeudaActivity,
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

    private fun eliminarDeuda(id: Int) {
        lifecycleScope.launch {
            try {
                // Eliminar la deuda
                val deuda = deudaDao.obtenerDeuda(id)
                deudaDao.abonarDeuda(id, deuda.monto-deuda.abonado)
                deudaDao.liquidarDeuda(deuda.id)
                Toast.makeText(this@EliminarDeudaActivity, "Deuda eliminada", Toast.LENGTH_SHORT).show()
                finish() // Cerrar la actividad despues de eliminar la deuda
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@EliminarDeudaActivity, "Error al eliminar deuda: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

