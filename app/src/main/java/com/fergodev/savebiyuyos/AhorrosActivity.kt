package com.fergodev.savebiyuyos

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fergodev.savebiyuyos.databinding.ActivityAhorrosBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AhorrosActivity : AppCompatActivity() {

    private lateinit var spinnerAhorros: Spinner
    private lateinit var etAbono: EditText
    private lateinit var btnAbonarAhorro: Button
    private lateinit var btnEliminarAhorro: Button
    private lateinit var textAbono: TextView
    private lateinit var binding: ActivityAhorrosBinding
    private lateinit var db: AppDatabase
    private lateinit var ahorroDao: AhorroDao
    private lateinit var ahorrosList: List<Ahorro>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAhorrosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            db = AppDatabase.getInstance(this)
            ahorroDao = db.ahorroDao()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Err: ${e.message}", Toast.LENGTH_LONG).show()
        }

        // Inicializar vistas
        spinnerAhorros = binding.spinnerAhorros
        etAbono = binding.etAbono
        btnAbonarAhorro = binding.btnAbonarAhorro
        btnEliminarAhorro = binding.btnEliminarAhorro
        textAbono = binding.textAbono

        // Inicialmente ocultamos los campos de abono y eliminar
        textAbono.visibility = View.GONE
        etAbono.visibility = View.GONE
        btnAbonarAhorro.visibility = View.GONE
        btnEliminarAhorro.visibility = View.GONE
        spinnerAhorros.visibility = View.GONE

        // Botón para crear ahorro
        val etDescripcion = findViewById<EditText>(R.id.etNombre)
        val etMontoMeta = findViewById<EditText>(R.id.etMonto)
        val etFechaMeta = findViewById<EditText>(R.id.etFecha)

        binding.btnCrear.setOnClickListener {
            val descripcion = etDescripcion.text.toString().trim()
            val montoMeta = etMontoMeta.text.toString().toDoubleOrNull()
            val fechaMeta = etFechaMeta.text.toString().trim()

            // Validaciones básicas
            if (descripcion.isEmpty() || montoMeta == null || montoMeta <= 0 || fechaMeta.isEmpty()) {
                Toast.makeText(this, "Ingrese una descripción y un monto válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear el objeto Ahorro
            val nuevoAhorro = Ahorro(
                descripcion = descripcion,
                montoMeta = montoMeta,
                abonado = 0.0,
                fechaMeta = fechaMeta
            )

            // Guardar en la base de datos
            lifecycleScope.launch {
                try {
                    ahorroDao.insertarAhorro(nuevoAhorro)
                    Toast.makeText(this@AhorrosActivity, "Ahorro creado con éxito", Toast.LENGTH_SHORT).show()

                    // Opcional: Limpiar los campos
                    etDescripcion.text.clear()
                    etMontoMeta.text.clear()
                    etFechaMeta.text.clear()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@AhorrosActivity, "Error al crear ahorro: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Botón para mostrar los ahorros
        binding.btnVer.setOnClickListener {
            // Mostrar los campos y botones de abono y eliminar al hacer clic en "Ver Ahorros"
            textAbono.visibility = View.VISIBLE
            etAbono.visibility = View.VISIBLE
            btnAbonarAhorro.visibility = View.VISIBLE
            btnEliminarAhorro.visibility = View.VISIBLE
            spinnerAhorros.visibility = View.VISIBLE

            // Configura el spinner para mostrar los ahorros existentes
            cargarAhorrosASpinner()
        }

        // Configurar botón "Abonar"
        binding.btnAbonarAhorro.setOnClickListener {
            val montoAbono = binding.etAbono.text.toString().toDoubleOrNull() ?: 0.0
            if (montoAbono > 0.0) {
                val ahorroId = (binding.spinnerAhorros.selectedItem as Ahorro).id
                // Llamar al método para abonar
                abonarAhorro(ahorroId, montoAbono)
            } else {
                Toast.makeText(this, "Ingrese un monto válido", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el botón "Eliminar"
        binding.btnEliminarAhorro.setOnClickListener {
            val ahorroId = (binding.spinnerAhorros.selectedItem as Ahorro).id
            eliminarAhorro(ahorroId)
        }
    }

    private fun cargarAhorrosASpinner() {
        lifecycleScope.launch {
            ahorrosList = ahorroDao.obtenerAhorrosNoLiquidados()

            if (ahorrosList.isNotEmpty()) {
                val adapter = object : ArrayAdapter<Ahorro>(
                    this@AhorrosActivity,
                    android.R.layout.simple_spinner_item,
                    ahorrosList
                ) {
                    override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                        val view = super.getView(position, convertView, parent)
                        val ahorro = getItem(position)

                        // Mostrar solo el nombre de la deuda y monto pendiente
                        val ahorroInfo = "${ahorro?.id} - ${ahorro?.descripcion ?: "Desconocido"} - ${ahorro?.montoMeta} - falta: ${"%.2f".format(ahorro?.montoMeta?.minus(ahorro.abonado) ?: 0.0)} - antes de: ${ahorro?.fechaMeta}"
                        (view as TextView).text = ahorroInfo

                        return view
                    }

                    override fun getDropDownView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                        val view = super.getDropDownView(position, convertView, parent)
                        val ahorro = getItem(position)

                        // Mostrar el mismo formato para el dropdown
                        val ahorroInfo = "${ahorro?.id} - ${ahorro?.descripcion ?: "Desconocido"} - ${ahorro?.montoMeta} - falta: ${"%.2f".format(ahorro?.montoMeta?.minus(ahorro.abonado) ?: 0.0)} - antes de: ${ahorro?.fechaMeta}"
                        (view as TextView).text = ahorroInfo

                        return view
                    }
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerAhorros.adapter = adapter
            } else {
                Toast.makeText(this@AhorrosActivity, "No hay ahorros disponibles", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun abonarAhorro(id: Int, abono: Double) {
        lifecycleScope.launch {
            try {
                // Mover la operación de obtener el ahorro a un hilo secundario (IO)
                val ahorro = withContext(Dispatchers.IO) {
                    ahorroDao.obtenerAhorro(id)
                }

                // Si no se encuentra el ahorro, se muestra un mensaje de error y se sale
                if (ahorro == null) {
                    Toast.makeText(this@AhorrosActivity, "Ahorro no encontrado", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Calcular el monto pendiente
                val montoPendiente = (ahorro.montoMeta) - (ahorro.abonado)

                // Verificar que el abono no exceda el monto pendiente
                if (abono > montoPendiente) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AhorrosActivity, "No puedes abonar más de lo que deseas ahorrar", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Si el abono es igual al monto pendiente, eliminar el ahorro
                    if (abono == montoPendiente) {
                        withContext(Dispatchers.IO) {
                            ahorroDao.abonarAhorro(id, abono)
                            ahorroDao.liquidarAhorro(id)
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@AhorrosActivity, "Ahorro completado y eliminado", Toast.LENGTH_SHORT).show()
                            finish() // Cerrar la actividad
                        }
                    } else {
                        // Si no es igual, solo actualizamos el monto abonado
                        withContext(Dispatchers.IO) {
                            ahorroDao.abonarAhorro(id, abono)
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@AhorrosActivity, "Abono realizado a tu ahorro correctamente", Toast.LENGTH_SHORT).show()
                            finish() // Cerrar la actividad
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AhorrosActivity, "Error al realizar el abono", Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    private fun eliminarAhorro(id: Int) {
        lifecycleScope.launch {
            try {
                // Eliminar ahorro
                val ahorro = ahorroDao.obtenerAhorro(id)
                if (ahorro != null) {
                    ahorroDao.eliminarAhorro(ahorro)
                    Toast.makeText(this@AhorrosActivity, "Ahorro eliminado con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AhorrosActivity, "Ahorro no encontrado", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@AhorrosActivity, "Error al eliminar ahorro: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


}
